package com.convention.service;

import com.convention.domain.ConventionDocumentEntity;
import com.convention.repository.ConventionDocumentRepository;
import com.convention.service.dto.ConventionDocumentDTO;
import com.convention.service.mapper.ConventionDocumentMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class ConventionDocumentService {

    private static final Logger LOG = LoggerFactory.getLogger(ConventionDocumentService.class);

    @Value("${application.documents.upload-dir:./uploads/documents}")
    private String uploadDir;

    private final ConventionDocumentRepository documentRepository;
    private final ConventionDocumentMapper documentMapper;

    public ConventionDocumentService(ConventionDocumentRepository documentRepository, ConventionDocumentMapper documentMapper) {
        this.documentRepository = documentRepository;
        this.documentMapper = documentMapper;
    }

    public ConventionDocumentDTO uploadDocument(
        Long conventionId,
        MultipartFile file,
        String typeDocument,
        String observations,
        String deposePar
    ) throws IOException {
        Path uploadPath = Paths.get(uploadDir).resolve("convention-" + conventionId);
        Files.createDirectories(uploadPath);

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".")
            ? originalFilename.substring(originalFilename.lastIndexOf("."))
            : "";
        String uniqueFilename = UUID.randomUUID() + extension;
        Path filePath = uploadPath.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        ConventionDocumentEntity entity = new ConventionDocumentEntity();
        entity.setNomFichier(originalFilename != null ? originalFilename : uniqueFilename);
        entity.setTypeDocument(typeDocument);
        entity.setContentType(file.getContentType());
        entity.setCheminFichier(filePath.toString());
        entity.setTailleFichier(file.getSize());
        entity.setDateDepot(Instant.now());
        entity.setDeposePar(deposePar);
        entity.setObservations(observations);

        com.convention.domain.ConventionEntity convention = new com.convention.domain.ConventionEntity();
        convention.setId(conventionId);
        entity.setConvention(convention);

        entity = documentRepository.save(entity);
        return documentMapper.toDto(entity);
    }

    @Transactional(readOnly = true)
    public List<ConventionDocumentDTO> findByConvention(Long conventionId) {
        return documentRepository.findByConventionIdOrderByDateDepotDesc(conventionId).stream().map(documentMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public Optional<ConventionDocumentDTO> findOne(Long id) {
        return documentRepository.findById(id).map(documentMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<Path> getFilePath(Long id) {
        return documentRepository.findById(id).map(doc -> Paths.get(doc.getCheminFichier()));
    }

    public void delete(Long id) {
        documentRepository
            .findById(id)
            .ifPresent(doc -> {
                try {
                    Files.deleteIfExists(Paths.get(doc.getCheminFichier()));
                } catch (IOException e) {
                    LOG.warn("Impossible de supprimer le fichier: {}", doc.getCheminFichier());
                }
                documentRepository.delete(doc);
            });
    }
}
