package com.convention.domain;

import static com.convention.domain.ConventionEntityTestSamples.*;
import static com.convention.domain.DetailConventionEntityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.convention.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DetailConventionEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DetailConventionEntity.class);
        DetailConventionEntity detailConventionEntity1 = getDetailConventionEntitySample1();
        DetailConventionEntity detailConventionEntity2 = new DetailConventionEntity();
        assertThat(detailConventionEntity1).isNotEqualTo(detailConventionEntity2);

        detailConventionEntity2.setId(detailConventionEntity1.getId());
        assertThat(detailConventionEntity1).isEqualTo(detailConventionEntity2);

        detailConventionEntity2 = getDetailConventionEntitySample2();
        assertThat(detailConventionEntity1).isNotEqualTo(detailConventionEntity2);
    }

    @Test
    void conventionTest() {
        DetailConventionEntity detailConventionEntity = getDetailConventionEntityRandomSampleGenerator();
        ConventionEntity conventionEntityBack = getConventionEntityRandomSampleGenerator();

        detailConventionEntity.setConvention(conventionEntityBack);
        assertThat(detailConventionEntity.getConvention()).isEqualTo(conventionEntityBack);

        detailConventionEntity.convention(null);
        assertThat(detailConventionEntity.getConvention()).isNull();
    }
}
