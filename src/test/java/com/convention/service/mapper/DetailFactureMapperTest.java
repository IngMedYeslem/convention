package com.convention.service.mapper;

import static com.convention.domain.DetailFactureEntityAsserts.*;
import static com.convention.domain.DetailFactureEntityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DetailFactureMapperTest {

    private DetailFactureMapper detailFactureMapper;

    @BeforeEach
    void setUp() {
        detailFactureMapper = new DetailFactureMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDetailFactureEntitySample1();
        var actual = detailFactureMapper.toEntity(detailFactureMapper.toDto(expected));
        assertDetailFactureEntityAllPropertiesEquals(expected, actual);
    }
}
