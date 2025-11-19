package com.convention.service.criteria;

import com.convention.domain.enumeration.StatutFacture;
import com.convention.domain.enumeration.TypeFacture;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.convention.domain.FactureEntity} entity. This class is used
 * in {@link com.convention.web.rest.FactureResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /factures?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FactureCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TypeFacture
     */
    public static class TypeFactureFilter extends Filter<TypeFacture> {

        public TypeFactureFilter() {}

        public TypeFactureFilter(TypeFactureFilter filter) {
            super(filter);
        }

        @Override
        public TypeFactureFilter copy() {
            return new TypeFactureFilter(this);
        }
    }

    /**
     * Class for filtering StatutFacture
     */
    public static class StatutFactureFilter extends Filter<StatutFacture> {

        public StatutFactureFilter() {}

        public StatutFactureFilter(StatutFactureFilter filter) {
            super(filter);
        }

        @Override
        public StatutFactureFilter copy() {
            return new StatutFactureFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter numFacture;

    private LocalDateFilter dateFacture;

    private BigDecimalFilter montantTotal;

    private BigDecimalFilter montantTTC;

    private BigDecimalFilter tva;

    private StringFilter ancienneRef;

    private TypeFactureFilter typeFacture;

    private StatutFactureFilter statut;

    private LocalDateFilter dateEcheance;

    private InstantFilter dateCreation;

    private LongFilter clientId;

    private LongFilter conventionId;

    private Boolean distinct;

    public FactureCriteria() {}

    public FactureCriteria(FactureCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.numFacture = other.optionalNumFacture().map(LongFilter::copy).orElse(null);
        this.dateFacture = other.optionalDateFacture().map(LocalDateFilter::copy).orElse(null);
        this.montantTotal = other.optionalMontantTotal().map(BigDecimalFilter::copy).orElse(null);
        this.montantTTC = other.optionalMontantTTC().map(BigDecimalFilter::copy).orElse(null);
        this.tva = other.optionalTva().map(BigDecimalFilter::copy).orElse(null);
        this.ancienneRef = other.optionalAncienneRef().map(StringFilter::copy).orElse(null);
        this.typeFacture = other.optionalTypeFacture().map(TypeFactureFilter::copy).orElse(null);
        this.statut = other.optionalStatut().map(StatutFactureFilter::copy).orElse(null);
        this.dateEcheance = other.optionalDateEcheance().map(LocalDateFilter::copy).orElse(null);
        this.dateCreation = other.optionalDateCreation().map(InstantFilter::copy).orElse(null);
        this.clientId = other.optionalClientId().map(LongFilter::copy).orElse(null);
        this.conventionId = other.optionalConventionId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public FactureCriteria copy() {
        return new FactureCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getNumFacture() {
        return numFacture;
    }

    public Optional<LongFilter> optionalNumFacture() {
        return Optional.ofNullable(numFacture);
    }

    public LongFilter numFacture() {
        if (numFacture == null) {
            setNumFacture(new LongFilter());
        }
        return numFacture;
    }

    public void setNumFacture(LongFilter numFacture) {
        this.numFacture = numFacture;
    }

    public LocalDateFilter getDateFacture() {
        return dateFacture;
    }

    public Optional<LocalDateFilter> optionalDateFacture() {
        return Optional.ofNullable(dateFacture);
    }

    public LocalDateFilter dateFacture() {
        if (dateFacture == null) {
            setDateFacture(new LocalDateFilter());
        }
        return dateFacture;
    }

    public void setDateFacture(LocalDateFilter dateFacture) {
        this.dateFacture = dateFacture;
    }

    public BigDecimalFilter getMontantTotal() {
        return montantTotal;
    }

    public Optional<BigDecimalFilter> optionalMontantTotal() {
        return Optional.ofNullable(montantTotal);
    }

    public BigDecimalFilter montantTotal() {
        if (montantTotal == null) {
            setMontantTotal(new BigDecimalFilter());
        }
        return montantTotal;
    }

    public void setMontantTotal(BigDecimalFilter montantTotal) {
        this.montantTotal = montantTotal;
    }

    public BigDecimalFilter getMontantTTC() {
        return montantTTC;
    }

    public Optional<BigDecimalFilter> optionalMontantTTC() {
        return Optional.ofNullable(montantTTC);
    }

    public BigDecimalFilter montantTTC() {
        if (montantTTC == null) {
            setMontantTTC(new BigDecimalFilter());
        }
        return montantTTC;
    }

    public void setMontantTTC(BigDecimalFilter montantTTC) {
        this.montantTTC = montantTTC;
    }

    public BigDecimalFilter getTva() {
        return tva;
    }

    public Optional<BigDecimalFilter> optionalTva() {
        return Optional.ofNullable(tva);
    }

    public BigDecimalFilter tva() {
        if (tva == null) {
            setTva(new BigDecimalFilter());
        }
        return tva;
    }

    public void setTva(BigDecimalFilter tva) {
        this.tva = tva;
    }

    public StringFilter getAncienneRef() {
        return ancienneRef;
    }

    public Optional<StringFilter> optionalAncienneRef() {
        return Optional.ofNullable(ancienneRef);
    }

    public StringFilter ancienneRef() {
        if (ancienneRef == null) {
            setAncienneRef(new StringFilter());
        }
        return ancienneRef;
    }

    public void setAncienneRef(StringFilter ancienneRef) {
        this.ancienneRef = ancienneRef;
    }

    public TypeFactureFilter getTypeFacture() {
        return typeFacture;
    }

    public Optional<TypeFactureFilter> optionalTypeFacture() {
        return Optional.ofNullable(typeFacture);
    }

    public TypeFactureFilter typeFacture() {
        if (typeFacture == null) {
            setTypeFacture(new TypeFactureFilter());
        }
        return typeFacture;
    }

    public void setTypeFacture(TypeFactureFilter typeFacture) {
        this.typeFacture = typeFacture;
    }

    public StatutFactureFilter getStatut() {
        return statut;
    }

    public Optional<StatutFactureFilter> optionalStatut() {
        return Optional.ofNullable(statut);
    }

    public StatutFactureFilter statut() {
        if (statut == null) {
            setStatut(new StatutFactureFilter());
        }
        return statut;
    }

    public void setStatut(StatutFactureFilter statut) {
        this.statut = statut;
    }

    public LocalDateFilter getDateEcheance() {
        return dateEcheance;
    }

    public Optional<LocalDateFilter> optionalDateEcheance() {
        return Optional.ofNullable(dateEcheance);
    }

    public LocalDateFilter dateEcheance() {
        if (dateEcheance == null) {
            setDateEcheance(new LocalDateFilter());
        }
        return dateEcheance;
    }

    public void setDateEcheance(LocalDateFilter dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public InstantFilter getDateCreation() {
        return dateCreation;
    }

    public Optional<InstantFilter> optionalDateCreation() {
        return Optional.ofNullable(dateCreation);
    }

    public InstantFilter dateCreation() {
        if (dateCreation == null) {
            setDateCreation(new InstantFilter());
        }
        return dateCreation;
    }

    public void setDateCreation(InstantFilter dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LongFilter getClientId() {
        return clientId;
    }

    public Optional<LongFilter> optionalClientId() {
        return Optional.ofNullable(clientId);
    }

    public LongFilter clientId() {
        if (clientId == null) {
            setClientId(new LongFilter());
        }
        return clientId;
    }

    public void setClientId(LongFilter clientId) {
        this.clientId = clientId;
    }

    public LongFilter getConventionId() {
        return conventionId;
    }

    public Optional<LongFilter> optionalConventionId() {
        return Optional.ofNullable(conventionId);
    }

    public LongFilter conventionId() {
        if (conventionId == null) {
            setConventionId(new LongFilter());
        }
        return conventionId;
    }

    public void setConventionId(LongFilter conventionId) {
        this.conventionId = conventionId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FactureCriteria that = (FactureCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(numFacture, that.numFacture) &&
            Objects.equals(dateFacture, that.dateFacture) &&
            Objects.equals(montantTotal, that.montantTotal) &&
            Objects.equals(montantTTC, that.montantTTC) &&
            Objects.equals(tva, that.tva) &&
            Objects.equals(ancienneRef, that.ancienneRef) &&
            Objects.equals(typeFacture, that.typeFacture) &&
            Objects.equals(statut, that.statut) &&
            Objects.equals(dateEcheance, that.dateEcheance) &&
            Objects.equals(dateCreation, that.dateCreation) &&
            Objects.equals(clientId, that.clientId) &&
            Objects.equals(conventionId, that.conventionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            numFacture,
            dateFacture,
            montantTotal,
            montantTTC,
            tva,
            ancienneRef,
            typeFacture,
            statut,
            dateEcheance,
            dateCreation,
            clientId,
            conventionId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FactureCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNumFacture().map(f -> "numFacture=" + f + ", ").orElse("") +
            optionalDateFacture().map(f -> "dateFacture=" + f + ", ").orElse("") +
            optionalMontantTotal().map(f -> "montantTotal=" + f + ", ").orElse("") +
            optionalMontantTTC().map(f -> "montantTTC=" + f + ", ").orElse("") +
            optionalTva().map(f -> "tva=" + f + ", ").orElse("") +
            optionalAncienneRef().map(f -> "ancienneRef=" + f + ", ").orElse("") +
            optionalTypeFacture().map(f -> "typeFacture=" + f + ", ").orElse("") +
            optionalStatut().map(f -> "statut=" + f + ", ").orElse("") +
            optionalDateEcheance().map(f -> "dateEcheance=" + f + ", ").orElse("") +
            optionalDateCreation().map(f -> "dateCreation=" + f + ", ").orElse("") +
            optionalClientId().map(f -> "clientId=" + f + ", ").orElse("") +
            optionalConventionId().map(f -> "conventionId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
