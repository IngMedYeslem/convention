package com.convention.service.dto;

import com.convention.domain.enumeration.StatutFacture;
import com.convention.domain.enumeration.TypeFacture;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.convention.domain.FactureEntity} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FactureDTO implements Serializable {

    private Long id;

    @NotNull
    private Long numFacture;

    @NotNull
    private LocalDate dateFacture;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal montantTotal;

    private BigDecimal montantTTC;

    @DecimalMin(value = "0")
    @DecimalMax(value = "100")
    private BigDecimal tva;

    @Lob
    private String observations;

    @Size(max = 50)
    private String ancienneRef;

    @NotNull
    private TypeFacture typeFacture;

    @NotNull
    private StatutFacture statut;

    private LocalDate dateEcheance;

    private Instant dateCreation;

    @NotNull
    private ClientDTO client;

    private ConventionDTO convention;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumFacture() {
        return numFacture;
    }

    public void setNumFacture(Long numFacture) {
        this.numFacture = numFacture;
    }

    public LocalDate getDateFacture() {
        return dateFacture;
    }

    public void setDateFacture(LocalDate dateFacture) {
        this.dateFacture = dateFacture;
    }

    public BigDecimal getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(BigDecimal montantTotal) {
        this.montantTotal = montantTotal;
    }

    public BigDecimal getMontantTTC() {
        return montantTTC;
    }

    public void setMontantTTC(BigDecimal montantTTC) {
        this.montantTTC = montantTTC;
    }

    public BigDecimal getTva() {
        return tva;
    }

    public void setTva(BigDecimal tva) {
        this.tva = tva;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public String getAncienneRef() {
        return ancienneRef;
    }

    public void setAncienneRef(String ancienneRef) {
        this.ancienneRef = ancienneRef;
    }

    public TypeFacture getTypeFacture() {
        return typeFacture;
    }

    public void setTypeFacture(TypeFacture typeFacture) {
        this.typeFacture = typeFacture;
    }

    public StatutFacture getStatut() {
        return statut;
    }

    public void setStatut(StatutFacture statut) {
        this.statut = statut;
    }

    public LocalDate getDateEcheance() {
        return dateEcheance;
    }

    public void setDateEcheance(LocalDate dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public Instant getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
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
        if (!(o instanceof FactureDTO)) {
            return false;
        }

        FactureDTO factureDTO = (FactureDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, factureDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FactureDTO{" +
            "id=" + getId() +
            ", numFacture=" + getNumFacture() +
            ", dateFacture='" + getDateFacture() + "'" +
            ", montantTotal=" + getMontantTotal() +
            ", montantTTC=" + getMontantTTC() +
            ", tva=" + getTva() +
            ", observations='" + getObservations() + "'" +
            ", ancienneRef='" + getAncienneRef() + "'" +
            ", typeFacture='" + getTypeFacture() + "'" +
            ", statut='" + getStatut() + "'" +
            ", dateEcheance='" + getDateEcheance() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", client=" + getClient() +
            ", convention=" + getConvention() +
            "}";
    }
}
