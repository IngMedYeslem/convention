package com.convention.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ClientCriteriaTest {

    @Test
    void newClientCriteriaHasAllFiltersNullTest() {
        var clientEntityCriteria = new ClientCriteria();
        assertThat(clientEntityCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void clientEntityCriteriaFluentMethodsCreatesFiltersTest() {
        var clientEntityCriteria = new ClientCriteria();

        setAllFilters(clientEntityCriteria);

        assertThat(clientEntityCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void clientEntityCriteriaCopyCreatesNullFilterTest() {
        var clientEntityCriteria = new ClientCriteria();
        var copy = clientEntityCriteria.copy();

        assertThat(clientEntityCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(clientEntityCriteria)
        );
    }

    @Test
    void clientEntityCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var clientEntityCriteria = new ClientCriteria();
        setAllFilters(clientEntityCriteria);

        var copy = clientEntityCriteria.copy();

        assertThat(clientEntityCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(clientEntityCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var clientEntityCriteria = new ClientCriteria();

        assertThat(clientEntityCriteria).hasToString("ClientCriteria{}");
    }

    private static void setAllFilters(ClientCriteria clientEntityCriteria) {
        clientEntityCriteria.id();
        clientEntityCriteria.numClient();
        clientEntityCriteria.nomClient();
        clientEntityCriteria.adresseClient();
        clientEntityCriteria.emailClient();
        clientEntityCriteria.whatsClient();
        clientEntityCriteria.dateCreation();
        clientEntityCriteria.actif();
        clientEntityCriteria.distinct();
    }

    private static Condition<ClientCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNumClient()) &&
                condition.apply(criteria.getNomClient()) &&
                condition.apply(criteria.getAdresseClient()) &&
                condition.apply(criteria.getEmailClient()) &&
                condition.apply(criteria.getWhatsClient()) &&
                condition.apply(criteria.getDateCreation()) &&
                condition.apply(criteria.getActif()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ClientCriteria> copyFiltersAre(ClientCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNumClient(), copy.getNumClient()) &&
                condition.apply(criteria.getNomClient(), copy.getNomClient()) &&
                condition.apply(criteria.getAdresseClient(), copy.getAdresseClient()) &&
                condition.apply(criteria.getEmailClient(), copy.getEmailClient()) &&
                condition.apply(criteria.getWhatsClient(), copy.getWhatsClient()) &&
                condition.apply(criteria.getDateCreation(), copy.getDateCreation()) &&
                condition.apply(criteria.getActif(), copy.getActif()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
