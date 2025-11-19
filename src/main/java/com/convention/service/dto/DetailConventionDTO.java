package com.convention.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.convention.domain.DetailConventionEntity} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DetailConventionDTO implements Serializable {

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

    private BigDecimal montantTotal;

    @Lob
    private String observations;

    private LocalDate dateCreation;

    @NotNull
    private ConventionDTO convention;

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

    public BigDecimal getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(BigDecimal montantTotal) {
        this.montantTotal = montantTotal;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public ConventionDTO getConvention() {
        return convention;
    }

    public void setConvention(ConventionDTO convention) {
        this.convention = convention;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DetailConventionDTO)) {
            return false;
        }

        DetailConventionDTO detailConventionDTO = (DetailConventionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, detailConventionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DetailConventionDTO{" +
            "id=" + getId() +
            ", designation='" + getDesignation() + "'" +
            ", prixUnitaire=" + getPrixUnitaire() +
            ", quantite=" + getQuantite() +
            ", montantTotal=" + getMontantTotal() +
            ", observations='" + getObservations() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", convention=" + getConvention() +
            "}";
    }
}
