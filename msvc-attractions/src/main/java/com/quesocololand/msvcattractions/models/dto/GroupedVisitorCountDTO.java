package com.quesocololand.msvcattractions.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupedVisitorCountDTO {
    //Fields of GroupedVisitorCountDTO
    private String attractionId;         // To know to what attraction it belongs the grouped counting
    private int attendance;           // Visitors sum at that interval
    private LocalDateTime intervalStart; // Time-lapse start time

    //Constructors of GroupedVisitorCountDTO
    //Field setters of GroupedVisitorCountDTO (setters)
    //Field getters of GroupedVisitorCountDTO (getters)
    //Methods of GroupedVisitorCountDTO
}
