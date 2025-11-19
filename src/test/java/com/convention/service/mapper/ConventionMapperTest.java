package com.convention.service.mapper;

import static com.convention.domain.ConventionEntityAsserts.*;
import static com.convention.domain.ConventionEntityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConventionMapperTest {

    private ConventionMapper conventionMapper;

    @BeforeEach
    void setUp() {
        conventionMapper = new ConventionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getConventionEntitySample1();
        var actual = conventionMapper.toEntity(conventionMapper.toDto(expected));
        assertConventionEntityAllPropertiesEquals(expected, actual);
    }
}
