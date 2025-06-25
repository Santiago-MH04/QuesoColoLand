package com.quesocololand.msvcattractions.repositories;

import com.quesocololand.msvcattractions.models.VisitorCount;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VisitorCountRepository extends MongoRepository<VisitorCount, String> {
    //Methods of VisitorCountRepository
    public List<VisitorCount> findByAttractionId(String attractionId);
    public List<VisitorCount> findByTimestampBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);
}
