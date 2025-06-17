package com.quesocololand.msvcattractions.controllers;

import com.quesocololand.msvcattractions.exceptions.AttractionNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionController {
    //Fields of ExceptionController
    //Constructors of ExceptionController
    //Field setters of ExceptionController (setters)
    //Field getters of ExceptionController (getters)
        //Methods of ExceptionController
    @ExceptionHandler({AttractionNotFoundException.class})
    public ResponseEntity<com.quesocololand.msvcattractions.models.dto.Error> notFoundException(Exception e){
        com.quesocololand.msvcattractions.models.dto.Error error = new com.quesocololand.msvcattractions.models.dto.Error();
            error.setName("Attraction searching error");
            error.setMessage(e.getMessage());
            error.setHttpStatus(HttpStatus.NOT_FOUND.value());
            error.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException e) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy 'T' HH:mm:ss");
        String formattedTimestamp = LocalDateTime.now().format(formatter);

        Map<String, String> errors = new HashMap<>();
        errors.put("name", "database saving error");
        errors.put("general message", "validation failed for one or more fields");

        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        errors.put("status", "400");
        errors.put("timestamp", formattedTimestamp);

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
            /*return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);*/
    }
}
