package com.quesocololand.msvcattractions.batch.steps;

import com.quesocololand.msvcattractions.batch.VisitorCountItemReader;
import com.quesocololand.msvcattractions.models.VisitorCount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DefaultFieldSet;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class VisitorCountItemReaderTest {
        //Fields of VisitorCountItemReaderTest
    @Mock
    private LineMapper<VisitorCount> lineMapperMock;
    @InjectMocks
    private VisitorCountItemReader reader;
    private String filePath = "src/test/resources/data/test-visitors.csv"; //Test file route

    //Constructors of VisitorCountItemReaderTest
    //Field setters of VisitorCountItemReaderTest (setters)
    //Field getters of VisitorCountItemReaderTest (getters)
        //Methods of VisitorCountItemReaderTest
    @BeforeEach
    void setUp() {
            //Reader initialiser with a filePath value
        reader = new VisitorCountItemReader(filePath);
    }

    @Test
    void testLineMapper_ShouldNotBeNull() {
            //Verify that the LineMapper won’t be null
        assertNotNull(reader.getLineMapper());
    }

    @Test
    void testGetLineMapper_ShouldReturnValidLineMapper() {
            //Verify that the LineMapper returned is a DefaultLineMapper
        LineMapper<VisitorCount> lineMapper = reader.getLineMapper();
        assertInstanceOf(DefaultLineMapper.class, lineMapper);
    }

    @Test
    public void testProcessLine_ShouldMapLineToVisitorCount() throws Exception {
            // Mocking that the lineMapper can map a line
        String line = "1,100,2023-06-24T10:00:00";
        VisitorCount expectedVisitorCount = new VisitorCount(
                "vadeopaebirnbwiroir9",
                "685048a168ff3f8035c3316a",
                100,
                LocalDateTime.parse("2023-06-24T10:00:00"),
                LocalDateTime.parse("2023-06-24T10:00:00")
        );

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
            tokenizer.setDelimiter(",");
            tokenizer.setStrict(true);
            tokenizer.setNames("attractionId", "count", "timestamp");
            tokenizer.setIncludedFields(0, 1, 2);

            //Date parsing logic
        Map<Class, PropertyEditor> customEditors = new HashMap<>();
        customEditors.put(LocalDateTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(LocalDateTime.parse(text, DateTimeFormatter.ISO_DATE_TIME));
            }
        });

        BeanWrapperFieldSetMapper<VisitorCount> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
            fieldSetMapper.setTargetType(VisitorCount.class);
            fieldSetMapper.setCustomEditors(customEditors);

        DefaultLineMapper<VisitorCount> lineMapper = new DefaultLineMapper<>();
            lineMapper.setLineTokenizer(tokenizer);
            lineMapper.setFieldSetMapper(fieldSetMapper);


            // Simular el mapeo de la línea a un FieldSet
        String[] tokens = tokenizer.tokenize(line).getValues();  // Tokenizar la línea
        FieldSet fieldSet = new DefaultFieldSet(tokens);  // Usar DefaultFieldSet

            // Usar mapLine en lugar de mapFieldSet
        VisitorCount result = lineMapper.mapLine(line, 0);

        assertEquals(expectedVisitorCount, result);
    }
}