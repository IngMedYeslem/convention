package com.convention.service;

import com.convention.domain.ConventionEntity;
import com.convention.domain.enumeration.StatutConvention;
import com.convention.repository.ConventionRepository;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for managing convention workflow.
 */
@Service
@Transactional
public class ConventionWorkflowService {

    private static final Logger LOG = LoggerFactory.getLogger(ConventionWorkflowService.class);

    private final ConventionRepository conventionRepository;

    public ConventionWorkflowService(ConventionRepository conventionRepository) {
        this.conventionRepository = conventionRepository;
    }

    /**
     * Activate a convention (BROUILLON -> ACTIVE).
     *
     * @param conventionId the convention ID
     * @return the updated convention
     */
    public ConventionEntity activateConvention(Long conventionId) {
        LOG.debug("Activating convention: {}", conventionId);

        ConventionEntity convention = conventionRepository
            .findById(conventionId)
            .orElseThrow(() -> new RuntimeException("Convention not found: " + conventionId));

        if (convention.getStatut() != StatutConvention.BROUILLON) {
            throw new RuntimeException("Convention must be in BROUILLON status to be activated");
        }

        convention.setStatut(StatutConvention.ACTIVE);
        convention.setDateModification(Instant.now());

        return conventionRepository.save(convention);
    }

    /**
     * Suspend a convention (ACTIVE -> SUSPENDUE).
     *
     * @param conventionId the convention ID
     * @return the updated convention
     */
    public ConventionEntity suspendConvention(Long conventionId) {
        LOG.debug("Suspending convention: {}", conventionId);

        ConventionEntity convention = conventionRepository
            .findById(conventionId)
            .orElseThrow(() -> new RuntimeException("Convention not found: " + conventionId));

        if (convention.getStatut() != StatutConvention.ACTIVE) {
            throw new RuntimeException("Convention must be ACTIVE to be suspended");
        }

        convention.setStatut(StatutConvention.SUSPENDUE);
        convention.setDateModification(Instant.now());

        return conventionRepository.save(convention);
    }

    /**
     * Reactivate a suspended convention (SUSPENDUE -> ACTIVE).
     *
     * @param conventionId the convention ID
     * @return the updated convention
     */
    public ConventionEntity reactivateConvention(Long conventionId) {
        LOG.debug("Reactivating convention: {}", conventionId);

        ConventionEntity convention = conventionRepository
            .findById(conventionId)
            .orElseThrow(() -> new RuntimeException("Convention not found: " + conventionId));

        if (convention.getStatut() != StatutConvention.SUSPENDUE) {
            throw new RuntimeException("Convention must be SUSPENDED to be reactivated");
        }

        convention.setStatut(StatutConvention.ACTIVE);
        convention.setDateModification(Instant.now());

        return conventionRepository.save(convention);
    }

    /**
     * Terminate a convention (ACTIVE/SUSPENDUE -> TERMINEE).
     *
     * @param conventionId the convention ID
     * @return the updated convention
     */
    public ConventionEntity terminateConvention(Long conventionId) {
        LOG.debug("Terminating convention: {}", conventionId);

        ConventionEntity convention = conventionRepository
            .findById(conventionId)
            .orElseThrow(() -> new RuntimeException("Convention not found: " + conventionId));

        if (convention.getStatut() == StatutConvention.TERMINEE || convention.getStatut() == StatutConvention.ANNULEE) {
            throw new RuntimeException("Convention is already terminated or cancelled");
        }

        convention.setStatut(StatutConvention.TERMINEE);
        convention.setDateModification(Instant.now());

        return conventionRepository.save(convention);
    }
}
