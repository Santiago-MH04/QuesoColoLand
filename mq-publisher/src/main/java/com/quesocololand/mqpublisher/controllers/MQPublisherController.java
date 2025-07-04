package com.quesocololand.mqpublisher.controllers;

import com.quesocololand.mqpublisher.models.VisitorCount;
import com.quesocololand.mqpublisher.services.abstractions.RabbitPublisherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/publish")
@RequiredArgsConstructor
@Tag(
    name = "messaging",
    description = "messages publisher"
)
public class MQPublisherController {
    //Fields of MQPublisherController
    private final RabbitPublisherService rabbitPublisherService;

    //Methods of MQPublisherController
    @PostMapping
    @Operation(
        summary = "message queuer endpoint",
        description = "creates and queues one message at RabbitMQ",
        tags = {"queuing", "message"},
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "uses a VisitorCount object to queue a message at RabbitMQ",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = VisitorCount.class)
            )
        ),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "message successfully queued",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = String.class)
                )
            )
        }
    )
    public ResponseEntity<String> sendTestVisitorCountMessage(@RequestBody VisitorCount message) {
        this.rabbitPublisherService.publish(message);
        return ResponseEntity.ok("Visitor counting message sent to RabbitMQ.");
    }
}
