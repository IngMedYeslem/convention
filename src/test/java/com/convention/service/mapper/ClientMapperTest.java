package com.convention.service.mapper;

import static com.convention.domain.ClientEntityAsserts.*;
import static com.convention.domain.ClientEntityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClientMapperTest {

    private ClientMapper clientMapper;

    @BeforeEach
    void setUp() {
        clientMapper = new ClientMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getClientEntitySample1();
        var actual = clientMapper.toEntity(clientMapper.toDto(expected));
        assertClientEntityAllPropertiesEquals(expected, actual);
    }
}
