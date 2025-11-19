package com.convention.service.dto;

import com.convention.domain.enumeration.StatutConvention;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.convention.domain.ConventionEntity} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConventionDTO implements Serializable {

    private Long id;

    @NotNull
    private Long numConvention;

    @NotNull
    private LocalDate dateSignConv;

    @NotNull
    private LocalDate dateDebutConv;

    @NotNull
    private LocalDate echeanceConv;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal redevance;

    @NotNull
    @Size(max = 100)
    private String nomResponsable;

    @NotNull
    private StatutConvention statut;

    private Instant dateCreation;

    private Instant dateModification;

    @NotNull
    private ClientDTO client;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumConvention() {
        return numConvention;
    }

    public void setNumConvention(Long numConvention) {
        this.numConvention = numConvention;
    }

    public LocalDate getDateSignConv() {
        return dateSignConv;
    }

    public void setDateSignConv(LocalDate dateSignConv) {
        this.dateSignConv = dateSignConv;
    }

    public LocalDate getDateDebutConv() {
        return dateDebutConv;
    }

    public void setDateDebutConv(LocalDate dateDebutConv) {
        this.dateDebutConv = dateDebutConv;
    }

    public LocalDate getEcheanceConv() {
        return echeanceConv;
    }

    public void setEcheanceConv(LocalDate echeanceConv) {
        this.echeanceConv = echeanceConv;
    }

    public BigDecimal getRedevance() {
        return redevance;
    }

    public void setRedevance(BigDecimal redevance) {
        this.redevance = redevance;
    }

    public String getNomResponsable() {
        return nomResponsable;
    }

    public void setNomResponsable(String nomResponsable) {
        this.nomResponsable = nomResponsable;
    }

    public StatutConvention getStatut() {
        return statut;
    }

    public void setStatut(StatutConvention statut) {
        this.statut = statut;
    }

    public Instant getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Instant getDateModification() {
        return dateModification;
    }

    public void setDateModification(Instant dateModification) {
        this.dateModification = dateModification;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConventionDTO)) {
            return false;
        }

        ConventionDTO conventionDTO = (ConventionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, conventionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConventionDTO{" +
            "id=" + getId() +
            ", numConvention=" + getNumConvention() +
            ", dateSignConv='" + getDateSignConv() + "'" +
            ", dateDebutConv='" + getDateDebutConv() + "'" +
            ", echeanceConv='" + getEcheanceConv() + "'" +
            ", redevance=" + getRedevance() +
            ", nomResponsable='" + getNomResponsable() + "'" +
            ", statut='" + getStatut() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateModification='" + getDateModification() + "'" +
            ", client=" + getClient() +
            "}";
    }
}
