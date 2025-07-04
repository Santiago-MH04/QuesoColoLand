package com.quesocololand.msvcattractions.messaging;

import com.quesocololand.msvcattractions.models.VisitorCount;
import com.quesocololand.msvcattractions.services.abstractions.VisitorCountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class VisitorCountConsumer {
    //Fields of VisitorCountConsumer
    private final VisitorCountService visitorCountService;

    //Methods of VisitorCountConsumer
    @RabbitListener(queues = {"${rabbitmq.queue.visitor-counts}"})
    public void receiveVisitorCountMessage(VisitorCount visitorCount) {
        log.info("Message received of visitor counting: {}", visitorCount);
        try {
            // To process and save the message using the service
            VisitorCount visitorCountSaved = this.visitorCountService.save(visitorCount);
            log.info("Visitor count persisted in database: {}", visitorCountSaved.getId());
        } catch (Exception e) {
            // Error handling: if occurs an error while persisting, the message could be ser recovered
            // or sent to a "dead-letter queue" depending on RabbitMQ’s configuration
            log.error("Error when processing and saving the counting message: {}", e.getMessage());
            // On a production environment, consider:
            // - Throw a AmqpRejectAndDontRequeueException to send to dead-letter queue
            // - Register the error on a logs system
            // - Notify to a monitoring system
        }
    }
}
