package com.quesocololand.msvcattractions.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.quesocololand.msvcattractions.models.utils.AttractionStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDateTime;

@Document(collection = "attractions")   //Pending of creation
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Attraction {
        //Fields of Attraction
    @Id
    /*@Field(name="_id")*/
    private String _id;

    @NotBlank(message = "this attraction must have a name, with 3 characters as minimum")
    @Size(min = 3)
    private String name;

    @NotNull(message = "the status must be either ACTIVE, INACTIVE, MAINTENANCE")
    private AttractionStatus status;

    @Min(value = 10, message = "each attraction must be capable to handle minimum 10 people")
    private int capacity;

    @Field(name = "last_update", targetType = FieldType.DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime lastUpdate;

    //Constructors of Attraction
    //Field setters of Attraction (setters)
    //Field getters of Attraction (getters)
        //Methods of Attraction
    public void preUpdate(){
            //To make sure it only applies to updates
        if (this._id != null) {
            this.lastUpdate = LocalDateTime.now();
        }
    }
}
