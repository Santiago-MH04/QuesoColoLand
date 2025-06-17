package com.quesocololand.msvcattractions.models.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AttractionDTO {
        //Fields of AttractionDTO
    private String _id;
    @NotBlank(message = "this attraction must have a name, with 3 characters as minimum")
    @Size(min = 3)
    private String name;
    @NotNull(message = "the status must be either ACTIVE, INACTIVE, MAINTENANCE")
    private String status;
    @Min(value = 10, message = "each attraction must be capable to handle minimum 10 people")
    private int capacity;
    private LocalDateTime lastUpdate;

    //Constructors of AttractionDTO
    //Field setters of AttractionDTO (setters)
    //Field getters of AttractionDTO (getters)
    //Methods of AttractionDTO
}
