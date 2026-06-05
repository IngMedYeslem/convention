package com.convention.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.convention.repository.ClientRepository;
import com.convention.service.mapper.ClientMapper;
import com.convention.service.mapper.ConventionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PdfScanServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ConventionMapper conventionMapper;

    @Mock
    private ClientMapper clientMapper;

    private PdfScanService pdfScanService;

    @BeforeEach
    void setUp() {
        pdfScanService = new PdfScanService(clientRepository, conventionMapper, clientMapper);
    }

    @Test
    void contextLoads() {
        assertThat(pdfScanService).isNotNull();
    }
}
