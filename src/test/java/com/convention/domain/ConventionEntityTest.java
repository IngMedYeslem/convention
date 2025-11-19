package com.convention.domain;

import static com.convention.domain.ClientEntityTestSamples.*;
import static com.convention.domain.ConventionEntityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.convention.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConventionEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConventionEntity.class);
        ConventionEntity conventionEntity1 = getConventionEntitySample1();
        ConventionEntity conventionEntity2 = new ConventionEntity();
        assertThat(conventionEntity1).isNotEqualTo(conventionEntity2);

        conventionEntity2.setId(conventionEntity1.getId());
        assertThat(conventionEntity1).isEqualTo(conventionEntity2);

        conventionEntity2 = getConventionEntitySample2();
        assertThat(conventionEntity1).isNotEqualTo(conventionEntity2);
    }

    @Test
    void clientTest() {
        ConventionEntity conventionEntity = getConventionEntityRandomSampleGenerator();
        ClientEntity clientEntityBack = getClientEntityRandomSampleGenerator();

        conventionEntity.setClient(clientEntityBack);
        assertThat(conventionEntity.getClient()).isEqualTo(clientEntityBack);

        conventionEntity.client(null);
        assertThat(conventionEntity.getClient()).isNull();
    }
}
