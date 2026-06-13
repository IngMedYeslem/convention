package com.convention.web.rest;

import com.convention.domain.ConventionEntity;
import com.convention.service.ConventionWorkflowService;
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

    // ─── Approbation hiérarchique ─────────────────────────────────────────────

    /** SERVICE: BROUILLON/REJETE → SOUMIS */
    @PutMapping("/{id}/soumettre")
    public ResponseEntity<ConventionEntity> soumettre(@PathVariable Long id) {
        LOG.debug("Soumission convention: {}", id);
        return ResponseEntity.ok(workflowService.soumettre(id));
    }

    /** SERVICE: SOUMIS → BROUILLON (retrait de la soumission) */
    @PutMapping("/{id}/rappeler")
    public ResponseEntity<ConventionEntity> rappeler(@PathVariable Long id) {
        LOG.debug("Rappel convention: {}", id);
        return ResponseEntity.ok(workflowService.rappeler(id));
    }

    /** DEPARTEMENT: SOUMIS → APPROUVE_DEPT */
    @PutMapping("/{id}/approuver-dept")
    public ResponseEntity<ConventionEntity> approuverDept(@PathVariable Long id) {
        LOG.debug("Approbation département: {}", id);
        return ResponseEntity.ok(workflowService.approuverDept(id));
    }

    /** DIRECTION: APPROUVE_DEPT → ACTIVE */
    @PutMapping("/{id}/approuver-direction")
    public ResponseEntity<ConventionEntity> approuverDirection(@PathVariable Long id) {
        LOG.debug("Approbation direction: {}", id);
        return ResponseEntity.ok(workflowService.approuverDirection(id));
    }

    /** Any approver: SOUMIS|APPROUVE_DEPT → REJETE */
    @PutMapping("/{id}/rejeter")
    public ResponseEntity<ConventionEntity> rejeter(
        @PathVariable Long id,
        @RequestParam(required = false, defaultValue = "") String commentaire
    ) {
        LOG.debug("Rejet convention: {}", id);
        return ResponseEntity.ok(workflowService.rejeter(id, commentaire));
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
    public ResponseEntity<ConventionEntity> annuler(
        @PathVariable Long id,
        @RequestParam(required = false, defaultValue = "") String motif
    ) {
        LOG.debug("Annulation: {}", id);
        return ResponseEntity.ok(workflowService.annulerConvention(id, motif));
    }
}
