package com.quesocololand.msvcattractionsalerting.services;

import com.quesocololand.msvcattractionsalerting.mvcClassicArchitecture.models.dto.VisitorCountDTO;
import com.quesocololand.msvcattractionsalerting.mvcClassicArchitecture.services.implementations.RabbitVisitorCountsPublisherServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RabbitPublisherServiceImplTest {
    //Fields of RabbitPublisherServiceImplTest
    //Properties got via injection using @Value
    private final String testExchangeName = "test_visitor_counts_exchange";
    private final String testRoutingKeyPrefix = "test_visitor_counts.#";
    @Mock // Creates a RabbitTemplate mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks // Injects the mocks created (@Mock) at this service instance
    private RabbitVisitorCountsPublisherServiceImpl rabbitPublisherService;

    //Configurations of RabbitPublisherServiceImplTest
    @BeforeEach
    void setUp() {
        // Initialises the properties @Value in your mocked service
        // This is necessary, since @InjectMocks only injects other @Mocks, not the @Value properties
        ReflectionTestUtils.setField(this.rabbitPublisherService, "exchangeName", this.testExchangeName);
        ReflectionTestUtils.setField(this.rabbitPublisherService, "routingKeyPrefix", this.testRoutingKeyPrefix);

        // Resets the RabbitTemplate mock before each test to ensure their independence
        reset(this.rabbitTemplate);
    }

    //Methods of RabbitPublisherServiceImplTest
    @Test
    void publish_shouldSetRegisteredAtIfNullAndSendMessage() {
        // 1. Arrange
        String attractionId = "attractionA123";
        int attendance = 100;

        // Entry message with registeredAt field null
        VisitorCountDTO message = VisitorCountDTO.builder()
            .attractionId(attractionId)
            .count(attendance)
            .registeredAt(null) // To verify this is assigned
            .build();

        // 2. Act
        this.rabbitPublisherService.publishVisitorCount(message);

        // 3. Assert
        // Capture the arguments passed to convertAndSend
        ArgumentCaptor<String> exchangeCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> routingKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<VisitorCountDTO> messageCaptor = ArgumentCaptor.forClass(VisitorCountDTO.class);

        // Verify that convertAndSend was invoked exactly once
        verify(this.rabbitTemplate, times(1))
        .convertAndSend(
            exchangeCaptor.capture(),
            routingKeyCaptor.capture(),
            messageCaptor.capture()
        );

        // Verify the values captured
        assertEquals(exchangeCaptor.getValue(), this.testExchangeName);

        String actualRoutingKey = routingKeyCaptor.getValue();
            assertThat(actualRoutingKey).startsWith("test_visitor_counts."); // Routing key prefix
            assertThat(actualRoutingKey).contains(attractionId); // Contiene el ID de la atracción

        VisitorCountDTO sentMessage = messageCaptor.getValue();
            assertEquals(attractionId, sentMessage.getAttractionId());
            assertEquals(attendance, sentMessage.getCount());
            assertNotNull(sentMessage.getRegisteredAt()); // Verify a timestamp was assigned
    }

    @Test
    void publish_shouldUseExistingRegisteredAtAndSendMessage() {
        // 1. Arrange
        String attractionId = "attractionB456";
        int attendance = 75;
        LocalDateTime existingTimestamp = LocalDateTime.of(2025, 6, 1, 10, 30, 0);

        // Message with registeredAt already set
        VisitorCountDTO message = VisitorCountDTO.builder()
            .attractionId(attractionId)
            .count(attendance)
            .registeredAt(existingTimestamp)
            .build();

        // 2. Act
        this.rabbitPublisherService.publishVisitorCount(message);

        // 3. Assert
        ArgumentCaptor<String> exchangeCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> routingKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<VisitorCountDTO> messageCaptor = ArgumentCaptor.forClass(VisitorCountDTO.class);

        verify(this.rabbitTemplate, times(1))
        .convertAndSend(
            exchangeCaptor.capture(),
            routingKeyCaptor.capture(),
            messageCaptor.capture()
        );

        assertEquals(exchangeCaptor.getValue(), this.testExchangeName);

        String actualRoutingKey = routingKeyCaptor.getValue();
            assertThat(actualRoutingKey).startsWith("test_visitor_counts.");
            assertThat(actualRoutingKey).contains(attractionId);

        VisitorCountDTO sentMessage = messageCaptor.getValue();
            assertEquals(attractionId, sentMessage.getAttractionId());
            assertEquals(attendance, sentMessage.getCount());
            assertEquals(sentMessage.getRegisteredAt(), existingTimestamp); // Verificar que no se cambió
    }

    @Test
    void publish_shouldGenerateRoutingKeyWithUUIDWhenAttractionIdIsNull() {
        // 1. Arrange
        int visitorCount = 200;

        // Message with attractionId null
        VisitorCountDTO message = VisitorCountDTO.builder()
            .attractionId(null) // To verify UUID logic
            .count(visitorCount)
            .registeredAt(LocalDateTime.now())
            .build();

        // 2. Act
        this.rabbitPublisherService.publishVisitorCount(message);

        // 3. Assert
        ArgumentCaptor<String> routingKeyCaptor = ArgumentCaptor.forClass(String.class);

        verify(this.rabbitTemplate, times(1))
        .convertAndSend(
            eq(this.testExchangeName),
            routingKeyCaptor.capture(),
            any(VisitorCountDTO.class)
        );

        String actualRoutingKey = routingKeyCaptor.getValue();
            assertThat(actualRoutingKey).startsWith("test_visitor_counts."); // Prefix expected
        // To verify this is indeed a UUID, one could use a regex o simply assert it doesn’t contain "null"
            assertThat(actualRoutingKey).doesNotContain("null");
        // A UUID has a specific format (i.e. "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx")
        // It is not trivial to verify the exact format of a UUID regardless of UUID.randomUUID() implementation.
        // But, when verifying it contains not "null" and that it has the correct prefix, one covers the basics.
        // A more robust way would be to inject a mockeable UUID generator.
    }
}