package com.convention.domain;

import static com.convention.domain.DetailFactureEntityTestSamples.*;
import static com.convention.domain.FactureEntityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.convention.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DetailFactureEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DetailFactureEntity.class);
        DetailFactureEntity detailFactureEntity1 = getDetailFactureEntitySample1();
        DetailFactureEntity detailFactureEntity2 = new DetailFactureEntity();
        assertThat(detailFactureEntity1).isNotEqualTo(detailFactureEntity2);

        detailFactureEntity2.setId(detailFactureEntity1.getId());
        assertThat(detailFactureEntity1).isEqualTo(detailFactureEntity2);

        detailFactureEntity2 = getDetailFactureEntitySample2();
        assertThat(detailFactureEntity1).isNotEqualTo(detailFactureEntity2);
    }

    @Test
    void factureTest() {
        DetailFactureEntity detailFactureEntity = getDetailFactureEntityRandomSampleGenerator();
        FactureEntity factureEntityBack = getFactureEntityRandomSampleGenerator();

        detailFactureEntity.setFacture(factureEntityBack);
        assertThat(detailFactureEntity.getFacture()).isEqualTo(factureEntityBack);

        detailFactureEntity.facture(null);
        assertThat(detailFactureEntity.getFacture()).isNull();
    }
}
