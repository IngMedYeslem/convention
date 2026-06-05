package com.convention.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "budget_engagement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BudgetEngagementEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "numero_engagement", length = 100, nullable = false, unique = true)
    private String numeroEngagement;

    @NotNull
    @Column(name = "annee_budgetaire", nullable = false)
    private Integer anneeBudgetaire;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "montant_autorise", precision = 21, scale = 2, nullable = false)
    private BigDecimal montantAutorise;

    @DecimalMin(value = "0")
    @Column(name = "montant_consomme", precision = 21, scale = 2)
    private BigDecimal montantConsomme;

    @Size(max = 200)
    @Column(name = "chapitre_budgetaire", length = 200)
    private String chapitreBudgetaire;

    @Size(max = 200)
    @Column(name = "ligne_budgetaire", length = 200)
    private String ligneBudgetaire;

    @Column(name = "date_creation")
    private Instant dateCreation;

    @Lob
    @Column(name = "observations")
    private String observations;

    @ManyToOne(optional = false)
    @NotNull
    private ConventionEntity convention;

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

    public ConventionEntity getConvention() {
        return convention;
    }

    public void setConvention(ConventionEntity convention) {
        this.convention = convention;
    }

    public BigDecimal getSoldeDisponible() {
        BigDecimal consomme = montantConsomme != null ? montantConsomme : BigDecimal.ZERO;
        return montantAutorise != null ? montantAutorise.subtract(consomme) : BigDecimal.ZERO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BudgetEngagementEntity)) return false;
        return getId() != null && getId().equals(((BudgetEngagementEntity) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
