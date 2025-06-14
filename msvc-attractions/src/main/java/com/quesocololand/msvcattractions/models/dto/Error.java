package com.quesocololand.msvcattractions.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Error {
        //Fields of Error
    private String name;
    private String message;
    private int httpStatus;
    private LocalDateTime timestamp;

    //Constructors of Error
    //Field setters of Error (setters)
    //Field getters of Error (getters)
    //Methods of Error
}
