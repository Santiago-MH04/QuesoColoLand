package com.quesocololand.msvc_attractions.models;

import com.quesocololand.msvc_attractions.models.utils.AttractionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AttractionDTO {
        //Fields of AttractionDTO
    private String id;
    private String name;
    private AttractionStatus status;
    private int capacity;
    private LocalDateTime lastUpdate;

    //Constructors of AttractionDTO
    //Setters of AttractionDTO
    //Getters of AttractionDTO
    //Methods de AttractionDTO
}
