package com.quesocololand.msvcattractionsalerting.mvcClassicArchitecture.services.abstractions;

import com.quesocololand.msvcattractionsalerting.mvcClassicArchitecture.models.dto.VisitorCountDTO;

public interface RabbitVisitorCountsPublisherService {
    //Methods of RabbitVisitorCountsPublisherService
    void publishVisitorCount(VisitorCountDTO message);
}
