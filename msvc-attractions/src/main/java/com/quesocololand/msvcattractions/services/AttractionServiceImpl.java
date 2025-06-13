package com.quesocololand.msvcattractions.services;

import com.quesocololand.msvcattractions.exceptions.AttractionPersistenceException;
import com.quesocololand.msvcattractions.models.Attraction;
import com.quesocololand.msvcattractions.models.dto.AttractionDTO;
import com.quesocololand.msvcattractions.models.mapper.AttractionMapper;
import com.quesocololand.msvcattractions.repositories.AttractionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AttractionServiceImpl implements AttractionService{
        //Fields of AttractionServiceImpl
    private final AttractionRepository repoAttraction;

        //Constructors of AttractionServiceImpl
    public AttractionServiceImpl(AttractionRepository repoAttraction) {
        this.repoAttraction = repoAttraction;
    }

        //Methods de AttractionServiceImpl
    @Override
    @Transactional(readOnly = true)
    public List<AttractionDTO> findAll() {
        return this.repoAttraction.findAll().stream()
                .map(AttractionMapper.INSTANCE::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AttractionDTO> findById(String id) {
        return this.repoAttraction.findById(id)
                .map(AttractionMapper.INSTANCE::toDTO);
    }

    @Override
    @Transactional
    public Attraction create(AttractionDTO attractionDTO) {
        return this.repoAttraction.save(AttractionMapper.INSTANCE.toAttraction(attractionDTO));
    }

    @Override
    @Transactional
    public Attraction update(String id, AttractionDTO attractionDTO) throws AttractionPersistenceException{
        if(this.repoAttraction.findById(id).isPresent()){
            return this.repoAttraction.save(AttractionMapper.INSTANCE.toAttraction(attractionDTO));
        }
        throw new AttractionPersistenceException("persistence error in attraction creation");
    }

    @Override
    @Transactional
    public void delete(String id) {
        this.repoAttraction.deleteById(id);
    }

    /*private static AttractionDTO buildAttractionDTO(Attraction a) {
        return AttractionDTO.builder()
                .id(a.getId())
                .name(a.getName())
                .status(a.getStatus())
                .capacity(a.getCapacity())
                .build();
    }*/

    /*private static Attraction buildAttraction(AttractionDTO attractionDTO) {
        return Attraction.builder()
                .name(attractionDTO.getName())
                .status(attractionDTO.getStatus())
                    *//*.status(AttractionStatus.valueOf(String.valueOf(attractionDTO.getStatus())))*//*
                .capacity(attractionDTO.getCapacity())
                .build();
    }*/
}
