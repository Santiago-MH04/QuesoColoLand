package com.quesocololand.msvcattractions.messaging;

import com.quesocololand.msvcattractions.models.VisitorCount;
import com.quesocololand.msvcattractions.services.abstractions.VisitorCountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisitorCountConsumerTest {
    //Fields of VisitorCountConsumerTest
    @Mock
    private VisitorCountService visitorCountService;

    @InjectMocks // Injects the mocked service into the consumer
    private VisitorCountConsumer visitorCountConsumer;

    // You can use a logger to capture log messages,
    // though direct sysout checks are also possible for simple cases.
    // private final Logger logger = LoggerFactory.getLogger(VisitorCountConsumer.class);

    //Configurations of VisitorCountConsumerTest
    @BeforeEach
    void setUp() {
        // Reset mocks before each test to ensure test independence
        reset(this.visitorCountService);
    }

    //Methods of VisitorCountConsumerTest
    @Test
    void receiveVisitorCountMessage_shouldSaveVisitorCountOnSuccess() {
        // 1. Arrange
        String testAttractionId = "attractionXYZ";
        int testCount = 10;
        LocalDateTime testRegisteredAt = LocalDateTime.now();
        String savedId = "dbId123";

        // Create an incoming message
        VisitorCount incomingMessage = VisitorCount.builder()
            .attractionId(testAttractionId)
            .count(testCount)
            .registeredAt(testRegisteredAt)
            .build();

        // Create the expected saved entity (with an ID from the DB)
        VisitorCount savedVisitorCount = VisitorCount.builder()
            .id(savedId)
            .attractionId(testAttractionId)
            .count(testCount)
            .registeredAt(testRegisteredAt)
            .build();

        // Mock the behaviour of visitorCountService.save()
        // When save() is called with any VisitorCount object, return the savedVisitorCount
        when(this.visitorCountService.save(any(VisitorCount.class))).thenReturn(savedVisitorCount);

        // 2. Act
        this.visitorCountConsumer.receiveVisitorCountMessage(incomingMessage);

        // 3. Assert
        // Verify that visitorCountService.save() was called exactly once with the incoming message
        verify(this.visitorCountService, times(1))
        .save(incomingMessage);

        // You could also assert on logging if you have a way to capture it (e.g., using Logback's ListAppender or similar)
        // For simple console output (System.out.println / System.err.println or direct log.info/error),
        // you'd typically run the test and visually inspect console output during development,
        // or use specific logging test libraries for automated assertion on logs.
        // For this example, we're primarily verifying method calls.
    }

    @Test
    void receiveVisitorCountMessage_shouldHandleServiceException() {
        // 1. Arrange
        String testAttractionId = "attractionABC";
        int testCount = 5;
        LocalDateTime testRegisteredAt = LocalDateTime.now();

        VisitorCount incomingMessage = VisitorCount.builder()
            .attractionId(testAttractionId)
            .count(testCount)
            .registeredAt(testRegisteredAt)
            .build();

        // Mock the behaviour of visitorCountService.save() to throw an exception
        when(this.visitorCountService.save(any(VisitorCount.class)))
            .thenThrow(new RuntimeException("Database connection failed for test"));

        // 2. Act
        this.visitorCountConsumer.receiveVisitorCountMessage(incomingMessage);

        // 3. Assert
        // Verify that visitorCountService.save() was still called once (it threw the exception then)
        verify(visitorCountService, times(1))
            .save(incomingMessage);

        // Verify that no other methods on the service were called after the exception (e.g., if there were more calls)
        verifyNoMoreInteractions(visitorCountService);

        // Here, you might want to assert on the log message being printed.
        // This is more advanced and requires capturing logging output.
        // For example, if you use Logback, you could add a ListAppender to capture log events.
        // For simplicity in a basic unit test, we're primarily checking the flow of control
        // (i.e., that the exception was caught and didn't crash the test, and the service was called).
    }
}