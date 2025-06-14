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
        //Fields of Attraction
    @Id
    @Field(name="_id")
    private String id;
    private String name;
    private AttractionStatus status;
    private int capacity;
    @Field(name="last_update")
    private LocalDateTime lastUpdate;

    //Constructors of Attraction
    //Field setters of Attraction (setters)
    //Field getters of Attraction (getters)
        //Methods of Attraction
    public void preUpdate(){
            //To make sure it only applies to updates
        if (this.id != null) {
            this.lastUpdate = LocalDateTime.now();
        }
    }
}
