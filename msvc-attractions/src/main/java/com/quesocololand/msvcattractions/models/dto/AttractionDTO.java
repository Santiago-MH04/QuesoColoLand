package com.quesocololand.msvcattractions.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AttractionDTO {
        //Fields of AttractionDTO
    private String _id;
    private String name;
    private String status;
    private int capacity;

    //Constructors of AttractionDTO
    //Field setters of AttractionDTO (setters)
    //Field getters of AttractionDTO (getters)
    //Methods of AttractionDTO
}
