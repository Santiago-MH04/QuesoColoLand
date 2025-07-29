package com.quesocololand.msvcattractionsalerting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VisitorCountDTO {
    //Fields of VisitorCountDTO
    private String id;
    private String attractionId;
    private int count;
    private LocalDateTime timestamp;
    private LocalDateTime registeredAt;
}
