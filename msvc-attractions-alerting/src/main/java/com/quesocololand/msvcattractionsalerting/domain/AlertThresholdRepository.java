package com.quesocololand.msvcattractionsalerting.domain;

import java.util.Optional;

public interface AlertThresholdRepository {
    //Fields of AlertThresholdRepository
    //Constructors of AlertThresholdRepository
    //Field setters of AlertThresholdRepository (setters)
    //Field getters of AlertThresholdRepository (getters)
    //Methods of AlertThresholdRepository
    Optional<AlertThreshold> findById(String id);
    void save(AlertThreshold alertThreshold);
}
