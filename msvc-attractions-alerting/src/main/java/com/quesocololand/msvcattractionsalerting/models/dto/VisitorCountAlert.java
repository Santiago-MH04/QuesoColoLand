package com.quesocololand.msvcattractionsalerting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisitorCountAlert {
    //Fields of VisitorCountAlert
    public enum AlertType {
        CONGESTION,
        LOW_OCCUPANCY
    }

    private String attractionId;
    private LocalDateTime timestamp;
    private int totalCount;
    private int intervalMinutes;
    private AlertType alertType;
    private String message;             // Mensaje descriptivo de la alerta (ej. "Atracción X está congestionada")

    //Constructors of VisitorCountAlert
    //Field setters of VisitorCountAlert (setters)
    //Field getters of VisitorCountAlert (getters)
    //Methods of VisitorCountAlert
}
