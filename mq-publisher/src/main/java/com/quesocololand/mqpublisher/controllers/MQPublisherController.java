package com.quesocololand.mqpublisher.controllers;

import com.quesocololand.mqpublisher.config.RabbitMQConfig;
import com.quesocololand.mqpublisher.models.VisitorCount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("api/publish")
@Slf4j
@RequiredArgsConstructor
public class MQPublisherController {
    //Fields of MQPublisherController
    @Value("${rabbitmq.exchange.visitor-counts}")
    private String exchangeName;
    @Value("${rabbitmq.routingkey.visitor-counts}")
    private String routingKeyPrefix;
    private final RabbitTemplate rabbitTemplate;    //In order not to make the cast

    //Constructors of MQPublisherController
    //Field setters of MQPublisherController (setters)
    //Field getters of MQPublisherController (getters)
        //Methods of MQPublisherController
    @PostMapping
    public ResponseEntity<String> sendTestVisitorCountMessage(@RequestBody VisitorCount message) {
        log.info("Publishing some visitor counts into the queue");

        if (message.getRegisteredAt() == null) {
            message.setRegisteredAt(LocalDateTime.now());
        }

        // Create a more specific routing key, e.g., using the attractionId
        String specificRoutingKey = this.routingKeyPrefix.replace("#", message.getAttractionId() != null ? message.getAttractionId() : UUID.randomUUID().toString());   // If the message routing key is visitor_counts.#, the # will be replaced by its ID.

        //Publish the message
        this.rabbitTemplate.convertAndSend(this.exchangeName, specificRoutingKey, message);

        return ResponseEntity.ok("Visitor counting message sent to RabbitMQ.");
    }
}
