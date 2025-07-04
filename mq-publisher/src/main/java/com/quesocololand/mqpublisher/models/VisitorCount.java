package com.quesocololand.mqpublisher.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VisitorCount {
    //Fields of VisitorCount
    private String id;
    private String attractionId;
    private int count;
    private LocalDateTime timestamp;
    private LocalDateTime registeredAt;

    //Constructors of VisitorCount
    //Field setters of VisitorCount (setters)
    //Field getters of VisitorCount (getters)
    //Methods of VisitorCount
}
