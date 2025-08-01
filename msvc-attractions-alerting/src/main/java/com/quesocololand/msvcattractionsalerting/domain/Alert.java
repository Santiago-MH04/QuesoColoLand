package com.quesocololand.msvcattractionsalerting.domain;

import com.quesocololand.msvcattractionsalerting.domain.utils.AlertType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alert {
    //Fields of Alert
    private AlertType alertType;
    private String message;
    private LocalDateTime timestamp;

    //Constructors of Alert
    //Field setters of Alert (setters)
    //Field getters of Alert (getters)
    //Methods of Alert
}
