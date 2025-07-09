package com.quesocololand.msvcattractions.services.implementations;

import com.quesocololand.msvcattractions.models.dto.GroupedVisitorCountDTO;
import com.quesocololand.msvcattractions.repositories.VisitorCountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
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
}
