package com.convention.service.dto;

import java.math.BigDecimal;
import java.util.Map;

/**
 * DTO for statistics data.
 */
public class StatistiquesDTO {

    private Long totalConventions;
    private Long conventionsActives;
    private Long conventionsSuspendues;
    private Long totalFactures;
    private BigDecimal chiffresAffaires;
    private BigDecimal montantImpaye;
    private Map<String, Long> conventionsParMois;
    private Map<String, BigDecimal> chiffresAffairesParMois;

    public Long getTotalConventions() {
        return totalConventions;
    }

    public void setTotalConventions(Long totalConventions) {
        this.totalConventions = totalConventions;
    }

    public Long getConventionsActives() {
        return conventionsActives;
    }

    public void setConventionsActives(Long conventionsActives) {
        this.conventionsActives = conventionsActives;
    }

    public Long getConventionsSuspendues() {
        return conventionsSuspendues;
    }

    public void setConventionsSuspendues(Long conventionsSuspendues) {
        this.conventionsSuspendues = conventionsSuspendues;
    }

    public Long getTotalFactures() {
        return totalFactures;
    }

    public void setTotalFactures(Long totalFactures) {
        this.totalFactures = totalFactures;
    }

    public BigDecimal getChiffresAffaires() {
        return chiffresAffaires;
    }

    public void setChiffresAffaires(BigDecimal chiffresAffaires) {
        this.chiffresAffaires = chiffresAffaires;
    }

    public BigDecimal getMontantImpaye() {
        return montantImpaye;
    }

    public void setMontantImpaye(BigDecimal montantImpaye) {
        this.montantImpaye = montantImpaye;
    }

    public Map<String, Long> getConventionsParMois() {
        return conventionsParMois;
    }

    public void setConventionsParMois(Map<String, Long> conventionsParMois) {
        this.conventionsParMois = conventionsParMois;
    }

    public Map<String, BigDecimal> getChiffresAffairesParMois() {
        return chiffresAffairesParMois;
    }

    public void setChiffresAffairesParMois(Map<String, BigDecimal> chiffresAffairesParMois) {
        this.chiffresAffairesParMois = chiffresAffairesParMois;
    }
}
