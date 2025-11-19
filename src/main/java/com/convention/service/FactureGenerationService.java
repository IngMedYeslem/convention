package com.convention.service;

import com.convention.domain.ConventionEntity;
import com.convention.domain.DetailFactureEntity;
import com.convention.domain.FactureEntity;
import com.convention.domain.enumeration.StatutFacture;
import com.convention.domain.enumeration.TypeFacture;
import com.convention.repository.ConventionRepository;
import com.convention.repository.DetailFactureRepository;
import com.convention.repository.FactureRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for generating invoices from conventions.
 */
@Service
public class FactureGenerationService {

    private static final Logger LOG = LoggerFactory.getLogger(FactureGenerationService.class);

    private final FactureRepository factureRepository;
    private final DetailFactureRepository detailFactureRepository;
    private final ConventionRepository conventionRepository;

    public FactureGenerationService(
        FactureRepository factureRepository,
        DetailFactureRepository detailFactureRepository,
        ConventionRepository conventionRepository
    ) {
        this.factureRepository = factureRepository;
        this.detailFactureRepository = detailFactureRepository;
        this.conventionRepository = conventionRepository;
    }

    /**
     * Generate invoice from convention.
     *
     * @param conventionId the convention ID
     * @return the generated invoice
     */
    public FactureEntity generateFactureFromConvention(Long conventionId) {
        LOG.debug("Generating invoice for convention: {}", conventionId);

        ConventionEntity convention = conventionRepository
            .findById(conventionId)
            .orElseThrow(() -> new RuntimeException("Convention not found: " + conventionId));

        // Create invoice
        FactureEntity facture = new FactureEntity();
        facture.setNumFacture(generateFactureNumber());
        facture.setDateFacture(LocalDate.now());
        facture.setDateEcheance(LocalDate.now().plusDays(30));
        facture.setTypeFacture(TypeFacture.NORMALE);
        facture.setStatut(StatutFacture.EMISE);
        facture.setClient(convention.getClient());
        facture.setMontantTotal(convention.getRedevance());
        facture.setDateCreation(java.time.Instant.now());

        // Calculate amounts first
        BigDecimal montantHT = convention.getRedevance();
        BigDecimal tauxTVA = BigDecimal.valueOf(20); // 20% taux
        BigDecimal montantTVA = montantHT.multiply(BigDecimal.valueOf(0.20));
        BigDecimal montantTTC = montantHT.add(montantTVA);

        // Set all amounts before saving
        facture.setMontantTotal(montantHT);
        facture.setTva(tauxTVA); // Stocker le taux (20) pas le montant
        facture.setMontantTTC(montantTTC);

        facture = factureRepository.save(facture);

        // Create invoice detail
        DetailFactureEntity detail = new DetailFactureEntity();
        detail.setDesignation("Redevance convention N° " + convention.getNumConvention());
        detail.setPrixUnitaire(convention.getRedevance());
        detail.setQuantite(1);
        detail.setMontantHT(montantHT);
        detail.setTauxTVA(tauxTVA);
        detail.setMontantTVA(montantTVA);
        detail.setMontantTTC(montantTTC);
        detail.setFacture(facture);

        detailFactureRepository.save(detail);

        return facture;
    }

    /**
     * Generate invoices for all active conventions.
     *
     * @return list of generated invoices
     */
    public List<FactureEntity> generateFacturesForActiveConventions() {
        LOG.debug("Generating invoices for all active conventions");

        List<ConventionEntity> activeConventions = conventionRepository.findByStatut(
            com.convention.domain.enumeration.StatutConvention.ACTIVE
        );

        if (activeConventions.isEmpty()) {
            LOG.info("No active conventions found");
            return List.of();
        }

        return activeConventions
            .stream()
            .map(convention -> {
                try {
                    return generateFactureFromConvention(convention.getId());
                } catch (Exception e) {
                    LOG.error("Error generating invoice for convention {}: {}", convention.getId(), e.getMessage());
                    throw new RuntimeException("Failed to generate invoice for convention " + convention.getId(), e);
                }
            })
            .toList();
    }

    private Long generateFactureNumber() {
        try {
            Long count = factureRepository.count() + 1;
            String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
            String numberStr = dateStr + String.format("%04d", count);
            return Long.valueOf(numberStr);
        } catch (Exception e) {
            LOG.error("Error generating invoice number: {}", e.getMessage());
            // Fallback: use timestamp
            return System.currentTimeMillis() / 1000;
        }
    }
}
