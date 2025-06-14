package com.quesocololand.msvcattractions.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ErrorDTO {
        //Atributos de Error
    private String name;
    private String message;
    private int httpStatus;
    private LocalDateTime timestamp;

    //Constructores de Error
    //Asignadores de atributos de Error (setters)
    //Lectores de atributos de Error (getters)
    //Métodos de Error
}
