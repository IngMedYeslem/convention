package com.convention.service;

import com.convention.domain.ConventionEntity;
import com.convention.domain.enumeration.EtapeApprobation;
import com.convention.domain.enumeration.StatutConvention;
import com.convention.repository.ConventionRepository;
import java.time.Instant;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ConventionWorkflowService {

    private static final Logger LOG = LoggerFactory.getLogger(ConventionWorkflowService.class);

    private final ConventionRepository conventionRepository;

    public ConventionWorkflowService(ConventionRepository conventionRepository) {
        this.conventionRepository = conventionRepository;
    }

    // ─── Workflow d'approbation ──────────────────────────────────────────────

    /** Étape 1 : soumettre à la revue juridique (REDACTION → REVUE_JURIDIQUE) */
    public ConventionEntity soumettrePourRevueJuridique(Long id) {
        LOG.debug("Soumission revue juridique: {}", id);
        ConventionEntity c = findOrThrow(id);
        validerEtape(c, EtapeApprobation.REDACTION, "REDACTION");
        c.setEtapeApprobation(EtapeApprobation.REVUE_JURIDIQUE);
        c.setDateModification(Instant.now());
        return conventionRepository.save(c);
    }

    /** Étape 2 : valider la revue juridique → demande de visa financier */
    public ConventionEntity validerRevueJuridique(Long id) {
        LOG.debug("Validation revue juridique: {}", id);
        ConventionEntity c = findOrThrow(id);
        validerEtape(c, EtapeApprobation.REVUE_JURIDIQUE, "REVUE_JURIDIQUE");
        c.setEtapeApprobation(EtapeApprobation.VISA_FINANCIER);
        c.setDateModification(Instant.now());
        return conventionRepository.save(c);
    }

    /** Étape 3 : apposer le visa du contrôleur financier */
    public ConventionEntity apposerVisaFinancier(Long id, LocalDate dateVisa) {
        LOG.debug("Visa financier: {}", id);
        ConventionEntity c = findOrThrow(id);
        validerEtape(c, EtapeApprobation.VISA_FINANCIER, "VISA_FINANCIER");
        c.setDateVisaControleur(dateVisa);
        c.setEtapeApprobation(EtapeApprobation.SIGNATURE_DIRECTION);
        c.setDateModification(Instant.now());
        return conventionRepository.save(c);
    }

    /** Étape 4 : signature de la direction → publication officielle */
    public ConventionEntity signerEtPublier(Long id) {
        LOG.debug("Signature direction + publication: {}", id);
        ConventionEntity c = findOrThrow(id);
        validerEtape(c, EtapeApprobation.SIGNATURE_DIRECTION, "SIGNATURE_DIRECTION");
        c.setEtapeApprobation(EtapeApprobation.PUBLIE);
        c.setStatut(StatutConvention.ACTIVE);
        c.setDateModification(Instant.now());
        return conventionRepository.save(c);
    }

    /** Rejeter à n'importe quelle étape → retour en REDACTION */
    public ConventionEntity rejeter(Long id, String commentaire) {
        LOG.debug("Rejet convention: {}", id);
        ConventionEntity c = findOrThrow(id);
        c.setEtapeApprobation(EtapeApprobation.REJETE);
        c.setCommentaireRejet(commentaire);
        c.setStatut(StatutConvention.BROUILLON);
        c.setDateModification(Instant.now());
        return conventionRepository.save(c);
    }

    /** Reprendre après rejet */
    public ConventionEntity reprendreApresRejet(Long id) {
        LOG.debug("Reprise après rejet: {}", id);
        ConventionEntity c = findOrThrow(id);
        if (c.getEtapeApprobation() != EtapeApprobation.REJETE) {
            throw new IllegalStateException("La convention n'est pas en statut REJETE");
        }
        c.setEtapeApprobation(EtapeApprobation.REDACTION);
        c.setCommentaireRejet(null);
        c.setDateModification(Instant.now());
        return conventionRepository.save(c);
    }

    // ─── Gestion du statut opérationnel ─────────────────────────────────────

    public ConventionEntity suspendConvention(Long id) {
        LOG.debug("Suspension convention: {}", id);
        ConventionEntity c = findOrThrow(id);
        if (c.getStatut() != StatutConvention.ACTIVE) throw new IllegalStateException("La convention doit être ACTIVE");
        c.setStatut(StatutConvention.SUSPENDUE);
        c.setDateModification(Instant.now());
        return conventionRepository.save(c);
    }

    public ConventionEntity reactivateConvention(Long id) {
        LOG.debug("Réactivation convention: {}", id);
        ConventionEntity c = findOrThrow(id);
        if (c.getStatut() != StatutConvention.SUSPENDUE) throw new IllegalStateException("La convention doit être SUSPENDUE");
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

    // ─── Helpers ─────────────────────────────────────────────────────────────

    private ConventionEntity findOrThrow(Long id) {
        return conventionRepository.findById(id).orElseThrow(() -> new RuntimeException("Convention introuvable: " + id));
    }

    private void validerEtape(ConventionEntity c, EtapeApprobation attendue, String nom) {
        if (c.getEtapeApprobation() != attendue) {
            throw new IllegalStateException("Étape attendue: " + nom + ", étape actuelle: " + c.getEtapeApprobation());
        }
    }
}
