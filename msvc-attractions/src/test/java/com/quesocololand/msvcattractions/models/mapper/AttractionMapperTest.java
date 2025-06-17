package com.quesocololand.msvcattractions.models.mapper;

import com.quesocololand.msvcattractions.models.Attraction;
import com.quesocololand.msvcattractions.models.dto.AttractionDTO;
import com.quesocololand.msvcattractions.models.utils.AttractionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AttractionMapperTest {

    private AttractionDTO attractionDTO;
    private Attraction attraction;

    @BeforeEach
    void setUp() {
        attractionDTO = AttractionDTO.builder()
                ._id("ADMSCEFPFAIOFNE")
                .name("Montaña Rusa")
                .status("ACTIVE")
                .capacity(20)
                .build();
        attraction = Attraction.builder()
                ._id("fenifneoinie")
                .name("Montaña Rusa")
                .status(AttractionStatus.ACTIVE)
                .capacity(20)
                .build();
    }

    @Test
    void toAttraction() {
        Attraction attractionMap = AttractionMapper.INSTANCE.toAttraction(attractionDTO);

        assertNotNull(attractionMap);
        assertEquals("Montaña Rusa", attractionMap.getName());
        assertEquals( 20, attractionMap.getCapacity());
        assertEquals(String.valueOf(attractionMap.getStatus()), String.valueOf(AttractionStatus.ACTIVE));
    }

    @Test
    void toDTO() {
        AttractionDTO attractionDTOMap= AttractionMapper.INSTANCE.toDTO(attraction);

        assertNotNull(attractionDTOMap);
        assertEquals("Montaña Rusa", attractionDTOMap.getName());
        assertEquals( 20, attractionDTOMap.getCapacity());
        assertEquals(String.valueOf(AttractionStatus.ACTIVE), attractionDTOMap.getStatus());
    }
}