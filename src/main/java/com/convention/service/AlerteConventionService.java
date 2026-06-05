package com.convention.service;

import com.convention.domain.ConventionEntity;
import com.convention.domain.enumeration.StatutConvention;
import com.convention.repository.ConventionRepository;
import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AlerteConventionService {

    private static final Logger LOG = LoggerFactory.getLogger(AlerteConventionService.class);

    private final ConventionRepository conventionRepository;
    private final MailService mailService;

    public AlerteConventionService(ConventionRepository conventionRepository, MailService mailService) {
        this.conventionRepository = conventionRepository;
        this.mailService = mailService;
    }

    /** Exécuté chaque jour à 08h00 */
    @Scheduled(cron = "0 0 8 * * ?")
    @Transactional(readOnly = true)
    public void verifierEcheancesConventions() {
        LOG.info("Vérification des échéances des conventions...");

        LocalDate aujourd = LocalDate.now();
        LocalDate dans30j = aujourd.plusDays(30);
        LocalDate dans60j = aujourd.plusDays(60);
        LocalDate dans90j = aujourd.plusDays(90);

        List<ConventionEntity> actives = conventionRepository.findByStatut(StatutConvention.ACTIVE);

        for (ConventionEntity conv : actives) {
            LocalDate echeance = conv.getEcheanceConv();
            if (echeance == null) continue;

            if (echeance.isBefore(aujourd)) {
                envoyerAlerte(conv, "EXPIREE", 0);
            } else if (!echeance.isAfter(dans30j)) {
                envoyerAlerte(conv, "30_JOURS", 30);
            } else if (!echeance.isAfter(dans60j)) {
                envoyerAlerte(conv, "60_JOURS", 60);
            } else if (!echeance.isAfter(dans90j)) {
                envoyerAlerte(conv, "90_JOURS", 90);
            }
        }
    }

    /** Exécuté chaque lundi à 09h00 pour les conventions expirées sans action */
    @Scheduled(cron = "0 0 9 * * MON")
    @Transactional(readOnly = true)
    public void rappelConventionsExpirees() {
        LocalDate hier = LocalDate.now().minusDays(1);
        List<ConventionEntity> actives = conventionRepository.findByStatut(StatutConvention.ACTIVE);
        actives
            .stream()
            .filter(c -> c.getEcheanceConv() != null && c.getEcheanceConv().isBefore(hier))
            .forEach(c ->
                LOG.warn("Convention {} (N°{}) expirée depuis {} - action requise", c.getId(), c.getNumConvention(), c.getEcheanceConv())
            );
    }

    private void envoyerAlerte(ConventionEntity conv, String typeAlerte, int joursRestants) {
        String client = conv.getClient() != null ? conv.getClient().getNomClient() : "Inconnu";
        String sujet = buildSujet(typeAlerte, conv.getNumConvention(), joursRestants);
        String corps = buildCorps(typeAlerte, conv, client, joursRestants);

        LOG.info("Alerte {} pour convention N°{} (client: {})", typeAlerte, conv.getNumConvention(), client);

        // Envoi email au responsable si email disponible
        if (conv.getClient() != null && conv.getClient().getEmailClient() != null) {
            try {
                mailService.sendEmail(conv.getClient().getEmailClient(), sujet, corps, false, false);
            } catch (Exception e) {
                LOG.error("Erreur envoi alerte pour convention {}: {}", conv.getId(), e.getMessage());
            }
        }
    }

    private String buildSujet(String type, Long numConv, int jours) {
        return switch (type) {
            case "EXPIREE" -> "[URGENT] Convention N°" + numConv + " expirée";
            case "30_JOURS" -> "[ALERTE] Convention N°" + numConv + " expire dans 30 jours";
            case "60_JOURS" -> "[RAPPEL] Convention N°" + numConv + " expire dans 60 jours";
            case "90_JOURS" -> "[INFO] Convention N°" + numConv + " expire dans 90 jours";
            default -> "Alerte convention N°" + numConv;
        };
    }

    private String buildCorps(String type, ConventionEntity conv, String client, int jours) {
        String echeance = conv.getEcheanceConv() != null ? conv.getEcheanceConv().toString() : "N/A";
        return String.format(
            """
            Alerte de gestion des conventions

            Convention N° : %d
            Partenaire    : %s
            Responsable   : %s
            Échéance      : %s
            Objet         : %s

            %s

            Veuillez prendre les mesures nécessaires (renouvellement, avenant de prolongation, ou clôture).

            -- Système de gestion des conventions
            """,
            conv.getNumConvention(),
            client,
            conv.getNomResponsable(),
            echeance,
            conv.getObjet() != null ? conv.getObjet() : "Non renseigné",
            type.equals("EXPIREE")
                ? "⚠️ CETTE CONVENTION EST EXPIRÉE. Toute exécution sans cadre juridique valide engage la responsabilité de l'institution."
                : "Cette convention expire dans " + jours + " jours."
        );
    }
}
