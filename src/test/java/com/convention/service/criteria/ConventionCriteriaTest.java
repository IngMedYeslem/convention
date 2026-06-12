package com.convention.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ConventionCriteriaTest {

    @Test
    void newConventionCriteriaHasAllFiltersNullTest() {
        var conventionEntityCriteria = new ConventionCriteria();
        assertThat(conventionEntityCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void conventionEntityCriteriaFluentMethodsCreatesFiltersTest() {
        var conventionEntityCriteria = new ConventionCriteria();

        setAllFilters(conventionEntityCriteria);

        assertThat(conventionEntityCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void conventionEntityCriteriaCopyCreatesNullFilterTest() {
        var conventionEntityCriteria = new ConventionCriteria();
        var copy = conventionEntityCriteria.copy();

        assertThat(conventionEntityCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(conventionEntityCriteria)
        );
    }

    @Test
    void conventionEntityCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var conventionEntityCriteria = new ConventionCriteria();
        setAllFilters(conventionEntityCriteria);

        var copy = conventionEntityCriteria.copy();

        assertThat(conventionEntityCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(conventionEntityCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var conventionEntityCriteria = new ConventionCriteria();

        assertThat(conventionEntityCriteria).hasToString("ConventionCriteria{}");
    }

    private static void setAllFilters(ConventionCriteria conventionEntityCriteria) {
        conventionEntityCriteria.id();
        conventionEntityCriteria.numConvention();
        conventionEntityCriteria.dateSignConv();
        conventionEntityCriteria.dateDebutConv();
        conventionEntityCriteria.periodeEcheance();
        conventionEntityCriteria.redevance();
        conventionEntityCriteria.nomResponsable();
        conventionEntityCriteria.statut();
        conventionEntityCriteria.dateCreation();
        conventionEntityCriteria.dateModification();
        conventionEntityCriteria.clientId();
        conventionEntityCriteria.distinct();
    }

    private static Condition<ConventionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNumConvention()) &&
                condition.apply(criteria.getDateSignConv()) &&
                condition.apply(criteria.getDateDebutConv()) &&
                condition.apply(criteria.getPeriodeEcheance()) &&
                condition.apply(criteria.getRedevance()) &&
                condition.apply(criteria.getNomResponsable()) &&
                condition.apply(criteria.getStatut()) &&
                condition.apply(criteria.getDateCreation()) &&
                condition.apply(criteria.getDateModification()) &&
                condition.apply(criteria.getClientId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ConventionCriteria> copyFiltersAre(ConventionCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNumConvention(), copy.getNumConvention()) &&
                condition.apply(criteria.getDateSignConv(), copy.getDateSignConv()) &&
                condition.apply(criteria.getDateDebutConv(), copy.getDateDebutConv()) &&
                condition.apply(criteria.getPeriodeEcheance(), copy.getPeriodeEcheance()) &&
                condition.apply(criteria.getRedevance(), copy.getRedevance()) &&
                condition.apply(criteria.getNomResponsable(), copy.getNomResponsable()) &&
                condition.apply(criteria.getStatut(), copy.getStatut()) &&
                condition.apply(criteria.getDateCreation(), copy.getDateCreation()) &&
                condition.apply(criteria.getDateModification(), copy.getDateModification()) &&
                condition.apply(criteria.getClientId(), copy.getClientId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
