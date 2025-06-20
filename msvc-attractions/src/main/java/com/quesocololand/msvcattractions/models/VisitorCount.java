package com.quesocololand.msvcattractions.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

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
    private Instant timestamp;

    //Constructors of VisitorCount
    //Field setters of VisitorCount (setters)
    //Field getters of VisitorCount (getters)
    //Methods of VisitorCount
}
