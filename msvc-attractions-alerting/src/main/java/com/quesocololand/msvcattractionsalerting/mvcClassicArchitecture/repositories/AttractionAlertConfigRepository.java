package com.quesocololand.msvcattractionsalerting.mvcClassicArchitecture.repositories;

import com.quesocololand.msvcattractionsalerting.mvcClassicArchitecture.models.AttractionAlertConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AttractionAlertConfigRepository extends MongoRepository<AttractionAlertConfig, String> {
    //Fields of AttractionAlertConfigRepository
    //Constructors of AttractionAlertConfigRepository
    //Field setters of AttractionAlertConfigRepository (setters)
    //Field getters of AttractionAlertConfigRepository (getters)
    //Methods of AttractionAlertConfigRepository
    public Optional<AttractionAlertConfig> findByAttractionId(String attractionId);
    public Optional<AttractionAlertConfig> findByIsGlobal(boolean isGlobal);
}
