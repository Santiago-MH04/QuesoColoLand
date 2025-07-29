package com.quesocololand.msvcattractions.repositories;

import com.quesocololand.msvcattractions.models.VisitorCount;
import com.quesocololand.msvcattractions.models.dto.GroupedVisitorCountDTO;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VisitorCountRepository extends MongoRepository<VisitorCount, String> {
    //Methods of VisitorCountRepository
    public List<VisitorCount> findByAttractionId(String attractionId);
    public List<VisitorCount> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    public List<VisitorCount> findByAttractionIdAndTimestampBetween(String attractionId, LocalDateTime start, LocalDateTime end);

    @Aggregation(pipeline = {
        "{ '$match': { " +
                "'attractionId': ?0, " +
                "'timestamp': { '$gte': ?1, '$lte': ?2 } " +
            "} " +
        "}",
        "{ '$project': { " +
                "'timestamp': 1, " +
                "'intervalStart': { " +
                    "'$dateTrunc': { " +
                        "'date': '$timestamp', " +
                        "'unit': 'minute', " +
                        "'binSize': ?3, " +
                        "'timezone': 'America/Bogota' " +
                "   } " +
                "} " +
                "'attractionId': 1, " +
                "'count': 1, " +
            "} " +
        "}",
        "{ '$group': { " +
                "'_id': { " +
                "'attractionId': '$attractionId', " +
                "'intervalStart': '$intervalStart' " +
            "}, " +
            "'totalVisitors': { '$sum': '$count' }, " +
            "'attractionId': { '$first': '$attractionId' } } " +
        "}",
        "{ '$sort': { '_id.intervalStart': 1 } }",
        "{ '$project': { " +
                "'_id': 0 " +
                "'attractionId': '$_id.attractionId', " +
                "'intervalStart': '$_id.intervalStart', " +
                "'attendance': '$totalVisitors', " +
            "} " +
        "}"
    })
    List<GroupedVisitorCountDTO> getGroupedVisitorCounts(String attractionId, LocalDateTime startOfDay, LocalDateTime endOfDay, int intervalMinutes);
}
