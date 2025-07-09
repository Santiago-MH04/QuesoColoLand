package com.quesocololand.msvcattractions.services.implementations;

import com.quesocololand.msvcattractions.models.dto.GroupedVisitorCountDTO;
import com.quesocololand.msvcattractions.repositories.VisitorCountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisitorCountServiceImplTest {
    //Fields of VisitorCountServiceImplTest
    @Mock
    private VisitorCountRepository visitorCountRepo; // Repository mock
    @Mock
    private MongoTemplate mongoTemplate; // MongoTemplate mock
    @InjectMocks
    private VisitorCountServiceImpl visitorCountService; // Real service instance with mocks injected

    //Parameterised setting of VisitorCountServiceImplTest
    @BeforeEach
    void setUp() {
        // Resets los mocks before each test to ensure independence
        reset(this.visitorCountRepo, this.mongoTemplate);
    }

    //Field setters of VisitorCountServiceImplTest (setters)
    //Field getters of VisitorCountServiceImplTest (getters)
    //Methods of VisitorCountServiceImplTest

    @Test
    void getGroupedVisitorCounts_shouldReturnGroupedDataForValidInterval() {
        // 1. Arrange
        String attractionId = "attraction123";
        LocalDate date = LocalDate.of(2025, 7, 2);
        int intervalMinutes = 15;

        // Example data to be returned by the Mongo pipeline
        LocalDateTime interval1Start = LocalDateTime.of(2025, 7, 2, 10, 0, 0);
        LocalDateTime interval2Start = LocalDateTime.of(2025, 7, 2, 10, 15, 0);

        List<GroupedVisitorCountDTO> expectedResults = Arrays.asList(
            GroupedVisitorCountDTO.builder()
                .attractionId(attractionId)
                .intervalStart(interval1Start)
                .attendance(50)
                .build(),
            GroupedVisitorCountDTO.builder()
                .attractionId(attractionId)
                .intervalStart(interval2Start)
                .attendance(30)
                .build()
        );

        // Mock the behaviour of mongoTemplate.aggregate()
        // A Mock of AggregationResults is needed to return the expected results
        AggregationResults<GroupedVisitorCountDTO> mockAggregationResults = mock(AggregationResults.class);
        when(mockAggregationResults.getMappedResults()).thenReturn(expectedResults);

        when(this.mongoTemplate.aggregate(
            any(Aggregation.class), // This will capture the Aggregation object
            eq("visitor_counts"),   // This will verify the name of the collection
            eq(GroupedVisitorCountDTO.class) // This will verify the mapping class
        )).thenReturn(mockAggregationResults);

        // 2. Act
        List<GroupedVisitorCountDTO> actualResults = this.visitorCountService.getGroupedVisitorCounts(
                attractionId, date, intervalMinutes
        );

        // 3. Assert
        assertEquals(actualResults, expectedResults);

        // Verify that mongoTemplate.aggregate() was invoked once
        verify(this.mongoTemplate, times(1)).aggregate(
            any(Aggregation.class),
            eq("visitor_counts"),
            eq(GroupedVisitorCountDTO.class)
        );

        // Opcional: Verificar los detalles del pipeline de agregación construido
        // Esto es más avanzado y puede ser frágil si el pipeline cambia.
        // ArgumentCaptor<Aggregation> aggregationCaptor = ArgumentCaptor.forClass(Aggregation.class);
        // verify(mongoTemplate).aggregate(aggregationCaptor.capture(), eq("visitor_counts"), eq(GroupedVisitorCountDTO.class));
        // Aggregation capturedAggregation = aggregationCaptor.getValue();
        // assertThat(capturedAggregation.getPipeline()).hasSize(4); // Match, Project, Group, Sort, Project
        // Puedes inspeccionar cada Operation si quieres una verificación más profunda.
    }
}