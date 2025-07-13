package com.quesocololand.msvcattractions.services.implementations;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.quesocololand.msvcattractions.models.dto.GroupedVisitorCountDTO;
import com.quesocololand.msvcattractions.repositories.VisitorCountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisitorCountServiceImplTest {
    //Fields of VisitorCountServiceImplTest
    @Mock
    private VisitorCountRepository visitorCountRepo;
    @InjectMocks
    private VisitorCountServiceImpl visitorCountService;

    //Methods of VisitorCountServiceImplTest
    @Test
    void testGetGroupedVisitorCounts() {
        // 1. Arrange
        String attractionId = "attraction_1";
        LocalDate date = LocalDate.of(2025, 7, 2);
        int intervalMinutes = 15;

        // Example data to be returned by the Mongo pipeline
        LocalDateTime interval1Start = LocalDateTime.of(2025, 7, 2, 10, 0, 0);
        LocalDateTime interval2Start = LocalDateTime.of(2025, 7, 2, 10, 15, 0);

        List<GroupedVisitorCountDTO> mockResult = Arrays.asList(
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

        // Mock the behaviour of the repository
        when(this.visitorCountRepo.getGroupedVisitorCounts(
            eq(attractionId),
            eq(LocalDateTime.of(date, LocalTime.of(0, 0))), // startOfDay
            eq(LocalDateTime.of(date, LocalTime.MAX)),       // endOfDay
            eq(intervalMinutes)
        )).thenReturn(mockResult);

        //2. Act
        List<GroupedVisitorCountDTO> result = this.visitorCountService.getGroupedVisitorCounts(attractionId, date, intervalMinutes);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(attractionId, result.getFirst().getAttractionId());
        assertEquals(50, result.getFirst().getAttendance());
        assertEquals(LocalDateTime.of(date, LocalTime.of(10, 0)), result.getFirst().getIntervalStart());

        // Verify
        verify(this.visitorCountRepo, times(1))
        .getGroupedVisitorCounts(
            eq(attractionId),
            eq(LocalDateTime.of(date, LocalTime.of(0, 0))),
            eq(LocalDateTime.of(date, LocalTime.MAX)),
            eq(intervalMinutes)
        );
    }

    @Test
    void generateCsv_shouldReturnCsvStringWithHeadersAndData() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        // 1. Arrange
        String attractionId = "testAttractionId";
        String attractionName = "testAttractionName";
        LocalDateTime interval1Start = LocalDateTime.of(2025, 7, 12, 9, 0, 0);
        LocalDateTime interval2Start = LocalDateTime.of(2025, 7, 12, 9, 15, 0);

        List<GroupedVisitorCountDTO> data = Arrays.asList(
            GroupedVisitorCountDTO.builder()
                .attractionId(attractionId)
                .attractionName(attractionName)
                .intervalStart(interval1Start)
                .attendance(100)
                .build(),
            GroupedVisitorCountDTO.builder()
                .attractionId(attractionId)
                .attractionName(attractionName)
                .intervalStart(interval2Start)
                .attendance(150)
                .build()
        );

        // 2. Act
        String csvContent = this.visitorCountService.generateCsv(data);

        // 3. Assert
        String expectedCsv = "ATTENDANCE,ATTRACTION ID,ATTRACTION NAME,INTERVAL START\n" +
                "100,testAttractionId,testAttractionName,2025-07-12T09:00:00\n" +
                "150,testAttractionId,testAttractionName,2025-07-12T09:15:00\n";

        assertEquals(expectedCsv, csvContent);
    }

    @Test
    void generateCsv_shouldReturnOnlyHeadersWhenNoData() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        // 1. Arrange
        List<GroupedVisitorCountDTO> emptyData = Collections.emptyList();

        // 2. Act
        String csvContent = this.visitorCountService.generateCsv(emptyData);

        // 3. Assert
        String expectedCsvHeaders = ("Attraction ID,Attraction name,Interval start,Attendance").toUpperCase();
        assertEquals(expectedCsvHeaders, csvContent);
    }
}
