package com.convention.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.convention.domain.DetailFactureEntity} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DetailFactureDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 200)
    private String designation;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal prixUnitaire;

    @NotNull
    @Min(value = 1)
    private Integer quantite;

    private BigDecimal montantHT;

    @DecimalMin(value = "0")
    @DecimalMax(value = "100")
    private BigDecimal tauxTVA;

    private BigDecimal montantTVA;

    private BigDecimal montantTTC;

    @Size(max = 500)
    private String observations;

    @NotNull
    private FactureDTO facture;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public BigDecimal getMontantHT() {
        return montantHT;
    }

    public void setMontantHT(BigDecimal montantHT) {
        this.montantHT = montantHT;
    }

    public BigDecimal getTauxTVA() {
        return tauxTVA;
    }

    public void setTauxTVA(BigDecimal tauxTVA) {
        this.tauxTVA = tauxTVA;
    }

    public BigDecimal getMontantTVA() {
        return montantTVA;
    }

    public void setMontantTVA(BigDecimal montantTVA) {
        this.montantTVA = montantTVA;
    }

    public BigDecimal getMontantTTC() {
        return montantTTC;
    }

    public void setMontantTTC(BigDecimal montantTTC) {
        this.montantTTC = montantTTC;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public FactureDTO getFacture() {
        return facture;
    }

    public void setFacture(FactureDTO facture) {
        this.facture = facture;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DetailFactureDTO)) {
            return false;
        }

        DetailFactureDTO detailFactureDTO = (DetailFactureDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, detailFactureDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DetailFactureDTO{" +
            "id=" + getId() +
            ", designation='" + getDesignation() + "'" +
            ", prixUnitaire=" + getPrixUnitaire() +
            ", quantite=" + getQuantite() +
            ", montantHT=" + getMontantHT() +
            ", tauxTVA=" + getTauxTVA() +
            ", montantTVA=" + getMontantTVA() +
            ", montantTTC=" + getMontantTTC() +
            ", observations='" + getObservations() + "'" +
            ", facture=" + getFacture() +
            "}";
    }
}
