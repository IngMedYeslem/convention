package com.convention.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public class BudgetEngagementDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String numeroEngagement;

    @NotNull
    private Integer anneeBudgetaire;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal montantAutorise;

    @DecimalMin(value = "0")
    private BigDecimal montantConsomme;

    @Size(max = 200)
    private String chapitreBudgetaire;

    @Size(max = 200)
    private String ligneBudgetaire;

    private Instant dateCreation;

    private String observations;

    private BigDecimal soldeDisponible;

    @NotNull
    private ConventionDTO convention;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroEngagement() {
        return numeroEngagement;
    }

    public void setNumeroEngagement(String numeroEngagement) {
        this.numeroEngagement = numeroEngagement;
    }

    public Integer getAnneeBudgetaire() {
        return anneeBudgetaire;
    }

    public void setAnneeBudgetaire(Integer anneeBudgetaire) {
        this.anneeBudgetaire = anneeBudgetaire;
    }

    public BigDecimal getMontantAutorise() {
        return montantAutorise;
    }

    public void setMontantAutorise(BigDecimal montantAutorise) {
        this.montantAutorise = montantAutorise;
    }

    public BigDecimal getMontantConsomme() {
        return montantConsomme;
    }

    public void setMontantConsomme(BigDecimal montantConsomme) {
        this.montantConsomme = montantConsomme;
    }

    public String getChapitreBudgetaire() {
        return chapitreBudgetaire;
    }

    public void setChapitreBudgetaire(String chapitreBudgetaire) {
        this.chapitreBudgetaire = chapitreBudgetaire;
    }

    public String getLigneBudgetaire() {
        return ligneBudgetaire;
    }

    public void setLigneBudgetaire(String ligneBudgetaire) {
        this.ligneBudgetaire = ligneBudgetaire;
    }

    public Instant getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public BigDecimal getSoldeDisponible() {
        return soldeDisponible;
    }

    public void setSoldeDisponible(BigDecimal soldeDisponible) {
        this.soldeDisponible = soldeDisponible;
    }

    public ConventionDTO getConvention() {
        return convention;
    }

    public void setConvention(ConventionDTO convention) {
        this.convention = convention;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BudgetEngagementDTO)) return false;
        BudgetEngagementDTO that = (BudgetEngagementDTO) o;
        if (this.id == null) return false;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}
