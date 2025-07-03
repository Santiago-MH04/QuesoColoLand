package com.quesocololand.mqpublisher.controllers;

import com.quesocololand.mqpublisher.models.VisitorCount;
import com.quesocololand.mqpublisher.services.abstractions.RabbitPublisherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/publish")
@RequiredArgsConstructor
public class MQPublisherController {
    //Fields of MQPublisherController
    private final RabbitPublisherService rabbitPublisherService;

    //Methods of MQPublisherController
    @PostMapping
    public ResponseEntity<String> sendTestVisitorCountMessage(@RequestBody VisitorCount message) {
        this.rabbitPublisherService.publish(message);
        return ResponseEntity.ok("Visitor counting message sent to RabbitMQ.");
    }
}
