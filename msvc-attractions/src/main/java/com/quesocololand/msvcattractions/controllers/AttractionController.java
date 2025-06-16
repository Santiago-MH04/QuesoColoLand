package com.quesocololand.msvcattractions.controllers;

import com.quesocololand.msvcattractions.exceptions.AttractionNotFoundException;
import com.quesocololand.msvcattractions.models.Attraction;
import com.quesocololand.msvcattractions.models.dto.AttractionDTO;
import com.quesocololand.msvcattractions.services.AttractionService;
import jakarta.validation.Valid;
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
    @ResponseStatus(value = HttpStatus.OK)
    public List<AttractionDTO> findAll(){
        return this.attractionService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttractionDTO> findById(@PathVariable String id){
        return this.attractionService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new AttractionNotFoundException("Attraction not found at our amusement park"));
    }

    @PostMapping
    public ResponseEntity<Attraction> create(@Valid @RequestBody AttractionDTO attractionDTO) {
            //In case it fails validation, it throws a MethodArgumentNotValidException
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.attractionService.create(attractionDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Attraction> update(@Valid @RequestBody AttractionDTO attractionDTO, @PathVariable String id) {
            //In case it fails validation, it throws a MethodArgumentNotValidException
        return ResponseEntity.ok(this.attractionService.update(id, attractionDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if(this.attractionService.findById(id).isPresent()){
            this.attractionService.deleteById(id);
            return ResponseEntity.noContent().build(); // Returns 204 No Content
        }
        throw new AttractionNotFoundException("Attraction not found at our amusement park. No deletion possible");
    }
}
