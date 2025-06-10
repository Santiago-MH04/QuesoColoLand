package com.quesocololand.msvc_attractions.repositories;

import com.quesocololand.msvc_attractions.models.Attraction;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AttractionRepository extends MongoRepository<Attraction, String> {
    //Atributos de AttractionRepository
    //Constructores de AttractionRepository
    //Asignadores de atributos de AttractionRepository (setters)
    //Lectores de atributos de AttractionRepository (getters)
    //Métodos de AttractionRepository
}
