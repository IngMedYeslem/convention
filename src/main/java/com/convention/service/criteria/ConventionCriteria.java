package com.convention.service.criteria;

import com.convention.domain.enumeration.StatutConvention;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.convention.domain.ConventionEntity} entity. This class is used
 * in {@link com.convention.web.rest.ConventionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /conventions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConventionCriteria implements Serializable, Criteria {

    /**
     * Class for filtering StatutConvention
     */
    public static class StatutConventionFilter extends Filter<StatutConvention> {

        public StatutConventionFilter() {}

        public StatutConventionFilter(StatutConventionFilter filter) {
            super(filter);
        }

        @Override
        public StatutConventionFilter copy() {
            return new StatutConventionFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter numConvention;

    private LocalDateFilter dateSignConv;

    private LocalDateFilter dateDebutConv;

    private LocalDateFilter echeanceConv;

    private BigDecimalFilter redevance;

    private StringFilter nomResponsable;

    private StatutConventionFilter statut;

    private InstantFilter dateCreation;

    private InstantFilter dateModification;

    private LongFilter clientId;

    private Boolean distinct;

    public ConventionCriteria() {}

    public ConventionCriteria(ConventionCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.numConvention = other.optionalNumConvention().map(LongFilter::copy).orElse(null);
        this.dateSignConv = other.optionalDateSignConv().map(LocalDateFilter::copy).orElse(null);
        this.dateDebutConv = other.optionalDateDebutConv().map(LocalDateFilter::copy).orElse(null);
        this.echeanceConv = other.optionalEcheanceConv().map(LocalDateFilter::copy).orElse(null);
        this.redevance = other.optionalRedevance().map(BigDecimalFilter::copy).orElse(null);
        this.nomResponsable = other.optionalNomResponsable().map(StringFilter::copy).orElse(null);
        this.statut = other.optionalStatut().map(StatutConventionFilter::copy).orElse(null);
        this.dateCreation = other.optionalDateCreation().map(InstantFilter::copy).orElse(null);
        this.dateModification = other.optionalDateModification().map(InstantFilter::copy).orElse(null);
        this.clientId = other.optionalClientId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ConventionCriteria copy() {
        return new ConventionCriteria(this);
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

    public LongFilter getNumConvention() {
        return numConvention;
    }

    public Optional<LongFilter> optionalNumConvention() {
        return Optional.ofNullable(numConvention);
    }

    public LongFilter numConvention() {
        if (numConvention == null) {
            setNumConvention(new LongFilter());
        }
        return numConvention;
    }

    public void setNumConvention(LongFilter numConvention) {
        this.numConvention = numConvention;
    }

    public LocalDateFilter getDateSignConv() {
        return dateSignConv;
    }

    public Optional<LocalDateFilter> optionalDateSignConv() {
        return Optional.ofNullable(dateSignConv);
    }

    public LocalDateFilter dateSignConv() {
        if (dateSignConv == null) {
            setDateSignConv(new LocalDateFilter());
        }
        return dateSignConv;
    }

    public void setDateSignConv(LocalDateFilter dateSignConv) {
        this.dateSignConv = dateSignConv;
    }

    public LocalDateFilter getDateDebutConv() {
        return dateDebutConv;
    }

    public Optional<LocalDateFilter> optionalDateDebutConv() {
        return Optional.ofNullable(dateDebutConv);
    }

    public LocalDateFilter dateDebutConv() {
        if (dateDebutConv == null) {
            setDateDebutConv(new LocalDateFilter());
        }
        return dateDebutConv;
    }

    public void setDateDebutConv(LocalDateFilter dateDebutConv) {
        this.dateDebutConv = dateDebutConv;
    }

    public LocalDateFilter getEcheanceConv() {
        return echeanceConv;
    }

    public Optional<LocalDateFilter> optionalEcheanceConv() {
        return Optional.ofNullable(echeanceConv);
    }

    public LocalDateFilter echeanceConv() {
        if (echeanceConv == null) {
            setEcheanceConv(new LocalDateFilter());
        }
        return echeanceConv;
    }

    public void setEcheanceConv(LocalDateFilter echeanceConv) {
        this.echeanceConv = echeanceConv;
    }

    public BigDecimalFilter getRedevance() {
        return redevance;
    }

    public Optional<BigDecimalFilter> optionalRedevance() {
        return Optional.ofNullable(redevance);
    }

    public BigDecimalFilter redevance() {
        if (redevance == null) {
            setRedevance(new BigDecimalFilter());
        }
        return redevance;
    }

    public void setRedevance(BigDecimalFilter redevance) {
        this.redevance = redevance;
    }

    public StringFilter getNomResponsable() {
        return nomResponsable;
    }

    public Optional<StringFilter> optionalNomResponsable() {
        return Optional.ofNullable(nomResponsable);
    }

    public StringFilter nomResponsable() {
        if (nomResponsable == null) {
            setNomResponsable(new StringFilter());
        }
        return nomResponsable;
    }

    public void setNomResponsable(StringFilter nomResponsable) {
        this.nomResponsable = nomResponsable;
    }

    public StatutConventionFilter getStatut() {
        return statut;
    }

    public Optional<StatutConventionFilter> optionalStatut() {
        return Optional.ofNullable(statut);
    }

    public StatutConventionFilter statut() {
        if (statut == null) {
            setStatut(new StatutConventionFilter());
        }
        return statut;
    }

    public void setStatut(StatutConventionFilter statut) {
        this.statut = statut;
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

    public InstantFilter getDateModification() {
        return dateModification;
    }

    public Optional<InstantFilter> optionalDateModification() {
        return Optional.ofNullable(dateModification);
    }

    public InstantFilter dateModification() {
        if (dateModification == null) {
            setDateModification(new InstantFilter());
        }
        return dateModification;
    }

    public void setDateModification(InstantFilter dateModification) {
        this.dateModification = dateModification;
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
        final ConventionCriteria that = (ConventionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(numConvention, that.numConvention) &&
            Objects.equals(dateSignConv, that.dateSignConv) &&
            Objects.equals(dateDebutConv, that.dateDebutConv) &&
            Objects.equals(echeanceConv, that.echeanceConv) &&
            Objects.equals(redevance, that.redevance) &&
            Objects.equals(nomResponsable, that.nomResponsable) &&
            Objects.equals(statut, that.statut) &&
            Objects.equals(dateCreation, that.dateCreation) &&
            Objects.equals(dateModification, that.dateModification) &&
            Objects.equals(clientId, that.clientId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            numConvention,
            dateSignConv,
            dateDebutConv,
            echeanceConv,
            redevance,
            nomResponsable,
            statut,
            dateCreation,
            dateModification,
            clientId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConventionCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNumConvention().map(f -> "numConvention=" + f + ", ").orElse("") +
            optionalDateSignConv().map(f -> "dateSignConv=" + f + ", ").orElse("") +
            optionalDateDebutConv().map(f -> "dateDebutConv=" + f + ", ").orElse("") +
            optionalEcheanceConv().map(f -> "echeanceConv=" + f + ", ").orElse("") +
            optionalRedevance().map(f -> "redevance=" + f + ", ").orElse("") +
            optionalNomResponsable().map(f -> "nomResponsable=" + f + ", ").orElse("") +
            optionalStatut().map(f -> "statut=" + f + ", ").orElse("") +
            optionalDateCreation().map(f -> "dateCreation=" + f + ", ").orElse("") +
            optionalDateModification().map(f -> "dateModification=" + f + ", ").orElse("") +
            optionalClientId().map(f -> "clientId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
