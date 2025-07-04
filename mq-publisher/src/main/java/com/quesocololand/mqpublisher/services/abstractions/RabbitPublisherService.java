package com.quesocololand.mqpublisher.services.abstractions;

import com.quesocololand.mqpublisher.models.VisitorCount;

public interface RabbitPublisherService {
    //Methods of RabbitPublisherService
    void publish(VisitorCount message);
}
