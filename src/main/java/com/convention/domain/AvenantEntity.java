package com.convention.domain;

import com.convention.domain.enumeration.StatutAvenant;
import com.convention.domain.enumeration.TypeAvenant;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "avenant")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AvenantEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "numero_avenant", nullable = false)
    private Integer numeroAvenant;

    @NotNull
    @Column(name = "date_signature", nullable = false)
    private LocalDate dateSignature;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type_avenant", nullable = false)
    private TypeAvenant typeAvenant;

    @NotNull
    @Size(max = 500)
    @Column(name = "objet", length = 500, nullable = false)
    private String objet;

    @Column(name = "nouvelle_echeance")
    private LocalDate nouvelleEcheance;

    @DecimalMin(value = "0")
    @Column(name = "montant_additionnel", precision = 21, scale = 2)
    private BigDecimal montantAdditionnel;

    @Lob
    @Column(name = "modifications_clauses")
    private String modificationsClauses;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private StatutAvenant statut;

    @Column(name = "date_creation")
    private Instant dateCreation;

    @ManyToOne(optional = false)
    @NotNull
    private ConventionEntity convention;

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

    public ConventionEntity getConvention() {
        return convention;
    }

    public void setConvention(ConventionEntity convention) {
        this.convention = convention;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AvenantEntity)) return false;
        return getId() != null && getId().equals(((AvenantEntity) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
