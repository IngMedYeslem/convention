package com.convention.service;

import com.convention.domain.ConventionEntity;
import com.convention.domain.enumeration.StatutConvention;
import com.convention.repository.ConventionRepository;
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

    public AlerteConventionService(ConventionRepository conventionRepository) {
        this.conventionRepository = conventionRepository;
    }

    /** Exécuté chaque lundi à 09h00 — rappel des conventions actives sans factures récentes */
    @Scheduled(cron = "0 0 9 * * MON")
    @Transactional(readOnly = true)
    public void verifierConventionsActives() {
        List<ConventionEntity> actives = conventionRepository.findByStatut(StatutConvention.ACTIVE);
        LOG.info("Nombre de conventions actives : {}", actives.size());
        for (ConventionEntity conv : actives) {
            LOG.debug(
                "Convention active N°{} — client : {} — période : {}",
                conv.getNumConvention(),
                conv.getClient() != null ? conv.getClient().getNomClient() : "Inconnu",
                conv.getPeriodeEcheance()
            );
        }
    }
}
