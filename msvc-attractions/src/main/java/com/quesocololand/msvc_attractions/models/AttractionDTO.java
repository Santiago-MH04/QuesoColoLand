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
    private LocalDateTime lastUpdate;
    private String id;
    private String name;
    private AttractionStatus status;
    private int capacity;

    //Constructors of AttractionDTO
    //Field setters of AttractionDTO (setters)
    //Field getters of AttractionDTO (getters)
    //Methods de AttractionDTO
}
