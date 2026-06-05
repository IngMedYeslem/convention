package com.convention.service.dto;

import com.convention.domain.enumeration.EtapeApprobation;
import com.convention.domain.enumeration.StatutConvention;
import com.convention.domain.enumeration.TypeConvention;
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

    private TypeConvention typeConvention;

    @Size(max = 500)
    private String objet;

    @Size(max = 200)
    private String directionResponsable;

    @Size(max = 200)
    private String referenceJuridique;

    @Size(max = 100)
    private String numeroEngagement;

    private java.time.LocalDate dateVisaControleur;

    @DecimalMin(value = "0")
    private java.math.BigDecimal valeurTotale;

    private Boolean renouvelable;

    private Integer nombreRenouvellements;

    private String conditionsResiliation;

    private String penalites;

    private EtapeApprobation etapeApprobation;

    @Size(max = 500)
    private String commentaireRejet;

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

    public TypeConvention getTypeConvention() {
        return typeConvention;
    }

    public void setTypeConvention(TypeConvention typeConvention) {
        this.typeConvention = typeConvention;
    }

    public String getObjet() {
        return objet;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public String getDirectionResponsable() {
        return directionResponsable;
    }

    public void setDirectionResponsable(String directionResponsable) {
        this.directionResponsable = directionResponsable;
    }

    public String getReferenceJuridique() {
        return referenceJuridique;
    }

    public void setReferenceJuridique(String referenceJuridique) {
        this.referenceJuridique = referenceJuridique;
    }

    public String getNumeroEngagement() {
        return numeroEngagement;
    }

    public void setNumeroEngagement(String numeroEngagement) {
        this.numeroEngagement = numeroEngagement;
    }

    public java.time.LocalDate getDateVisaControleur() {
        return dateVisaControleur;
    }

    public void setDateVisaControleur(java.time.LocalDate dateVisaControleur) {
        this.dateVisaControleur = dateVisaControleur;
    }

    public java.math.BigDecimal getValeurTotale() {
        return valeurTotale;
    }

    public void setValeurTotale(java.math.BigDecimal valeurTotale) {
        this.valeurTotale = valeurTotale;
    }

    public Boolean getRenouvelable() {
        return renouvelable;
    }

    public void setRenouvelable(Boolean renouvelable) {
        this.renouvelable = renouvelable;
    }

    public Integer getNombreRenouvellements() {
        return nombreRenouvellements;
    }

    public void setNombreRenouvellements(Integer nombreRenouvellements) {
        this.nombreRenouvellements = nombreRenouvellements;
    }

    public String getConditionsResiliation() {
        return conditionsResiliation;
    }

    public void setConditionsResiliation(String conditionsResiliation) {
        this.conditionsResiliation = conditionsResiliation;
    }

    public String getPenalites() {
        return penalites;
    }

    public void setPenalites(String penalites) {
        this.penalites = penalites;
    }

    public EtapeApprobation getEtapeApprobation() {
        return etapeApprobation;
    }

    public void setEtapeApprobation(EtapeApprobation etapeApprobation) {
        this.etapeApprobation = etapeApprobation;
    }

    public String getCommentaireRejet() {
        return commentaireRejet;
    }

    public void setCommentaireRejet(String commentaireRejet) {
        this.commentaireRejet = commentaireRejet;
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
