package com.convention.domain;

import com.convention.domain.enumeration.ModePaiement;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PaymentEntity.
 */
@Entity
@Table(name = "payment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "num_paiement", length = 50, nullable = false)
    private String numPaiement;

    @NotNull
    @Column(name = "date_paiement", nullable = false)
    private LocalDate datePaiement;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "montant", precision = 21, scale = 2, nullable = false)
    private BigDecimal montant;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "mode_paiement", nullable = false)
    private ModePaiement modePaiement;

    @Size(max = 100)
    @Column(name = "reference", length = 100)
    private String reference;

    @Size(max = 500)
    @Column(name = "observations", length = 500)
    private String observations;

    @Column(name = "date_creation")
    private Instant dateCreation;

    @ManyToOne(optional = false)
    @NotNull
    private FactureEntity facture;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PaymentEntity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumPaiement() {
        return this.numPaiement;
    }

    public PaymentEntity numPaiement(String numPaiement) {
        this.setNumPaiement(numPaiement);
        return this;
    }

    public void setNumPaiement(String numPaiement) {
        this.numPaiement = numPaiement;
    }

    public LocalDate getDatePaiement() {
        return this.datePaiement;
    }

    public PaymentEntity datePaiement(LocalDate datePaiement) {
        this.setDatePaiement(datePaiement);
        return this;
    }

    public void setDatePaiement(LocalDate datePaiement) {
        this.datePaiement = datePaiement;
    }

    public BigDecimal getMontant() {
        return this.montant;
    }

    public PaymentEntity montant(BigDecimal montant) {
        this.setMontant(montant);
        return this;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public ModePaiement getModePaiement() {
        return this.modePaiement;
    }

    public PaymentEntity modePaiement(ModePaiement modePaiement) {
        this.setModePaiement(modePaiement);
        return this;
    }

    public void setModePaiement(ModePaiement modePaiement) {
        this.modePaiement = modePaiement;
    }

    public String getReference() {
        return this.reference;
    }

    public PaymentEntity reference(String reference) {
        this.setReference(reference);
        return this;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getObservations() {
        return this.observations;
    }

    public PaymentEntity observations(String observations) {
        this.setObservations(observations);
        return this;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public Instant getDateCreation() {
        return this.dateCreation;
    }

    public PaymentEntity dateCreation(Instant dateCreation) {
        this.setDateCreation(dateCreation);
        return this;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public FactureEntity getFacture() {
        return this.facture;
    }

    public void setFacture(FactureEntity facture) {
        this.facture = facture;
    }

    public PaymentEntity facture(FactureEntity facture) {
        this.setFacture(facture);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentEntity)) {
            return false;
        }
        return getId() != null && getId().equals(((PaymentEntity) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "PaymentEntity{" +
            "id=" +
            getId() +
            ", numPaiement='" +
            getNumPaiement() +
            "'" +
            ", datePaiement='" +
            getDatePaiement() +
            "'" +
            ", montant=" +
            getMontant() +
            ", modePaiement='" +
            getModePaiement() +
            "'" +
            ", reference='" +
            getReference() +
            "'" +
            ", observations='" +
            getObservations() +
            "'" +
            ", dateCreation='" +
            getDateCreation() +
            "'" +
            "}"
        );
    }
}
