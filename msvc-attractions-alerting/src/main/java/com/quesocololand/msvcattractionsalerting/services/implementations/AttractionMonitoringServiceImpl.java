package com.quesocololand.msvcattractionsalerting.services.implementations;

import com.quesocololand.msvcattractionsalerting.models.AttractionAlertConfig;
import com.quesocololand.msvcattractionsalerting.models.VisitorCount;
import com.quesocololand.msvcattractionsalerting.models.dto.VisitorCountAlert;
import com.quesocololand.msvcattractionsalerting.repositories.AttractionAlertConfigRepository;
import com.quesocololand.msvcattractionsalerting.repositories.VisitorCountRepository;
import com.quesocololand.msvcattractionsalerting.services.abstractions.AttractionMonitoringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttractionMonitoringServiceImpl implements AttractionMonitoringService {
    //Fields of AttractionMonitoringServiceImpl
    private final AttractionAlertConfigRepository repoAlertConfig;
    private final VisitorCountRepository repoVisitorCount;

    private static final int EXECUTION_PERIOD_IN_SECONDS = 300000;

        @Value("${rabbitmq.queue.visitor-counts-alert}")
        private String alertQueueName;

    @Value("${rabbitmq.exchange.visitor-counts}")
    private String exchangeName;
    @Value("${rabbitmq.routingkey.visitor-counts-alert}")
    private String routingKeyPrefix;
    private final RabbitTemplate rabbitTemplate;

    //Constructors of AttractionMonitoringServiceImpl
    //Field setters of AttractionMonitoringServiceImpl (setters)
    //Field getters of AttractionMonitoringServiceImpl (getters)
    //Methods of AttractionMonitoringServiceImpl
    @Override
    @Scheduled(fixedRate = EXECUTION_PERIOD_IN_SECONDS) // Executing every 300 s (5 min)
    public void monitorAttractionOccupancy() {
        log.info("Initialising monitoring task of attraction occupancy.");

        // 1. Get all the alert configurations
        List<AttractionAlertConfig> allAlertConfigs = this.repoAlertConfig.findAll();

        // Group configurations by attractionId for a quick access
        Map<String, AttractionAlertConfig> configMap = allAlertConfigs.stream()
            .collect(Collectors.toMap(
                AttractionAlertConfig::getAttractionId,
                config -> config,
                (existing, replacement) -> existing // Keeping the existing one in case of duplicates
            ));

        // Separate the global configuration in case it exists
        Optional<AttractionAlertConfig> globalConfig = this.repoAlertConfig.findByIsGlobal(true);

        // 2. Get a list with all the unique attractionId on recent data (in order not to iterate all the possible attractions when there’s no data).
        // Esto evita tener que iterar sobre todas las atracciones posibles si no hay datos.
        List<String> distinctAttractionIds = this.repoVisitorCount.findAllAttractionIdsOnly().parallelStream()
            .map(VisitorCount::getAttractionId)
            .distinct()
            .toList();

        distinctAttractionIds.forEach(aID -> {
            AttractionAlertConfig config = configMap.getOrDefault(aID, globalConfig.orElse(null));
            if (config == null){
                log.warn("It hasn’t been found neither a global nor alert configuration for attraction under id '{}'. Skipping monitoring.", aID);
            }
        });

    }

    @Override
    public void processAlertsForType(String attractionId, int thresholdN, int periodMins, VisitorCountAlert.AlertType alertType) {
        if (periodMins <= 0) {
            log.warn("Alert period for {} in attraction {} is invalid ({} minutes). It must be larger than 0.", alertType, attractionId, periodMins);
            return;
        }

        // Calculate time range (i.e. the last 'periodMins' in minutes from now)
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = now.minusMinutes(periodMins).truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime endTime = now.truncatedTo(ChronoUnit.MINUTES); // Truncate to the exact finalof the current minute

        log.debug("Monitoring '{}' for attraction '{}' in range: {} - {} ({} minutes)",
                alertType, attractionId, startTime, endTime, periodMins);

        // --- AGREGATION PIPELINE ADAPTED TO MONITORING ---
        List<Map> results = this.repoVisitorCount.aggregateVisitorCount(attractionId, startTime, endTime, periodMins);

        if (results.isEmpty() || results.getFirst().get("totalCount") == null) {
            log.debug("No visitor data for attraction '{}' during the lapse of {} minutes. No alert triggered.", attractionId, periodMins);
            return; // No data for that period, no alert
        }

        int currentTotalCount = (Integer) results.getFirst().get("totalCount");
        log.debug("Attraction '{}'. Type: '{}'. Current counting in {} minutes: {}. Threshold N: {}",
                attractionId, alertType, periodMins, currentTotalCount, thresholdN);

        // 3. Apply the rules and trigger alerts
        boolean alertTriggered = false;
        String alertMessage = null;

        if (alertType == VisitorCountAlert.AlertType.CONGESTION) {
            if (currentTotalCount >= thresholdN) {
                alertTriggered = true;
                alertMessage = String.format("OCCUPANCY ALERT: Attraction '%s' has surpassed the occupancy threshold. Counting: %d (Threshold: %d) in %d minutes.",
                        attractionId, currentTotalCount, thresholdN, periodMins);
            }
        } else if (alertType == VisitorCountAlert.AlertType.LOW_OCCUPANCY) {
            if (currentTotalCount < thresholdN) {
                alertTriggered = true;
                alertMessage = String.format("LOW OCCUPANCY ALERT: Atraction '%s' below its occupancy threshold. Counting: %d (Threshold: %d) in %d minutes.",
                        attractionId, currentTotalCount, thresholdN, periodMins);
            }
        }

        if (alertTriggered) {
            VisitorCountAlert alert = VisitorCountAlert.builder()
                .attractionId(attractionId)
                .totalCount(currentTotalCount)
                .timestamp(endTime)
                .intervalMinutes(periodMins)
                .alertType(alertType)
                .message(alertMessage)
                .build();

            sendAlertToRabbitMQ(alert);
            log.info("{} alert triggered for attraction '{}': {}", alertType, attractionId, alert.getMessage());
        }
    }

    @Override
    public void sendAlertToRabbitMQ(VisitorCountAlert alert) {
        log.info("Alert triggered and sent to RabbitMQ: {}", alert.getMessage());

        // Create a more specific routing key, e.g., using the attractionId
        String specificRoutingKey = this.routingKeyPrefix.replace("#", alert.getAttractionId() != null ? alert.getAttractionId() : UUID.randomUUID().toString());   // If the message routing key is visitor_counts_alert.#, the # will be replaced by its ID.

        //Trigger the alert
        this.rabbitTemplate.convertAndSend(this.exchangeName, specificRoutingKey, alert);
    }

    @Override
    public AttractionAlertConfig saveAlertConfig(AttractionAlertConfig config) {
        return this.repoAlertConfig.save(config);
    }

    @Override
    public Optional<AttractionAlertConfig> getAlertConfigForAttraction(String attractionId) {
        return this.repoAlertConfig.findByAttractionId(attractionId);
    }

    @Override
    public Optional<AttractionAlertConfig> getGlobalAlertConfig() {
        return this.repoAlertConfig.findByIsGlobal(true);
    }
}
