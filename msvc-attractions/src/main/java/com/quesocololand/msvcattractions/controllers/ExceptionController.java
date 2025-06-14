package com.quesocololand.msvcattractions.controllers;

import com.quesocololand.msvcattractions.exceptions.AttractionNotFoundException;
import com.quesocololand.msvcattractions.models.dto.ErrorDTO;
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
    public ResponseEntity<ErrorDTO> attractionNotFound(Exception e){
        ErrorDTO errorDTO = new ErrorDTO();
            errorDTO.setName("Attraction not found");
            errorDTO.setMessage(e.getMessage());
            errorDTO.setHttpStatus(HttpStatus.NOT_FOUND.value());
            errorDTO.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
    }
}
