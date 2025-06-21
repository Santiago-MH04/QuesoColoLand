package com.quesocololand.msvcattractions.batch.steps;

import com.quesocololand.msvcattractions.models.VisitorCount;
import com.quesocololand.msvcattractions.repositories.VisitorCountRepository;
import com.quesocololand.msvcattractions.services.abstractions.VisitorCountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public class VisitorCountItemWriter extends RepositoryItemWriter<VisitorCount> /*implements ItemWriter<VisitorCount>*/ {
        //Fields of VisitorCountItemWriter
    /*@Autowired
    private VisitorCountService visitorCountService;*/

        //Constructors of VisitorCountItemWriter
    public VisitorCountItemWriter(VisitorCountRepository visitorCountRepo) {
        log.info("Entering the writer");

        setRepository(visitorCountRepo);
        setMethodName("saveAll");
    }

    //Field setters of VisitorCountItemWriter (setters)
    //Field getters of VisitorCountItemWriter (getters)
        //Methods of VisitorCountItemWriter
    /*@Override
    public void write(Chunk<? extends VisitorCount> chunk) throws Exception {
        log.info("Entering the writer");
        chunk.forEach(System.out::println);
        this.visitorCountService.saveAll((List<VisitorCount>) chunk);
    }*/
}
