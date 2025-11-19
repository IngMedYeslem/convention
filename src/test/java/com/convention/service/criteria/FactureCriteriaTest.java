package com.convention.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class FactureCriteriaTest {

    @Test
    void newFactureCriteriaHasAllFiltersNullTest() {
        var factureEntityCriteria = new FactureCriteria();
        assertThat(factureEntityCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void factureEntityCriteriaFluentMethodsCreatesFiltersTest() {
        var factureEntityCriteria = new FactureCriteria();

        setAllFilters(factureEntityCriteria);

        assertThat(factureEntityCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void factureEntityCriteriaCopyCreatesNullFilterTest() {
        var factureEntityCriteria = new FactureCriteria();
        var copy = factureEntityCriteria.copy();

        assertThat(factureEntityCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(factureEntityCriteria)
        );
    }

    @Test
    void factureEntityCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var factureEntityCriteria = new FactureCriteria();
        setAllFilters(factureEntityCriteria);

        var copy = factureEntityCriteria.copy();

        assertThat(factureEntityCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(factureEntityCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var factureEntityCriteria = new FactureCriteria();

        assertThat(factureEntityCriteria).hasToString("FactureCriteria{}");
    }

    private static void setAllFilters(FactureCriteria factureEntityCriteria) {
        factureEntityCriteria.id();
        factureEntityCriteria.numFacture();
        factureEntityCriteria.dateFacture();
        factureEntityCriteria.montantTotal();
        factureEntityCriteria.montantTTC();
        factureEntityCriteria.tva();
        factureEntityCriteria.ancienneRef();
        factureEntityCriteria.typeFacture();
        factureEntityCriteria.statut();
        factureEntityCriteria.dateEcheance();
        factureEntityCriteria.dateCreation();
        factureEntityCriteria.clientId();
        factureEntityCriteria.conventionId();
        factureEntityCriteria.distinct();
    }

    private static Condition<FactureCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNumFacture()) &&
                condition.apply(criteria.getDateFacture()) &&
                condition.apply(criteria.getMontantTotal()) &&
                condition.apply(criteria.getMontantTTC()) &&
                condition.apply(criteria.getTva()) &&
                condition.apply(criteria.getAncienneRef()) &&
                condition.apply(criteria.getTypeFacture()) &&
                condition.apply(criteria.getStatut()) &&
                condition.apply(criteria.getDateEcheance()) &&
                condition.apply(criteria.getDateCreation()) &&
                condition.apply(criteria.getClientId()) &&
                condition.apply(criteria.getConventionId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<FactureCriteria> copyFiltersAre(FactureCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNumFacture(), copy.getNumFacture()) &&
                condition.apply(criteria.getDateFacture(), copy.getDateFacture()) &&
                condition.apply(criteria.getMontantTotal(), copy.getMontantTotal()) &&
                condition.apply(criteria.getMontantTTC(), copy.getMontantTTC()) &&
                condition.apply(criteria.getTva(), copy.getTva()) &&
                condition.apply(criteria.getAncienneRef(), copy.getAncienneRef()) &&
                condition.apply(criteria.getTypeFacture(), copy.getTypeFacture()) &&
                condition.apply(criteria.getStatut(), copy.getStatut()) &&
                condition.apply(criteria.getDateEcheance(), copy.getDateEcheance()) &&
                condition.apply(criteria.getDateCreation(), copy.getDateCreation()) &&
                condition.apply(criteria.getClientId(), copy.getClientId()) &&
                condition.apply(criteria.getConventionId(), copy.getConventionId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
