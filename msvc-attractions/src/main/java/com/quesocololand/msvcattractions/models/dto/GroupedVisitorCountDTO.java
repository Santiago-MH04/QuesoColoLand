package com.quesocololand.msvcattractions.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupedVisitorCountDTO {
    //Fields of GroupedVisitorCountDTO
    @CsvBindByName(column = "Attraction ID  ") // Column header in the .csv file
    private String attractionId;         // To know to what attraction it belongs the grouped counting

    @JsonIgnore //For this field not to be displayed when invoking the aggregations
    @CsvBindByName(column = "Attraction name") // Column header in the .csv file
    private String attractionName;         // To know to what attraction it belongs the grouped counting

    @CsvBindByName(column = "Interval start")   // Column header in the .csv file
    @CsvDate(value = "yyyy-MM-dd'T'HH:mm:ss")   // Date/time format in the .csv file
    private LocalDateTime intervalStart; // Time-lapse start time

    @CsvBindByName(column = "Attendance")   // Column header in the .csv file
    private int attendance;           // Visitors sum at that interval

    //Constructors of GroupedVisitorCountDTO
    //Field setters of GroupedVisitorCountDTO (setters)
    //Field getters of GroupedVisitorCountDTO (getters)
    //Methods of GroupedVisitorCountDTO
}
