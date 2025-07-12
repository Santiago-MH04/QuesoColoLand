package com.quesocololand.msvcattractions.services.abstractions;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.quesocololand.msvcattractions.models.VisitorCount;
import com.quesocololand.msvcattractions.models.dto.GroupedVisitorCountDTO;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface VisitorCountService {
    //Fields of VisitorCountService
    //Constructors of VisitorCountService
    //Field setters of VisitorCountService (setters)
    //Field getters of VisitorCountService (getters)
        //Methods of VisitorCountService
    public List<VisitorCount> findByAttractionId(String attractionId);
    public List<VisitorCount> findByTimestampOn(LocalDate date);
    public VisitorCount save(VisitorCount visitorCount);
    public void saveAll(List<VisitorCount> visitorCountList);   //In order to avoid insertion problems with the Chunk
    public List<GroupedVisitorCountDTO> getGroupedVisitorCounts(String attractionId, LocalDate date, int intervalMinutes);
    public String getCsvFile(String attractionId, LocalDate date, int intervalMinutes) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException;
}
