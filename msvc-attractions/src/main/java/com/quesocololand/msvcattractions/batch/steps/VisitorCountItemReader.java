package com.quesocololand.msvcattractions.batch.steps;

import com.quesocololand.msvcattractions.models.VisitorCount;

import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.nio.charset.StandardCharsets;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class VisitorCountItemReader extends FlatFileItemReader<VisitorCount> {
        //Fields of VisitorCountItemReader
    /*@Value("#{jobParameters['filePath']}")
    private String filePath;*/
    private static final String RESOURCE_PATH = "data/visitors.csv";

        //Constructors of VisitorCountItemReader
    public VisitorCountItemReader() {
        setName("readVisitorCounts");
        setResource(new ClassPathResource(/*filePath*/ RESOURCE_PATH)); //It points directly to resources folder
        setLinesToSkip(1);
        setEncoding(StandardCharsets.UTF_8.name());
        setLineMapper(getLineMapper());
    }

    //Field setters of VisitorCountItemReader (setters)
    //Field getters of VisitorCountItemReader (getters)
        //Methods of VisitorCountItemReader
    public LineMapper<VisitorCount> getLineMapper(){
            //How to read each line
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
            lineTokenizer.setDelimiter(",");
            lineTokenizer.setStrict(true);  //It forces each line to have exactly 3 fields, or it will throw an exception
            lineTokenizer.setNames("attractionId", "count", "timestamp");
            lineTokenizer.setIncludedFields(0, 1, 2);   //It tells the tokeniser to have into account only the three first fields. A better way to handle possible exceptions

            //Date parsing logic
        Map<Class, PropertyEditor> customEditors = new HashMap<>();
        customEditors.put(LocalDateTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(LocalDateTime.parse(text, DateTimeFormatter.ISO_DATE_TIME));
            }
        });

            //What type of object to set to each line
        BeanWrapperFieldSetMapper<VisitorCount> fieldSetMapper = new BeanWrapperFieldSetMapper<>(); //It means that each line will be parsed into a VisitorCount object
            fieldSetMapper.setTargetType(VisitorCount.class);
            fieldSetMapper.setCustomEditors(customEditors);

            //What instance is in charge to make the mapping to Spring Batch
        DefaultLineMapper<VisitorCount> lineMapper = new DefaultLineMapper<>();
            lineMapper.setLineTokenizer(lineTokenizer);
            lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }
}
