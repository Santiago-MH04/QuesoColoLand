package com.quesocololand.msvcattractions.controllers;

import com.quesocololand.msvcattractions.exceptions.AttractionNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

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
}
