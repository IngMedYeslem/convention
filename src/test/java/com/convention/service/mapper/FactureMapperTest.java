package com.convention.service.mapper;

import static com.convention.domain.FactureEntityAsserts.*;
import static com.convention.domain.FactureEntityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FactureMapperTest {

    private FactureMapper factureMapper;

    @BeforeEach
    void setUp() {
        factureMapper = new FactureMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getFactureEntitySample1();
        var actual = factureMapper.toEntity(factureMapper.toDto(expected));
        assertFactureEntityAllPropertiesEquals(expected, actual);
    }
}
