package com.convention.service;

import com.convention.domain.enumeration.StatutConvention;
import com.convention.domain.enumeration.StatutFacture;
import com.convention.repository.ConventionRepository;
import com.convention.repository.FactureRepository;
import com.convention.repository.PaymentRepository;
import com.convention.service.dto.StatistiquesDTO;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for generating statistics.
 */
@Service
@Transactional(readOnly = true)
public class StatistiquesService {

    private final ConventionRepository conventionRepository;
    private final FactureRepository factureRepository;
    private final PaymentRepository paymentRepository;

    public StatistiquesService(
        ConventionRepository conventionRepository,
        FactureRepository factureRepository,
        PaymentRepository paymentRepository
    ) {
        this.conventionRepository = conventionRepository;
        this.factureRepository = factureRepository;
        this.paymentRepository = paymentRepository;
    }

    /**
     * Get dashboard statistics.
     *
     * @return statistics DTO
     */
    public StatistiquesDTO getDashboardStatistics() {
        StatistiquesDTO stats = new StatistiquesDTO();

        // Convention statistics
        stats.setTotalConventions(conventionRepository.count());
        stats.setConventionsActives(conventionRepository.countByStatut(StatutConvention.ACTIVE));
        stats.setConventionsSuspendues(conventionRepository.countByStatut(StatutConvention.SUSPENDUE));

        // Invoice statistics
        stats.setTotalFactures(factureRepository.count());
        stats.setChiffresAffaires(factureRepository.sumMontantTTCByStatut(StatutFacture.PAYEE));
        stats.setMontantImpaye(factureRepository.sumMontantTTCByStatut(StatutFacture.EMISE));

        // Monthly statistics
        stats.setConventionsParMois(getConventionsParMois());
        stats.setChiffresAffairesParMois(getChiffresAffairesParMois());

        return stats;
    }

    private Map<String, Long> getConventionsParMois() {
        Map<String, Long> result = new LinkedHashMap<>();
        LocalDate startDate = LocalDate.now().minusMonths(11).withDayOfMonth(1);

        for (int i = 0; i < 12; i++) {
            LocalDate monthStart = startDate.plusMonths(i);
            LocalDate monthEnd = monthStart.plusMonths(1).minusDays(1);
            String monthKey = monthStart.format(DateTimeFormatter.ofPattern("yyyy-MM"));

            Long count = conventionRepository.countByDateSignConvBetween(monthStart, monthEnd);
            result.put(monthKey, count);
        }

        return result;
    }

    private Map<String, BigDecimal> getChiffresAffairesParMois() {
        Map<String, BigDecimal> result = new LinkedHashMap<>();
        LocalDate startDate = LocalDate.now().minusMonths(11).withDayOfMonth(1);

        for (int i = 0; i < 12; i++) {
            LocalDate monthStart = startDate.plusMonths(i);
            LocalDate monthEnd = monthStart.plusMonths(1).minusDays(1);
            String monthKey = monthStart.format(DateTimeFormatter.ofPattern("yyyy-MM"));

            BigDecimal amount = factureRepository.sumMontantTTCByDateFactureBetweenAndStatut(monthStart, monthEnd, StatutFacture.PAYEE);
            result.put(monthKey, amount != null ? amount : BigDecimal.ZERO);
        }

        return result;
    }
}
