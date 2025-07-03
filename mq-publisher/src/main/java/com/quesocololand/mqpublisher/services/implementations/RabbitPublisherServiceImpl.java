package com.quesocololand.mqpublisher.services.implementations;

import com.quesocololand.mqpublisher.models.VisitorCount;
import com.quesocololand.mqpublisher.services.abstractions.RabbitPublisherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class RabbitPublisherServiceImpl implements RabbitPublisherService {
    //Fields of RabbitPublisherServiceImpl
    @Value("${rabbitmq.exchange.visitor-counts}")
    private String exchangeName;
    @Value("${rabbitmq.routingkey.visitor-counts}")
    private String routingKeyPrefix;
    private final RabbitTemplate rabbitTemplate;    //In order not to make the cast

    //Constructors of RabbitPublisherServiceImpl
    //Field setters of RabbitPublisherServiceImpl (setters)
    //Field getters of RabbitPublisherServiceImpl (getters)
    //Methods of RabbitPublisherServiceImpl
    @Override
    public void publish(VisitorCount message) {
        log.info("Publishing some visitor counts into the queue");

        if (message.getRegisteredAt() == null) {
            message.setRegisteredAt(LocalDateTime.now());
        }

        // Create a more specific routing key, e.g., using the attractionId
        String specificRoutingKey = this.routingKeyPrefix.replace("#", message.getAttractionId() != null ? message.getAttractionId() : UUID.randomUUID().toString());   // If the message routing key is visitor_counts.#, the # will be replaced by its ID.

        //Publish the message
        this.rabbitTemplate.convertAndSend(this.exchangeName, specificRoutingKey, message);
    }
}
