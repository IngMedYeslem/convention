package com.convention.web.rest;

import com.convention.domain.FactureEntity;
import com.convention.service.FactureGenerationService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for invoice generation.
 */
@RestController
@RequestMapping("/api/facture-generation")
public class FactureGenerationResource {

    private static final Logger LOG = LoggerFactory.getLogger(FactureGenerationResource.class);

    private final FactureGenerationService factureGenerationService;

    public FactureGenerationResource(FactureGenerationService factureGenerationService) {
        this.factureGenerationService = factureGenerationService;
    }

    /**
     * {@code POST  /facture-generation/convention/:id} : generate invoice for convention.
     *
     * @param id the convention ID
     * @return the generated invoice
     */
    @PostMapping("/convention/{id}")
    public ResponseEntity<FactureEntity> generateFactureFromConvention(@PathVariable Long id) {
        LOG.debug("REST request to generate invoice for convention: {}", id);
        FactureEntity facture = factureGenerationService.generateFactureFromConvention(id);
        return ResponseEntity.ok().body(facture);
    }

    /**
     * {@code POST  /facture-generation/active-conventions} : generate invoices for all active conventions.
     *
     * @return the list of generated invoices
     */
    @PostMapping("/active-conventions")
    public ResponseEntity<List<FactureEntity>> generateFacturesForActiveConventions() {
        LOG.debug("REST request to generate invoices for all active conventions");
        List<FactureEntity> factures = factureGenerationService.generateFacturesForActiveConventions();
        return ResponseEntity.ok().body(factures);
    }
}
