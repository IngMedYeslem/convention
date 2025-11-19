package com.convention.domain;

import com.convention.domain.enumeration.StatutFacture;
import com.convention.domain.enumeration.TypeFacture;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FactureEntity.
 */
@Entity
@Table(name = "facture")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FactureEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "num_facture", nullable = false, unique = true)
    private Long numFacture;

    @NotNull
    @Column(name = "date_facture", nullable = false)
    private LocalDate dateFacture;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "montant_total", precision = 21, scale = 2, nullable = false)
    private BigDecimal montantTotal;

    @Column(name = "montant_ttc", precision = 21, scale = 2)
    private BigDecimal montantTTC;

    @DecimalMin(value = "0")
    @DecimalMax(value = "100")
    @Column(name = "tva", precision = 21, scale = 2)
    private BigDecimal tva;

    @Lob
    @Column(name = "observations")
    private String observations;

    @Size(max = 50)
    @Column(name = "ancienne_ref", length = 50)
    private String ancienneRef;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type_facture", nullable = false)
    private TypeFacture typeFacture;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private StatutFacture statut;

    @Column(name = "date_echeance")
    private LocalDate dateEcheance;

    @Column(name = "date_creation")
    private Instant dateCreation;

    @ManyToOne(optional = false)
    @NotNull
    private ClientEntity client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "client" }, allowSetters = true)
    private ConventionEntity convention;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FactureEntity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumFacture() {
        return this.numFacture;
    }

    public FactureEntity numFacture(Long numFacture) {
        this.setNumFacture(numFacture);
        return this;
    }

    public void setNumFacture(Long numFacture) {
        this.numFacture = numFacture;
    }

    public LocalDate getDateFacture() {
        return this.dateFacture;
    }

    public FactureEntity dateFacture(LocalDate dateFacture) {
        this.setDateFacture(dateFacture);
        return this;
    }

    public void setDateFacture(LocalDate dateFacture) {
        this.dateFacture = dateFacture;
    }

    public BigDecimal getMontantTotal() {
        return this.montantTotal;
    }

    public FactureEntity montantTotal(BigDecimal montantTotal) {
        this.setMontantTotal(montantTotal);
        return this;
    }

    public void setMontantTotal(BigDecimal montantTotal) {
        this.montantTotal = montantTotal;
    }

    public BigDecimal getMontantTTC() {
        return this.montantTTC;
    }

    public FactureEntity montantTTC(BigDecimal montantTTC) {
        this.setMontantTTC(montantTTC);
        return this;
    }

    public void setMontantTTC(BigDecimal montantTTC) {
        this.montantTTC = montantTTC;
    }

    public BigDecimal getTva() {
        return this.tva;
    }

    public FactureEntity tva(BigDecimal tva) {
        this.setTva(tva);
        return this;
    }

    public void setTva(BigDecimal tva) {
        this.tva = tva;
    }

    public String getObservations() {
        return this.observations;
    }

    public FactureEntity observations(String observations) {
        this.setObservations(observations);
        return this;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public String getAncienneRef() {
        return this.ancienneRef;
    }

    public FactureEntity ancienneRef(String ancienneRef) {
        this.setAncienneRef(ancienneRef);
        return this;
    }

    public void setAncienneRef(String ancienneRef) {
        this.ancienneRef = ancienneRef;
    }

    public TypeFacture getTypeFacture() {
        return this.typeFacture;
    }

    public FactureEntity typeFacture(TypeFacture typeFacture) {
        this.setTypeFacture(typeFacture);
        return this;
    }

    public void setTypeFacture(TypeFacture typeFacture) {
        this.typeFacture = typeFacture;
    }

    public StatutFacture getStatut() {
        return this.statut;
    }

    public FactureEntity statut(StatutFacture statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(StatutFacture statut) {
        this.statut = statut;
    }

    public LocalDate getDateEcheance() {
        return this.dateEcheance;
    }

    public FactureEntity dateEcheance(LocalDate dateEcheance) {
        this.setDateEcheance(dateEcheance);
        return this;
    }

    public void setDateEcheance(LocalDate dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public Instant getDateCreation() {
        return this.dateCreation;
    }

    public FactureEntity dateCreation(Instant dateCreation) {
        this.setDateCreation(dateCreation);
        return this;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public ClientEntity getClient() {
        return this.client;
    }

    public void setClient(ClientEntity client) {
        this.client = client;
    }

    public FactureEntity client(ClientEntity client) {
        this.setClient(client);
        return this;
    }

    public ConventionEntity getConvention() {
        return this.convention;
    }

    public void setConvention(ConventionEntity convention) {
        this.convention = convention;
    }

    public FactureEntity convention(ConventionEntity convention) {
        this.setConvention(convention);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FactureEntity)) {
            return false;
        }
        return getId() != null && getId().equals(((FactureEntity) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FactureEntity{" +
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
            "}";
    }
}
