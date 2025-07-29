package com.quesocololand.msvcattractionsalerting.services.abstractions;

import com.quesocololand.msvcattractionsalerting.models.AttractionAlertConfig;
import com.quesocololand.msvcattractionsalerting.models.dto.VisitorCountAlert;

import java.util.Optional;

public interface AttractionMonitoringService {
    //Fields of AttractionMonitoringService
    //Constructors of AttractionMonitoringService
    //Field setters of AttractionMonitoringService (setters)
    //Field getters of AttractionMonitoringService (getters)
    //Methods of AttractionMonitoringService
    void monitorAttractionOccupancy();
    void processAlertsForType(String attractionId, int thresholdN, int periodMins, VisitorCountAlert.AlertType alertType);
    void sendAlertToRabbitMQ(VisitorCountAlert alert);
    AttractionAlertConfig saveAlertConfig(AttractionAlertConfig config);
    Optional<AttractionAlertConfig> getAlertConfigForAttraction(String attractionId);
    Optional<AttractionAlertConfig> getGlobalAlertConfig();
}
