package com.quesocololand.msvcattractionsalerting.services.abstractions;

import com.quesocololand.msvcattractionsalerting.models.VisitorCount;

public interface RabbitVisitorCountsPublisherService {
    //Methods of RabbitVisitorCountsPublisherService
    void publish(VisitorCount message);
}
