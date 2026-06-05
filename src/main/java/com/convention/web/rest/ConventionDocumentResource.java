package com.convention.web.rest;

import com.convention.service.ConventionDocumentService;
import com.convention.service.dto.ConventionDocumentDTO;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class ConventionDocumentResource {

    private final ConventionDocumentService documentService;

    public ConventionDocumentResource(ConventionDocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/conventions/{conventionId}/documents")
    public ResponseEntity<ConventionDocumentDTO> uploadDocument(
        @PathVariable Long conventionId,
        @RequestParam MultipartFile file,
        @RequestParam String typeDocument,
        @RequestParam(required = false) String observations
    ) throws IOException {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        ConventionDocumentDTO result = documentService.uploadDocument(conventionId, file, typeDocument, observations, currentUser);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/conventions/{conventionId}/documents")
    public ResponseEntity<List<ConventionDocumentDTO>> getDocuments(@PathVariable Long conventionId) {
        return ResponseEntity.ok(documentService.findByConvention(conventionId));
    }

    @GetMapping("/documents/{id}/download")
    public ResponseEntity<ByteArrayResource> downloadDocument(@PathVariable Long id) throws IOException {
        Path filePath = documentService.getFilePath(id).orElse(null);
        if (filePath == null || !Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }
        byte[] data = Files.readAllBytes(filePath);
        String contentType = Files.probeContentType(filePath);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filePath.getFileName() + "\"")
            .contentType(contentType != null ? MediaType.parseMediaType(contentType) : MediaType.APPLICATION_OCTET_STREAM)
            .body(new ByteArrayResource(data));
    }

    @DeleteMapping("/documents/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        documentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
