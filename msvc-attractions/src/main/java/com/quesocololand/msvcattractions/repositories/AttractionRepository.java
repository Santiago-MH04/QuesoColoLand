package com.quesocololand.msvcattractions.repositories;

import com.quesocololand.msvcattractions.models.Attraction;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AttractionRepository extends MongoRepository<Attraction, String> {
    //Atributos de AttractionRepository
    //Constructores de AttractionRepository
    //Asignadores de atributos de AttractionRepository (setters)
    //Lectores de atributos de AttractionRepository (getters)
    //Métodos de AttractionRepository
}
