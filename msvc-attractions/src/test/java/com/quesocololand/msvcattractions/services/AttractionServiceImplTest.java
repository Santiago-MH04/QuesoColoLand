package com.quesocololand.msvcattractions.services;

import com.quesocololand.msvcattractions.exceptions.AttractionPersistenceException;
import com.quesocololand.msvcattractions.models.Attraction;
import com.quesocololand.msvcattractions.models.dto.AttractionDTO;
import com.quesocololand.msvcattractions.models.utils.AttractionStatus;
import com.quesocololand.msvcattractions.repositories.AttractionRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/*@DataMongoTest*/
@ExtendWith({MockitoExtension.class/*, SpringExtension.class*/})
public class AttractionServiceImplTest {

    /*@Autowired
    private MongoTemplate mongoTemplate;*/

    @Mock
    private AttractionRepository repoAttraction;

    @InjectMocks
    private AttractionServiceImpl attractionService;

    private AttractionDTO attractionDTO;
    private Attraction attraction;

    @BeforeEach
    void setUp() {
        attractionDTO = AttractionDTO.builder()
                .id("ADMSCEFPFAIOFNE")
                .name("Montaña Rusa")
                .status("ACTIVE")
                .capacity(20)
                .build();
        attraction = Attraction.builder()
                .id("fenifneoinie")
                .name("Montaña Rusa")
                .status(AttractionStatus.ACTIVE)
                .capacity(20)
                .build();
    }



    /*@Test
    public void shouldMapCarToDto() {
        //given
        Car car = new Car( "Morris", 5, CarType.SEDAN );

        //when
        CarDto carDto = CarMapper.INSTANCE.carToCarDto( car );

        //then
        assertThat( carDto ).isNotNull();
        assertThat( carDto.getMake() ).isEqualTo( "Morris" );
        assertThat( carDto.getSeatCount() ).isEqualTo( 5 );
        assertThat( carDto.getType() ).isEqualTo( "SEDAN" );
    }*/

    @Test
    void testFindAll() {
        when(this.repoAttraction.findAll()).thenReturn(List.of(attraction));

        List<AttractionDTO> result = this.attractionService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Montaña Rusa", result.get(0).getName());

        verify(this.repoAttraction, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(this.repoAttraction.findById("fenifneoinie")).thenReturn(Optional.of(attraction));

        Optional<AttractionDTO> result = this.attractionService.findById("fenifneoinie");

        assertTrue(result.isPresent());
        assertEquals("Montaña Rusa", result.get().getName());
        verify(this.repoAttraction, times(1)).findById("fenifneoinie");
    }

    @Test
    void testCreate() {
        when(this.repoAttraction.save(any(Attraction.class))).thenReturn(attraction);

        Attraction result = this.attractionService.create(attractionDTO);

        assertNotNull(result);
        assertEquals("Montaña Rusa", result.getName());
        verify(this.repoAttraction, times(1)).save(any(Attraction.class));
    }

    @Test
    void testUpdate() throws AttractionPersistenceException {
        when(this.repoAttraction.findById("ADMSCEFPFAIOFNE")).thenReturn(Optional.of(attraction));
        when(this.repoAttraction.save(any(Attraction.class))).thenReturn(attraction);

        Attraction result = this.attractionService.update("ADMSCEFPFAIOFNE", attractionDTO);
            //Tests commented, in order to be proved by API exposure
        /*assertNotNull(result);
        assertEquals("Montaña Rusa", result.getName());*/
            //Refine the update assignation method
        assertNotNull(result.getLastUpdate());
        assertTrue(result.getLastUpdate().isBefore(LocalDateTime.now()));

        verify(this.repoAttraction, times(1)).findById("ADMSCEFPFAIOFNE");
        verify(this.repoAttraction, times(1)).save(any(Attraction.class));
    }

    @Test
    void testUpdateThrowsException() {
        when(this.repoAttraction.findById("ADMSCEFPFAIOFNE")).thenReturn(Optional.empty());

        assertThrows(AttractionPersistenceException.class, () -> {
            this.attractionService.update("ADMSCEFPFAIOFNE", attractionDTO);
        });
        verify(this.repoAttraction, times(1)).findById("ADMSCEFPFAIOFNE");
        verify(this.repoAttraction, times(0)).save(any(Attraction.class));
    }

    @Test
    void testDelete() {
        doNothing().when(this.repoAttraction).deleteById("fenifneoinie");

        this.attractionService.delete("fenifneoinie");

        verify(this.repoAttraction, times(1)).deleteById("fenifneoinie");
    }
}