package com.quesocololand.msvcattractions.controllers;

import com.quesocololand.msvcattractions.exceptions.AttractionNotFoundException;
import com.quesocololand.msvcattractions.models.dto.AttractionDTO;
import com.quesocololand.msvcattractions.services.AttractionService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attractions")
public class AttractionController {
        //Fields of AttractionController
    private final AttractionService attractionService;

        //Constructors of AttractionController
    public AttractionController(AttractionService attractionService) {
        this.attractionService = attractionService;
    }

    //Field setters of AttractionController (setters)
    //Field getters of AttractionController (getters)
        //Methods of AttractionController
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AttractionDTO> findAll(){
        return this.attractionService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttractionDTO> findById(@PathVariable String id){
        return this.attractionService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new AttractionNotFoundException("Attraction not found at our amusement park"));
    }
}
