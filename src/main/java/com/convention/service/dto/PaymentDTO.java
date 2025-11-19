package com.convention.service.dto;

import com.convention.domain.enumeration.ModePaiement;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.convention.domain.PaymentEntity} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String numPaiement;

    @NotNull
    private LocalDate datePaiement;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal montant;

    @NotNull
    private ModePaiement modePaiement;

    @Size(max = 100)
    private String reference;

    @Size(max = 500)
    private String observations;

    private Instant dateCreation;

    @NotNull
    private FactureDTO facture;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumPaiement() {
        return numPaiement;
    }

    public void setNumPaiement(String numPaiement) {
        this.numPaiement = numPaiement;
    }

    public LocalDate getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(LocalDate datePaiement) {
        this.datePaiement = datePaiement;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public ModePaiement getModePaiement() {
        return modePaiement;
    }

    public void setModePaiement(ModePaiement modePaiement) {
        this.modePaiement = modePaiement;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public Instant getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
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
        if (!(o instanceof PaymentDTO)) {
            return false;
        }

        PaymentDTO paymentDTO = (PaymentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paymentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return (
            "PaymentDTO{" +
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
            ", facture=" +
            getFacture() +
            "}"
        );
    }
}
