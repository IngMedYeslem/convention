package com.convention.web.rest;

import com.convention.service.PdfScanService;
import com.convention.service.PdfService;
import com.convention.service.PdfTestService;
import com.convention.service.dto.ConventionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * REST controller for PDF generation.
 */
@RestController
@RequestMapping("/api/pdf")
public class PdfResource {

    private static final String ENTITY_NAME = "pdf";

    private static final Logger LOG = LoggerFactory.getLogger(PdfResource.class);

    private final PdfService pdfService;
    private final PdfScanService pdfScanService;
    private final PdfTestService pdfTestService;

    public PdfResource(PdfService pdfService, PdfScanService pdfScanService, PdfTestService pdfTestService) {
        this.pdfService = pdfService;
        this.pdfScanService = pdfScanService;
        this.pdfTestService = pdfTestService;
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

    /**
     * {@code POST  /pdf/scan} : scan PDF and extract convention data.
     *
     * @param file the PDF file to scan
     * @return the extracted convention data
     */
    @PostMapping("/scan")
    public ResponseEntity<ConventionDTO> scanPdf(@RequestParam("file") MultipartFile file) {
        LOG.debug("REST request to scan PDF file: {}", file.getOriginalFilename());

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        if (!"application/pdf".equals(file.getContentType())) {
            return ResponseEntity.badRequest().build();
        }

        try {
            ConventionDTO conventionData = pdfScanService.scanPdfForConvention(file);
            return ResponseEntity.ok(conventionData);
        } catch (Exception e) {
            LOG.error("Error scanning PDF: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * {@code GET  /pdf/test} : generate a test PDF for scanning demonstration.
     *
     * @return the test PDF file
     */
    @GetMapping("/test")
    public ResponseEntity<byte[]> generateTestPdf() {
        LOG.debug("REST request to generate test PDF for scanning");

        byte[] pdfContent = pdfTestService.generateTestConventionPdf();

        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_PDF)
            .header("Content-Disposition", "attachment; filename=convention-test.pdf")
            .body(pdfContent);
    }
}
