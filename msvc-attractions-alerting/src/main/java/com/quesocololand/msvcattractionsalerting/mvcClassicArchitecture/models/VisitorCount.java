package com.quesocololand.msvcattractionsalerting.mvcClassicArchitecture.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "visitor_counts")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VisitorCount {
    //Fields of VisitorCount
    @Field(name = "_id")
    private String id;
    private String attractionId;
    private int count;
    private LocalDateTime timestamp;
    @Field(name = "registered_at")
    private LocalDateTime registeredAt;
}
