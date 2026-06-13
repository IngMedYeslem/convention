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
 * Workflow d'approbation hiérarchique:
 *   SERVICE     : BROUILLON → [soumettre]        → SOUMIS
 *   DEPARTEMENT : SOUMIS    → [approuver]         → APPROUVE_DEPT
 *   DIRECTION   : APPROUVE_DEPT → [approuver]     → ACTIVE
 *   Any approver: n'importe quel statut → [rejeter] → REJETE
 *   SERVICE     : SOUMIS    → [rappeler]           → BROUILLON
 *   SERVICE     : REJETE    → [soumettre]          → SOUMIS  (re-submission)
 */
@Service
@Transactional
public class ConventionWorkflowService {

    private static final Logger LOG = LoggerFactory.getLogger(ConventionWorkflowService.class);

    private final ConventionRepository conventionRepository;

    public ConventionWorkflowService(ConventionRepository conventionRepository) {
        this.conventionRepository = conventionRepository;
    }

    // ─── Approbation hiérarchique ─────────────────────────────────────────────

    /** SERVICE soumet la convention (BROUILLON ou REJETE → SOUMIS) */
    public ConventionEntity soumettre(Long id) {
        LOG.debug("Soumission convention: {}", id);
        ConventionEntity c = findOrThrow(id);
        if (c.getStatut() != StatutConvention.BROUILLON && c.getStatut() != StatutConvention.REJETE) {
            throw new IllegalStateException("La convention doit être en BROUILLON ou REJETE pour être soumise");
        }
        c.setStatut(StatutConvention.SOUMIS);
        c.setCommentaireRejet(null);
        c.setDateModification(Instant.now());
        return conventionRepository.save(c);
    }

    /** SERVICE rappelle la convention (SOUMIS → BROUILLON) */
    public ConventionEntity rappeler(Long id) {
        LOG.debug("Rappel convention: {}", id);
        ConventionEntity c = findOrThrow(id);
        if (c.getStatut() != StatutConvention.SOUMIS) {
            throw new IllegalStateException("La convention doit être en SOUMIS pour être rappelée");
        }
        c.setStatut(StatutConvention.BROUILLON);
        c.setDateModification(Instant.now());
        return conventionRepository.save(c);
    }

    /** DEPARTEMENT approuve (SOUMIS → APPROUVE_DEPT) */
    public ConventionEntity approuverDept(Long id) {
        LOG.debug("Approbation département: {}", id);
        ConventionEntity c = findOrThrow(id);
        if (c.getStatut() != StatutConvention.SOUMIS) {
            throw new IllegalStateException("La convention doit être en SOUMIS pour approbation département");
        }
        c.setStatut(StatutConvention.APPROUVE_DEPT);
        c.setDateModification(Instant.now());
        return conventionRepository.save(c);
    }

    /** DIRECTION approuve en final (APPROUVE_DEPT → ACTIVE) */
    public ConventionEntity approuverDirection(Long id) {
        LOG.debug("Approbation direction: {}", id);
        ConventionEntity c = findOrThrow(id);
        if (c.getStatut() != StatutConvention.APPROUVE_DEPT) {
            throw new IllegalStateException("La convention doit être en APPROUVE_DEPT pour approbation direction");
        }
        c.setStatut(StatutConvention.ACTIVE);
        c.setDateModification(Instant.now());
        return conventionRepository.save(c);
    }

    /** Rejet à n'importe quelle étape d'approbation → REJETE */
    public ConventionEntity rejeter(Long id, String commentaire) {
        LOG.debug("Rejet convention: {}", id);
        ConventionEntity c = findOrThrow(id);
        if (c.getStatut() != StatutConvention.SOUMIS && c.getStatut() != StatutConvention.APPROUVE_DEPT) {
            throw new IllegalStateException("La convention doit être en attente d'approbation pour être rejetée");
        }
        c.setStatut(StatutConvention.REJETE);
        c.setCommentaireRejet(commentaire);
        c.setDateModification(Instant.now());
        return conventionRepository.save(c);
    }

    // ─── Gestion du statut opérationnel ──────────────────────────────────────

    public ConventionEntity suspendConvention(Long id) {
        LOG.debug("Suspension convention: {}", id);
        ConventionEntity c = findOrThrow(id);
        if (c.getStatut() != StatutConvention.ACTIVE) {
            throw new IllegalStateException("La convention doit être ACTIVE pour être suspendue");
        }
        c.setStatut(StatutConvention.SUSPENDUE);
        c.setDateModification(Instant.now());
        return conventionRepository.save(c);
    }

    public ConventionEntity reactivateConvention(Long id) {
        LOG.debug("Réactivation convention: {}", id);
        ConventionEntity c = findOrThrow(id);
        if (c.getStatut() != StatutConvention.SUSPENDUE) {
            throw new IllegalStateException("La convention doit être SUSPENDUE pour être réactivée");
        }
        c.setStatut(StatutConvention.ACTIVE);
        c.setDateModification(Instant.now());
        return conventionRepository.save(c);
    }

    public ConventionEntity terminateConvention(Long id) {
        LOG.debug("Clôture convention: {}", id);
        ConventionEntity c = findOrThrow(id);
        if (c.getStatut() == StatutConvention.TERMINEE || c.getStatut() == StatutConvention.ANNULEE) {
            throw new IllegalStateException("Convention déjà clôturée ou annulée");
        }
        c.setStatut(StatutConvention.TERMINEE);
        c.setDateModification(Instant.now());
        return conventionRepository.save(c);
    }

    public ConventionEntity annulerConvention(Long id, String motif) {
        LOG.debug("Annulation convention: {}", id);
        ConventionEntity c = findOrThrow(id);
        if (c.getStatut() == StatutConvention.TERMINEE || c.getStatut() == StatutConvention.ANNULEE) {
            throw new IllegalStateException("Convention déjà clôturée ou annulée");
        }
        c.setStatut(StatutConvention.ANNULEE);
        c.setCommentaireRejet(motif);
        c.setDateModification(Instant.now());
        return conventionRepository.save(c);
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private ConventionEntity findOrThrow(Long id) {
        return conventionRepository.findById(id).orElseThrow(() -> new RuntimeException("Convention introuvable: " + id));
    }
}
