package com.quesocololand.msvcattractions.services.implementations;

import com.quesocololand.msvcattractions.models.VisitorCount;
import com.quesocololand.msvcattractions.repositories.VisitorCountRepository;
import com.quesocololand.msvcattractions.services.abstractions.VisitorCountService;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class VisitorCountServiceImpl implements VisitorCountService {
        //Fields of VisitorCountServiceImpl
    private final VisitorCountRepository visitorCountRepo;

        //Constructors of VisitorCountServiceImpl
    public VisitorCountServiceImpl(VisitorCountRepository visitorCountRepo) {
        this.visitorCountRepo = visitorCountRepo;
    }

    //Field setters of VisitorCountServiceImpl (setters)
    //Field getters of VisitorCountServiceImpl (getters)
        //Methods of VisitorCountServiceImpl
    @Override
    public List<VisitorCount> findByAttractionId(String attractionId) {
        return this.visitorCountRepo.findByAttractionId(attractionId);
    }

    @Override
    public List<VisitorCount> findByTimestampOn(LocalDate date) {
        ZoneId zone = ZoneId.of("America/Bogota");

        Instant startOfDay = date.atStartOfDay(zone).toInstant();
        Instant endOfDay = date.plusDays(1).atStartOfDay(zone).toInstant().minus(1, ChronoUnit.NANOS);

        return this.visitorCountRepo.findByTimestampBetween(startOfDay, endOfDay);
    }

    @Override
    public void saveAll(List<VisitorCount> visitorCountList) {
        this.visitorCountRepo.saveAll(visitorCountList);
    }
}
