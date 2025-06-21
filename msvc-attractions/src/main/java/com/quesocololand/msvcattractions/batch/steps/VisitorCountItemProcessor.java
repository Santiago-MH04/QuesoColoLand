package com.quesocololand.msvcattractions.batch.steps;

import com.quesocololand.msvcattractions.models.VisitorCount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class VisitorCountItemProcessor implements ItemProcessor<VisitorCount, VisitorCount> {
    //Fields of VisitorCountItemProcessor
    //Constructors of VisitorCountItemProcessor
    //Field setters of VisitorCountItemProcessor (setters)
    //Field getters of VisitorCountItemProcessor (getters)
        //Methods of VisitorCountItemProcessor
    @Override
    public VisitorCount process(VisitorCount item) throws Exception {
        item.setRegisteredAt(LocalDateTime.now());
        return item;
    }
}
