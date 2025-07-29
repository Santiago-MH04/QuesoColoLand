package com.quesocololand.msvcattractionsalerting.services.abstractions;

import com.quesocololand.msvcattractionsalerting.models.dto.VisitorCountDTO;

public interface RabbitVisitorCountsPublisherService {
    //Methods of RabbitVisitorCountsPublisherService
    void publishVisitorCount(VisitorCountDTO message);
}
