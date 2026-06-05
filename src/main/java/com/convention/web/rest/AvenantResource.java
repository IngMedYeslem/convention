package com.convention.web.rest;

import com.convention.service.AvenantService;
import com.convention.service.dto.AvenantDTO;
import com.convention.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api")
public class AvenantResource {

    private static final String ENTITY_NAME = "avenant";

    private final AvenantService avenantService;

    public AvenantResource(AvenantService avenantService) {
        this.avenantService = avenantService;
    }

    @PostMapping("/avenants")
    public ResponseEntity<AvenantDTO> createAvenant(@Valid @RequestBody AvenantDTO dto) throws Exception {
        if (dto.getId() != null) throw new BadRequestAlertException("Un nouvel avenant ne peut pas avoir d'ID", ENTITY_NAME, "idexists");
        AvenantDTO result = avenantService.save(dto);
        return ResponseEntity.created(new URI("/api/avenants/" + result.getId())).body(result);
    }

    @PutMapping("/avenants/{id}")
    public ResponseEntity<AvenantDTO> updateAvenant(@PathVariable Long id, @Valid @RequestBody AvenantDTO dto) {
        if (dto.getId() == null) throw new BadRequestAlertException("ID invalide", ENTITY_NAME, "idnull");
        AvenantDTO result = avenantService.save(dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/conventions/{conventionId}/avenants")
    public ResponseEntity<List<AvenantDTO>> getAvenantsByConvention(@PathVariable Long conventionId) {
        return ResponseEntity.ok(avenantService.findByConvention(conventionId));
    }

    @GetMapping("/avenants/{id}")
    public ResponseEntity<AvenantDTO> getAvenant(@PathVariable Long id) {
        return ResponseUtil.wrapOrNotFound(avenantService.findOne(id));
    }

    @PostMapping("/avenants/{id}/signer")
    public ResponseEntity<AvenantDTO> signerAvenant(@PathVariable Long id) {
        return ResponseEntity.ok(avenantService.signer(id));
    }

    @DeleteMapping("/avenants/{id}")
    public ResponseEntity<Void> deleteAvenant(@PathVariable Long id) {
        avenantService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert("convention", false, ENTITY_NAME, id.toString()))
            .build();
    }
}
