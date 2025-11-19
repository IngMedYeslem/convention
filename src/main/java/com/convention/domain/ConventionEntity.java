package com.convention.domain;

import com.convention.domain.enumeration.StatutConvention;
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
    @Column(name = "echeance_conv", nullable = false)
    private LocalDate echeanceConv;

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

    public LocalDate getEcheanceConv() {
        return this.echeanceConv;
    }

    public ConventionEntity echeanceConv(LocalDate echeanceConv) {
        this.setEcheanceConv(echeanceConv);
        return this;
    }

    public void setEcheanceConv(LocalDate echeanceConv) {
        this.echeanceConv = echeanceConv;
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
            ", echeanceConv='" + getEcheanceConv() + "'" +
            ", redevance=" + getRedevance() +
            ", nomResponsable='" + getNomResponsable() + "'" +
            ", statut='" + getStatut() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateModification='" + getDateModification() + "'" +
            "}";
    }
}
