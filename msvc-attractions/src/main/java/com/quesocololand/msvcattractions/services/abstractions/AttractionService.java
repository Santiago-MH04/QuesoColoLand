package com.quesocololand.msvcattractions.services.abstractions;

import com.quesocololand.msvcattractions.models.Attraction;
import com.quesocololand.msvcattractions.models.dto.AttractionDTO;

import java.util.List;
import java.util.Optional;

public interface AttractionService {
    //Fields of AttractionService
    //Constructors of AttractionService
    //Field setters of AttractionService (setters)
    //Field getters of AttractionService (getters)
        //Methods de AttractionService
    public List<AttractionDTO> findAll();
    public Optional<AttractionDTO> findById(String id);
    public Attraction create(AttractionDTO attractionDTO);
    public Attraction update(String id, AttractionDTO attractionDTO);
    public void deleteById(String id);
}
