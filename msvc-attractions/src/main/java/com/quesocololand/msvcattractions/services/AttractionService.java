package com.quesocololand.msvcattractions.services;

import com.quesocololand.msvcattractions.models.Attraction;
import com.quesocololand.msvcattractions.models.dto.AttractionDTO;

import java.util.List;
import java.util.Optional;

public interface AttractionService {
    //Atributos de CLASE
    //Constructores de CLASE
    //Asignadores de atributos de CLASE (setters)
    //Lectores de atributos de CLASE (getters)
        //Métodos de CLASE
    public List<AttractionDTO> findAll();
    public Optional<AttractionDTO> findById(String id);
    public Attraction create(AttractionDTO attractionDTO);
    public Attraction update(String id, AttractionDTO attractionDTO);
    public void delete(String id);
}
