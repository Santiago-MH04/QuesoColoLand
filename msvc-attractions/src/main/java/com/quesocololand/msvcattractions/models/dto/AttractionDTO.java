package com.quesocololand.msvcattractions.models.dto;

import com.quesocololand.msvcattractions.models.utils.AttractionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AttractionDTO {
        //Atributos de AttractionDTO
    private String id;
    private String name;
    private AttractionStatus status;
    private int capacity;
    private LocalDateTime lastUpdate;

    //Constructores de AttractionDTO
    //Asignadores de atributos de AttractionDTO (setters)
    //Lectores de atributos de AttractionDTO (getters)
    //Métodos de AttractionDTO
}
