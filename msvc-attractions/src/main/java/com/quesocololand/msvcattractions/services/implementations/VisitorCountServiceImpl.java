package com.quesocololand.msvcattractions.services.implementations;

import com.quesocololand.msvcattractions.models.VisitorCount;
import com.quesocololand.msvcattractions.models.dto.GroupedVisitorCountDTO;
import com.quesocololand.msvcattractions.repositories.VisitorCountRepository;
import com.quesocololand.msvcattractions.services.abstractions.VisitorCountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VisitorCountServiceImpl implements VisitorCountService {
    //Fields of VisitorCountServiceImpl
    private final VisitorCountRepository visitorCountRepo;

    //Methods of VisitorCountServiceImpl
    @Override
    public List<VisitorCount> findByAttractionId(String attractionId) {
        return this.visitorCountRepo.findByAttractionId(attractionId);
    }

    @Override
    public List<VisitorCount> findByTimestampOn(LocalDate date) {

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        return this.visitorCountRepo.findByTimestampBetween(startOfDay, endOfDay);
    }

    @Override
    public VisitorCount save(VisitorCount visitorCount) {
        return this.visitorCountRepo.save(visitorCount);
    }

    @Override
    public void saveAll(List<VisitorCount> visitorCountList) {
        this.visitorCountRepo.saveAll(visitorCountList);
    }

    @Override
    public List<GroupedVisitorCountDTO> getGroupedVisitorCounts(String attractionId, LocalDate date, int intervalMinutes) {
        // Define the date range for the specific day (from 00:00:00 to 23:59:59)
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX); // This yields 23:59:59.999999999

        return this.visitorCountRepo.getGroupedVisitorCounts(attractionId, startOfDay, endOfDay, intervalMinutes);
    }
}
