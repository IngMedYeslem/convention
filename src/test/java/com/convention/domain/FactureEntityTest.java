package com.convention.domain;

import static com.convention.domain.ClientEntityTestSamples.*;
import static com.convention.domain.ConventionEntityTestSamples.*;
import static com.convention.domain.FactureEntityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.convention.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FactureEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FactureEntity.class);
        FactureEntity factureEntity1 = getFactureEntitySample1();
        FactureEntity factureEntity2 = new FactureEntity();
        assertThat(factureEntity1).isNotEqualTo(factureEntity2);

        factureEntity2.setId(factureEntity1.getId());
        assertThat(factureEntity1).isEqualTo(factureEntity2);

        factureEntity2 = getFactureEntitySample2();
        assertThat(factureEntity1).isNotEqualTo(factureEntity2);
    }

    @Test
    void clientTest() {
        FactureEntity factureEntity = getFactureEntityRandomSampleGenerator();
        ClientEntity clientEntityBack = getClientEntityRandomSampleGenerator();

        factureEntity.setClient(clientEntityBack);
        assertThat(factureEntity.getClient()).isEqualTo(clientEntityBack);

        factureEntity.client(null);
        assertThat(factureEntity.getClient()).isNull();
    }

    @Test
    void conventionTest() {
        FactureEntity factureEntity = getFactureEntityRandomSampleGenerator();
        ConventionEntity conventionEntityBack = getConventionEntityRandomSampleGenerator();

        factureEntity.setConvention(conventionEntityBack);
        assertThat(factureEntity.getConvention()).isEqualTo(conventionEntityBack);

        factureEntity.convention(null);
        assertThat(factureEntity.getConvention()).isNull();
    }
}
