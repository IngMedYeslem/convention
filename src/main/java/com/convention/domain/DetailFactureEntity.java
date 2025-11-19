package com.convention.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DetailFactureEntity.
 */
@Entity
@Table(name = "detail_facture")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DetailFactureEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 200)
    @Column(name = "designation", length = 200, nullable = false)
    private String designation;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "prix_unitaire", precision = 21, scale = 2, nullable = false)
    private BigDecimal prixUnitaire;

    @NotNull
    @Min(value = 1)
    @Column(name = "quantite", nullable = false)
    private Integer quantite;

    @Column(name = "montant_ht", precision = 21, scale = 2)
    private BigDecimal montantHT;

    @DecimalMin(value = "0")
    @DecimalMax(value = "100")
    @Column(name = "taux_tva", precision = 21, scale = 2)
    private BigDecimal tauxTVA;

    @Column(name = "montant_tva", precision = 21, scale = 2)
    private BigDecimal montantTVA;

    @Column(name = "montant_ttc", precision = 21, scale = 2)
    private BigDecimal montantTTC;

    @Size(max = 500)
    @Column(name = "observations", length = 500)
    private String observations;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "client", "convention" }, allowSetters = true)
    private FactureEntity facture;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DetailFactureEntity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesignation() {
        return this.designation;
    }

    public DetailFactureEntity designation(String designation) {
        this.setDesignation(designation);
        return this;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public BigDecimal getPrixUnitaire() {
        return this.prixUnitaire;
    }

    public DetailFactureEntity prixUnitaire(BigDecimal prixUnitaire) {
        this.setPrixUnitaire(prixUnitaire);
        return this;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public Integer getQuantite() {
        return this.quantite;
    }

    public DetailFactureEntity quantite(Integer quantite) {
        this.setQuantite(quantite);
        return this;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public BigDecimal getMontantHT() {
        return this.montantHT;
    }

    public DetailFactureEntity montantHT(BigDecimal montantHT) {
        this.setMontantHT(montantHT);
        return this;
    }

    public void setMontantHT(BigDecimal montantHT) {
        this.montantHT = montantHT;
    }

    public BigDecimal getTauxTVA() {
        return this.tauxTVA;
    }

    public DetailFactureEntity tauxTVA(BigDecimal tauxTVA) {
        this.setTauxTVA(tauxTVA);
        return this;
    }

    public void setTauxTVA(BigDecimal tauxTVA) {
        this.tauxTVA = tauxTVA;
    }

    public BigDecimal getMontantTVA() {
        return this.montantTVA;
    }

    public DetailFactureEntity montantTVA(BigDecimal montantTVA) {
        this.setMontantTVA(montantTVA);
        return this;
    }

    public void setMontantTVA(BigDecimal montantTVA) {
        this.montantTVA = montantTVA;
    }

    public BigDecimal getMontantTTC() {
        return this.montantTTC;
    }

    public DetailFactureEntity montantTTC(BigDecimal montantTTC) {
        this.setMontantTTC(montantTTC);
        return this;
    }

    public void setMontantTTC(BigDecimal montantTTC) {
        this.montantTTC = montantTTC;
    }

    public String getObservations() {
        return this.observations;
    }

    public DetailFactureEntity observations(String observations) {
        this.setObservations(observations);
        return this;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public FactureEntity getFacture() {
        return this.facture;
    }

    public void setFacture(FactureEntity facture) {
        this.facture = facture;
    }

    public DetailFactureEntity facture(FactureEntity facture) {
        this.setFacture(facture);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DetailFactureEntity)) {
            return false;
        }
        return getId() != null && getId().equals(((DetailFactureEntity) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DetailFactureEntity{" +
            "id=" + getId() +
            ", designation='" + getDesignation() + "'" +
            ", prixUnitaire=" + getPrixUnitaire() +
            ", quantite=" + getQuantite() +
            ", montantHT=" + getMontantHT() +
            ", tauxTVA=" + getTauxTVA() +
            ", montantTVA=" + getMontantTVA() +
            ", montantTTC=" + getMontantTTC() +
            ", observations='" + getObservations() + "'" +
            "}";
    }
}
