package com.quesocololand.msvcattractionsalerting.repositories;

import com.quesocololand.msvcattractionsalerting.models.VisitorCount;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface VisitorCountRepository extends MongoRepository<VisitorCount, String> {
    @Query(value = "{}", fields = "{ 'attractionId' : 1 }")
    List<VisitorCount> findAllAttractionIdsOnly();

    @Aggregation(pipeline = {
        "{ '$match': { " +
                "'attractionId': ?0, " +
                "'timestamp': { '$gte': ?1, '$lt': ?2 } " +
            "} " +
        "}",
        "{ '$group': { " +
                "'_id': null, " +
                "'totalCountInPeriod': { '$sum': '$count' } " +
            "} " +
        "}",
        "{ '$project': { " +
                "'_id': 0, " +
                "'totalCount': '$totalCountInPeriod', " +
                "'attractionId': ?0, " +
                "'timestamp': ?2, " +
                "'intervalMinutes': ?3 " +
            "} " +
        "}"
    })
    List<Map> aggregateVisitorCount(String attractionId, LocalDateTime startTime, LocalDateTime endTime, int periodMins);
}
