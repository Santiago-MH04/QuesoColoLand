package com.quesocololand.msvcattractions.batch.steps;

import com.quesocololand.msvcattractions.models.VisitorCount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class VisitorCountItemWriter extends MongoItemWriter<VisitorCount> {
    //Fields of VisitorCountItemWriter
    private static final String COLLECTION_NAME = "visitor_counts";
    private MongoTemplate mongoTemplate;

    //Constructors of VisitorCountItemWriter
    public VisitorCountItemWriter(MongoTemplate mongoTemplate) {
        setCollection(COLLECTION_NAME);
        setTemplate(mongoTemplate);
    }

    //Field setters of VisitorCountItemWriter (setters)
    //Field getters of VisitorCountItemWriter (getters)
    //Methods of VisitorCountItemWriter
}
