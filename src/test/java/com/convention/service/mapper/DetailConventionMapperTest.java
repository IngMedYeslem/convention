package com.convention.service.mapper;

import static com.convention.domain.DetailConventionEntityAsserts.*;
import static com.convention.domain.DetailConventionEntityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DetailConventionMapperTest {

    private DetailConventionMapper detailConventionMapper;

    @BeforeEach
    void setUp() {
        detailConventionMapper = new DetailConventionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDetailConventionEntitySample1();
        var actual = detailConventionMapper.toEntity(detailConventionMapper.toDto(expected));
        assertDetailConventionEntityAllPropertiesEquals(expected, actual);
    }
}
