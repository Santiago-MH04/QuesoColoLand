package com.quesocololand.msvcattractions.controllers;

import com.quesocololand.msvcattractions.exceptions.AttractionNotFoundException;
import com.quesocololand.msvcattractions.models.Attraction;
import com.quesocololand.msvcattractions.models.dto.AttractionDTO;
import com.quesocololand.msvcattractions.services.abstractions.AttractionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attractions")
@Tag(
    name = "attractions",
    description = "attractions controller"
)
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
    @Operation(
        summary = "attractions listing endpoint",
        description = "lists all the attractions in QuesoColoLand",
        tags = {"list", "all"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "successful attraction listing",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = List.class)
                )
            )
        }
    )
    public List<AttractionDTO> findAll(){
        return this.attractionService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "attraction showing endpoint",
        description = "lists one attraction in QuesoColoLand, under the id submitted",
        tags = {"show", "id"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "successful attraction search",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AttractionDTO.class)
                )
            )
        }
    )
    public ResponseEntity<AttractionDTO> findById(@PathVariable String id){
        return this.attractionService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new AttractionNotFoundException("Attraction not found at our amusement park"));
    }

    @PostMapping
    @Operation(
        summary = "attraction creating endpoint",
        description = "creates and persists one attraction",
        tags = {"create", "attraction"},
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "uses an attractionDTO object to create an attraction",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AttractionDTO.class)
            )
        ),
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "successful attraction creation",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = Attraction.class)
                )
            )
        }
    )
    public ResponseEntity<Attraction> create(@Valid @RequestBody AttractionDTO attractionDTO) {
            //In case it fails validation, it throws a MethodArgumentNotValidException
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.attractionService.create(attractionDTO));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "attraction updating endpoint",
            description = "persists one existing attraction using the data submitted to update it",
            tags = {"update", "attraction"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "uses an attractionDTO object to update an attraction",
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AttractionDTO.class)
                )
            ),
            responses = {
                @ApiResponse(
                    responseCode = "200",
                    description = "successful attraction update",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Attraction.class)
                    )
                )
            }
    )
    public ResponseEntity<Attraction> update(@Valid @RequestBody AttractionDTO attractionDTO, @PathVariable String id) {
            //In case it fails validation, it throws a MethodArgumentNotValidException
        return ResponseEntity.ok(this.attractionService.update(id, attractionDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "attraction deleting endpoint",
        description = "deletes one attraction, searching it for its id",
        tags = {"delete", "id", "attraction"},
        responses = {
            @ApiResponse(
                responseCode = "204",
                description = "successful attraction deletion",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Void.class)
                )
            )
        }
    )
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if(this.attractionService.findById(id).isPresent()){
            this.attractionService.deleteById(id);
            return ResponseEntity.noContent().build(); // Returns 204 No Content
        }
        throw new AttractionNotFoundException("Attraction not found at our amusement park. No deletion possible");
    }
}
