package com.quesocololand.msvcattractions.services.abstractions;

import com.quesocololand.msvcattractions.models.VisitorCount;

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
    public void saveAll(List<VisitorCount> visitorCountList);
}
