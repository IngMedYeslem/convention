package com.convention.service.dto;

import com.convention.domain.enumeration.StatutAvenant;
import com.convention.domain.enumeration.TypeAvenant;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

public class AvenantDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer numeroAvenant;

    @NotNull
    private LocalDate dateSignature;

    @NotNull
    private TypeAvenant typeAvenant;

    @NotNull
    @Size(max = 500)
    private String objet;

    private LocalDate nouvelleEcheance;

    @DecimalMin(value = "0")
    private BigDecimal montantAdditionnel;

    private String modificationsClauses;

    @NotNull
    private StatutAvenant statut;

    private Instant dateCreation;

    @NotNull
    private ConventionDTO convention;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumeroAvenant() {
        return numeroAvenant;
    }

    public void setNumeroAvenant(Integer numeroAvenant) {
        this.numeroAvenant = numeroAvenant;
    }

    public LocalDate getDateSignature() {
        return dateSignature;
    }

    public void setDateSignature(LocalDate dateSignature) {
        this.dateSignature = dateSignature;
    }

    public TypeAvenant getTypeAvenant() {
        return typeAvenant;
    }

    public void setTypeAvenant(TypeAvenant typeAvenant) {
        this.typeAvenant = typeAvenant;
    }

    public String getObjet() {
        return objet;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public LocalDate getNouvelleEcheance() {
        return nouvelleEcheance;
    }

    public void setNouvelleEcheance(LocalDate nouvelleEcheance) {
        this.nouvelleEcheance = nouvelleEcheance;
    }

    public BigDecimal getMontantAdditionnel() {
        return montantAdditionnel;
    }

    public void setMontantAdditionnel(BigDecimal montantAdditionnel) {
        this.montantAdditionnel = montantAdditionnel;
    }

    public String getModificationsClauses() {
        return modificationsClauses;
    }

    public void setModificationsClauses(String modificationsClauses) {
        this.modificationsClauses = modificationsClauses;
    }

    public StatutAvenant getStatut() {
        return statut;
    }

    public void setStatut(StatutAvenant statut) {
        this.statut = statut;
    }

    public Instant getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Instant dateCreation) {
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
        if (this == o) return true;
        if (!(o instanceof AvenantDTO)) return false;
        AvenantDTO that = (AvenantDTO) o;
        if (this.id == null) return false;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}
