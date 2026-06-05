package com.convention.web.rest;

import com.convention.domain.ConventionEntity;
import com.convention.service.ConventionWorkflowService;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/convention-workflow")
public class ConventionWorkflowResource {

    private static final Logger LOG = LoggerFactory.getLogger(ConventionWorkflowResource.class);

    private final ConventionWorkflowService workflowService;

    public ConventionWorkflowResource(ConventionWorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    // ─── Approbation ──────────────────────────────────────────────────────────

    @PutMapping("/{id}/soumettre-juridique")
    public ResponseEntity<ConventionEntity> soumettreJuridique(@PathVariable Long id) {
        LOG.debug("Soumission revue juridique: {}", id);
        return ResponseEntity.ok(workflowService.soumettrePourRevueJuridique(id));
    }

    @PutMapping("/{id}/valider-juridique")
    public ResponseEntity<ConventionEntity> validerJuridique(@PathVariable Long id) {
        LOG.debug("Validation juridique: {}", id);
        return ResponseEntity.ok(workflowService.validerRevueJuridique(id));
    }

    @PutMapping("/{id}/visa-financier")
    public ResponseEntity<ConventionEntity> visaFinancier(@PathVariable Long id, @RequestParam String dateVisa) {
        LOG.debug("Visa financier: {}", id);
        return ResponseEntity.ok(workflowService.apposerVisaFinancier(id, LocalDate.parse(dateVisa)));
    }

    @PutMapping("/{id}/signer-publier")
    public ResponseEntity<ConventionEntity> signerEtPublier(@PathVariable Long id) {
        LOG.debug("Signature + publication: {}", id);
        return ResponseEntity.ok(workflowService.signerEtPublier(id));
    }

    @PutMapping("/{id}/rejeter")
    public ResponseEntity<ConventionEntity> rejeter(@PathVariable Long id, @RequestParam String commentaire) {
        LOG.debug("Rejet convention: {}", id);
        return ResponseEntity.ok(workflowService.rejeter(id, commentaire));
    }

    @PutMapping("/{id}/reprendre")
    public ResponseEntity<ConventionEntity> reprendre(@PathVariable Long id) {
        LOG.debug("Reprise après rejet: {}", id);
        return ResponseEntity.ok(workflowService.reprendreApresRejet(id));
    }

    // ─── Statut opérationnel ──────────────────────────────────────────────────

    @PutMapping("/{id}/suspend")
    public ResponseEntity<ConventionEntity> suspendConvention(@PathVariable Long id) {
        LOG.debug("Suspension: {}", id);
        return ResponseEntity.ok(workflowService.suspendConvention(id));
    }

    @PutMapping("/{id}/reactivate")
    public ResponseEntity<ConventionEntity> reactivateConvention(@PathVariable Long id) {
        LOG.debug("Réactivation: {}", id);
        return ResponseEntity.ok(workflowService.reactivateConvention(id));
    }

    @PutMapping("/{id}/terminate")
    public ResponseEntity<ConventionEntity> terminateConvention(@PathVariable Long id) {
        LOG.debug("Clôture: {}", id);
        return ResponseEntity.ok(workflowService.terminateConvention(id));
    }

    @PutMapping("/{id}/annuler")
    public ResponseEntity<ConventionEntity> annuler(@PathVariable Long id, @RequestParam String motif) {
        LOG.debug("Annulation: {}", id);
        return ResponseEntity.ok(workflowService.annulerConvention(id, motif));
    }
}
