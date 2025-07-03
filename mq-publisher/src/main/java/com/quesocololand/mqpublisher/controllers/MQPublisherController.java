package com.quesocololand.mqpublisher.controllers;

import com.quesocololand.mqpublisher.config.RabbitMQConfig;
import com.quesocololand.mqpublisher.models.VisitorCount;
import com.quesocololand.mqpublisher.services.abstractions.RabbitPublisherService;
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
@RequiredArgsConstructor
public class MQPublisherController {
    //Fields of MQPublisherController
    private final RabbitPublisherService rabbitPublisherService;

    //Constructors of MQPublisherController
    //Field setters of MQPublisherController (setters)
    //Field getters of MQPublisherController (getters)
        //Methods of MQPublisherController
    @PostMapping
    public ResponseEntity<String> sendTestVisitorCountMessage(@RequestBody VisitorCount message) {
        this.rabbitPublisherService.publish(message);
        return ResponseEntity.ok("Visitor counting message sent to RabbitMQ.");
    }
}
