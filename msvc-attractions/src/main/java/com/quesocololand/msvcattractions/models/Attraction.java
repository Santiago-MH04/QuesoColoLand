package com.quesocololand.msvcattractions.models;

import com.quesocololand.msvcattractions.models.utils.AttractionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "attractions")   //Pending of creation
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Attraction {
        //Atributos de Attraction
    @Id
    @Field(name="_id")
    private String id;
    private String name;
    private AttractionStatus status;
    private int capacity;
    @Field(name="last_update")
    private LocalDateTime lastUpdate;
    
    //Constructores de Attraction
    //Asignadores de atributos de Attraction (setters)
    //Lectores de atributos de Attraction (getters)
        //Métodos de Attraction
    public void preUpdate(){
        this.lastUpdate = LocalDateTime.now();
    }
}
