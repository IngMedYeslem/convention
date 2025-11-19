package com.convention.web.rest;

import com.convention.domain.ConventionEntity;
import com.convention.service.ConventionWorkflowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for convention workflow management.
 */
@RestController
@RequestMapping("/api/convention-workflow")
public class ConventionWorkflowResource {

    private static final Logger LOG = LoggerFactory.getLogger(ConventionWorkflowResource.class);

    private final ConventionWorkflowService conventionWorkflowService;

    public ConventionWorkflowResource(ConventionWorkflowService conventionWorkflowService) {
        this.conventionWorkflowService = conventionWorkflowService;
    }

    /**
     * {@code PUT  /convention-workflow/:id/activate} : activate a convention.
     *
     * @param id the convention ID
     * @return the updated convention
     */
    @PutMapping("/{id}/activate")
    public ResponseEntity<ConventionEntity> activateConvention(@PathVariable Long id) {
        LOG.debug("REST request to activate convention: {}", id);
        ConventionEntity convention = conventionWorkflowService.activateConvention(id);
        return ResponseEntity.ok().body(convention);
    }

    /**
     * {@code PUT  /convention-workflow/:id/suspend} : suspend a convention.
     *
     * @param id the convention ID
     * @return the updated convention
     */
    @PutMapping("/{id}/suspend")
    public ResponseEntity<ConventionEntity> suspendConvention(@PathVariable Long id) {
        LOG.debug("REST request to suspend convention: {}", id);
        ConventionEntity convention = conventionWorkflowService.suspendConvention(id);
        return ResponseEntity.ok().body(convention);
    }

    /**
     * {@code PUT  /convention-workflow/:id/reactivate} : reactivate a convention.
     *
     * @param id the convention ID
     * @return the updated convention
     */
    @PutMapping("/{id}/reactivate")
    public ResponseEntity<ConventionEntity> reactivateConvention(@PathVariable Long id) {
        LOG.debug("REST request to reactivate convention: {}", id);
        ConventionEntity convention = conventionWorkflowService.reactivateConvention(id);
        return ResponseEntity.ok().body(convention);
    }

    /**
     * {@code PUT  /convention-workflow/:id/terminate} : terminate a convention.
     *
     * @param id the convention ID
     * @return the updated convention
     */
    @PutMapping("/{id}/terminate")
    public ResponseEntity<ConventionEntity> terminateConvention(@PathVariable Long id) {
        LOG.debug("REST request to terminate convention: {}", id);
        ConventionEntity convention = conventionWorkflowService.terminateConvention(id);
        return ResponseEntity.ok().body(convention);
    }
}
