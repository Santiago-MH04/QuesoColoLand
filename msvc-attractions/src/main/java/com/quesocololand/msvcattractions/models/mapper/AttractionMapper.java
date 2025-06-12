package com.quesocololand.msvcattractions.models.mapper;

import com.quesocololand.msvcattractions.models.Attraction;
import com.quesocololand.msvcattractions.models.dto.AttractionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AttractionMapper {
        //Implementations of AttractionAndDTOMapper
    AttractionMapper INSTANCE = Mappers.getMapper(AttractionMapper.class);

    //Constructors of AttractionAndDTOMapper
    //Field setters of AttractionAndDTOMapper (setters)
    //Field getters of AttractionAndDTOMapper (getters)
        //Methods of AttractionAndDTOMapper
    /*@Mapping(source = "entityFieldName", target = "dtoFieldName")*/   //Just to show how to use when similiar fields are called differently
    Attraction toAttraction(AttractionDTO attractionDTO);
    AttractionDTO toDTO(Attraction attraction);
}
