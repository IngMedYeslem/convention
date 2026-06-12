package com.convention.domain;

import com.convention.domain.enumeration.EtapeApprobation;
import com.convention.domain.enumeration.PeriodeEcheance;
import com.convention.domain.enumeration.StatutConvention;
import com.convention.domain.enumeration.TypeConvention;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ConventionEntity.
 */
@Entity
@Table(name = "convention")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConventionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "num_convention", nullable = false, unique = true)
    private Long numConvention;

    @NotNull
    @Column(name = "date_sign_conv", nullable = false)
    private LocalDate dateSignConv;

    @NotNull
    @Column(name = "date_debut_conv", nullable = false)
    private LocalDate dateDebutConv;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "periode_echeance", nullable = false)
    private PeriodeEcheance periodeEcheance;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "redevance", precision = 21, scale = 2, nullable = false)
    private BigDecimal redevance;

    @NotNull
    @Size(max = 100)
    @Column(name = "nom_responsable", length = 100, nullable = false)
    private String nomResponsable;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private StatutConvention statut;

    @Column(name = "date_creation")
    private Instant dateCreation;

    @Column(name = "date_modification")
    private Instant dateModification;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_convention")
    private TypeConvention typeConvention;

    @Size(max = 500)
    @Column(name = "objet", length = 500)
    private String objet;

    @Size(max = 200)
    @Column(name = "direction_responsable", length = 200)
    private String directionResponsable;

    @Size(max = 200)
    @Column(name = "reference_juridique", length = 200)
    private String referenceJuridique;

    @Size(max = 100)
    @Column(name = "numero_engagement", length = 100)
    private String numeroEngagement;

    @Column(name = "date_visa_controleur")
    private LocalDate dateVisaControleur;

    @DecimalMin(value = "0")
    @Column(name = "valeur_totale", precision = 21, scale = 2)
    private java.math.BigDecimal valeurTotale;

    @Column(name = "renouvelable")
    private Boolean renouvelable;

    @Column(name = "nombre_renouvellements")
    private Integer nombreRenouvellements;

    @Lob
    @Column(name = "conditions_resiliation")
    private String conditionsResiliation;

    @Lob
    @Column(name = "penalites")
    private String penalites;

    @Enumerated(EnumType.STRING)
    @Column(name = "etape_approbation")
    private EtapeApprobation etapeApprobation;

    @Size(max = 500)
    @Column(name = "commentaire_rejet", length = 500)
    private String commentaireRejet;

    @ManyToOne(optional = false)
    @NotNull
    private ClientEntity client;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ConventionEntity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumConvention() {
        return this.numConvention;
    }

    public ConventionEntity numConvention(Long numConvention) {
        this.setNumConvention(numConvention);
        return this;
    }

    public void setNumConvention(Long numConvention) {
        this.numConvention = numConvention;
    }

    public LocalDate getDateSignConv() {
        return this.dateSignConv;
    }

    public ConventionEntity dateSignConv(LocalDate dateSignConv) {
        this.setDateSignConv(dateSignConv);
        return this;
    }

    public void setDateSignConv(LocalDate dateSignConv) {
        this.dateSignConv = dateSignConv;
    }

    public LocalDate getDateDebutConv() {
        return this.dateDebutConv;
    }

    public ConventionEntity dateDebutConv(LocalDate dateDebutConv) {
        this.setDateDebutConv(dateDebutConv);
        return this;
    }

    public void setDateDebutConv(LocalDate dateDebutConv) {
        this.dateDebutConv = dateDebutConv;
    }

    public PeriodeEcheance getPeriodeEcheance() {
        return this.periodeEcheance;
    }

    public ConventionEntity periodeEcheance(PeriodeEcheance periodeEcheance) {
        this.setPeriodeEcheance(periodeEcheance);
        return this;
    }

    public void setPeriodeEcheance(PeriodeEcheance periodeEcheance) {
        this.periodeEcheance = periodeEcheance;
    }

    public BigDecimal getRedevance() {
        return this.redevance;
    }

    public ConventionEntity redevance(BigDecimal redevance) {
        this.setRedevance(redevance);
        return this;
    }

    public void setRedevance(BigDecimal redevance) {
        this.redevance = redevance;
    }

    public String getNomResponsable() {
        return this.nomResponsable;
    }

    public ConventionEntity nomResponsable(String nomResponsable) {
        this.setNomResponsable(nomResponsable);
        return this;
    }

    public void setNomResponsable(String nomResponsable) {
        this.nomResponsable = nomResponsable;
    }

    public StatutConvention getStatut() {
        return this.statut;
    }

    public ConventionEntity statut(StatutConvention statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(StatutConvention statut) {
        this.statut = statut;
    }

    public Instant getDateCreation() {
        return this.dateCreation;
    }

    public ConventionEntity dateCreation(Instant dateCreation) {
        this.setDateCreation(dateCreation);
        return this;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Instant getDateModification() {
        return this.dateModification;
    }

    public ConventionEntity dateModification(Instant dateModification) {
        this.setDateModification(dateModification);
        return this;
    }

    public void setDateModification(Instant dateModification) {
        this.dateModification = dateModification;
    }

    public ClientEntity getClient() {
        return this.client;
    }

    public void setClient(ClientEntity client) {
        this.client = client;
    }

    public ConventionEntity client(ClientEntity client) {
        this.setClient(client);
        return this;
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

    public LocalDate getDateVisaControleur() {
        return dateVisaControleur;
    }

    public void setDateVisaControleur(LocalDate dateVisaControleur) {
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

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConventionEntity)) {
            return false;
        }
        return getId() != null && getId().equals(((ConventionEntity) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConventionEntity{" +
            "id=" + getId() +
            ", numConvention=" + getNumConvention() +
            ", dateSignConv='" + getDateSignConv() + "'" +
            ", dateDebutConv='" + getDateDebutConv() + "'" +
            ", periodeEcheance='" + getPeriodeEcheance() + "'" +
            ", redevance=" + getRedevance() +
            ", nomResponsable='" + getNomResponsable() + "'" +
            ", statut='" + getStatut() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateModification='" + getDateModification() + "'" +
            "}";
    }
}
