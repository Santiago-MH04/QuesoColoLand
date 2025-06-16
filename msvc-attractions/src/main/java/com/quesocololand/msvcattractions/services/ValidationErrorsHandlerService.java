package com.quesocololand.msvcattractions.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class ValidationErrorsHandlerService {
    //Fields of ValidationErrorsHandlerService
    //Constructors of ValidationErrorsHandlerService
    //Field setters of ValidationErrorsHandlerService (setters)
    //Field getters of ValidationErrorsHandlerService (getters)
        //Methods of ValidationErrorsHandlerService
    public ResponseEntity<Map<String, String>> handleValidationErrors(BindingResult result) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy 'T' HH:mm:ss");
        String formattedTimestamp = LocalDateTime.now().format(formatter);

        Map<String, String> errors = new HashMap<>();
            errors.put("name", "database saving error");
            errors.put("message", "there were issues saving some fields");

        for (FieldError error : result.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

            errors.put("status", "400");
            errors.put("timestamp", formattedTimestamp);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

}
