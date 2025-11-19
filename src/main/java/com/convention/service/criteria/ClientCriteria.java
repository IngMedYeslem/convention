package com.convention.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.convention.domain.ClientEntity} entity. This class is used
 * in {@link com.convention.web.rest.ClientResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /clients?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClientCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter numClient;

    private StringFilter nomClient;

    private StringFilter adresseClient;

    private StringFilter emailClient;

    private StringFilter whatsClient;

    private InstantFilter dateCreation;

    private BooleanFilter actif;

    private Boolean distinct;

    public ClientCriteria() {}

    public ClientCriteria(ClientCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.numClient = other.optionalNumClient().map(LongFilter::copy).orElse(null);
        this.nomClient = other.optionalNomClient().map(StringFilter::copy).orElse(null);
        this.adresseClient = other.optionalAdresseClient().map(StringFilter::copy).orElse(null);
        this.emailClient = other.optionalEmailClient().map(StringFilter::copy).orElse(null);
        this.whatsClient = other.optionalWhatsClient().map(StringFilter::copy).orElse(null);
        this.dateCreation = other.optionalDateCreation().map(InstantFilter::copy).orElse(null);
        this.actif = other.optionalActif().map(BooleanFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ClientCriteria copy() {
        return new ClientCriteria(this);
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

    public LongFilter getNumClient() {
        return numClient;
    }

    public Optional<LongFilter> optionalNumClient() {
        return Optional.ofNullable(numClient);
    }

    public LongFilter numClient() {
        if (numClient == null) {
            setNumClient(new LongFilter());
        }
        return numClient;
    }

    public void setNumClient(LongFilter numClient) {
        this.numClient = numClient;
    }

    public StringFilter getNomClient() {
        return nomClient;
    }

    public Optional<StringFilter> optionalNomClient() {
        return Optional.ofNullable(nomClient);
    }

    public StringFilter nomClient() {
        if (nomClient == null) {
            setNomClient(new StringFilter());
        }
        return nomClient;
    }

    public void setNomClient(StringFilter nomClient) {
        this.nomClient = nomClient;
    }

    public StringFilter getAdresseClient() {
        return adresseClient;
    }

    public Optional<StringFilter> optionalAdresseClient() {
        return Optional.ofNullable(adresseClient);
    }

    public StringFilter adresseClient() {
        if (adresseClient == null) {
            setAdresseClient(new StringFilter());
        }
        return adresseClient;
    }

    public void setAdresseClient(StringFilter adresseClient) {
        this.adresseClient = adresseClient;
    }

    public StringFilter getEmailClient() {
        return emailClient;
    }

    public Optional<StringFilter> optionalEmailClient() {
        return Optional.ofNullable(emailClient);
    }

    public StringFilter emailClient() {
        if (emailClient == null) {
            setEmailClient(new StringFilter());
        }
        return emailClient;
    }

    public void setEmailClient(StringFilter emailClient) {
        this.emailClient = emailClient;
    }

    public StringFilter getWhatsClient() {
        return whatsClient;
    }

    public Optional<StringFilter> optionalWhatsClient() {
        return Optional.ofNullable(whatsClient);
    }

    public StringFilter whatsClient() {
        if (whatsClient == null) {
            setWhatsClient(new StringFilter());
        }
        return whatsClient;
    }

    public void setWhatsClient(StringFilter whatsClient) {
        this.whatsClient = whatsClient;
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

    public BooleanFilter getActif() {
        return actif;
    }

    public Optional<BooleanFilter> optionalActif() {
        return Optional.ofNullable(actif);
    }

    public BooleanFilter actif() {
        if (actif == null) {
            setActif(new BooleanFilter());
        }
        return actif;
    }

    public void setActif(BooleanFilter actif) {
        this.actif = actif;
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
        final ClientCriteria that = (ClientCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(numClient, that.numClient) &&
            Objects.equals(nomClient, that.nomClient) &&
            Objects.equals(adresseClient, that.adresseClient) &&
            Objects.equals(emailClient, that.emailClient) &&
            Objects.equals(whatsClient, that.whatsClient) &&
            Objects.equals(dateCreation, that.dateCreation) &&
            Objects.equals(actif, that.actif) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, numClient, nomClient, adresseClient, emailClient, whatsClient, dateCreation, actif, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClientCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNumClient().map(f -> "numClient=" + f + ", ").orElse("") +
            optionalNomClient().map(f -> "nomClient=" + f + ", ").orElse("") +
            optionalAdresseClient().map(f -> "adresseClient=" + f + ", ").orElse("") +
            optionalEmailClient().map(f -> "emailClient=" + f + ", ").orElse("") +
            optionalWhatsClient().map(f -> "whatsClient=" + f + ", ").orElse("") +
            optionalDateCreation().map(f -> "dateCreation=" + f + ", ").orElse("") +
            optionalActif().map(f -> "actif=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
