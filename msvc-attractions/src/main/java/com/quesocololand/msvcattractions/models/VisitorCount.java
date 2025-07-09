package com.quesocololand.msvcattractions.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.Instant;
import java.time.LocalDateTime;

@Document(collection = "visitor_counts")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VisitorCount {
        //Fields of VisitorCount
    @Id
    private String id;
    private String attractionId;
    private int count;
    @Field(targetType = FieldType.DATE_TIME)
    private LocalDateTime timestamp;
    @Field(name = "registered_at", targetType = FieldType.DATE_TIME)
    private LocalDateTime registeredAt;

    //Constructors of VisitorCount
    //Field setters of VisitorCount (setters)
    //Field getters of VisitorCount (getters)
    //Methods of VisitorCount
}
