package com.convention.web.rest;

import com.convention.service.PdfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for PDF generation.
 */
@RestController
@RequestMapping("/api/pdf")
public class PdfResource {

    private static final String ENTITY_NAME = "pdf";

    private static final Logger LOG = LoggerFactory.getLogger(PdfResource.class);

    private final PdfService pdfService;

    public PdfResource(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    /**
     * {@code GET  /pdf/convention/:id} : generate PDF for convention.
     *
     * @param id the convention ID
     * @return the PDF file
     */
    @GetMapping("/convention/{id}")
    public ResponseEntity<byte[]> generateConventionPdf(@PathVariable Long id) {
        LOG.debug("REST request to generate PDF for convention: {}", id);

        byte[] pdfContent = pdfService.generateConventionPdf(id);

        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_PDF)
            .header("Content-Disposition", "inline; filename=convention-" + id + ".pdf")
            .body(pdfContent);
    }
}
