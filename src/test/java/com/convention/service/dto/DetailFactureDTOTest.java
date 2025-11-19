package com.convention.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.convention.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DetailFactureDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DetailFactureDTO.class);
        DetailFactureDTO detailFactureDTO1 = new DetailFactureDTO();
        detailFactureDTO1.setId(1L);
        DetailFactureDTO detailFactureDTO2 = new DetailFactureDTO();
        assertThat(detailFactureDTO1).isNotEqualTo(detailFactureDTO2);
        detailFactureDTO2.setId(detailFactureDTO1.getId());
        assertThat(detailFactureDTO1).isEqualTo(detailFactureDTO2);
        detailFactureDTO2.setId(2L);
        assertThat(detailFactureDTO1).isNotEqualTo(detailFactureDTO2);
        detailFactureDTO1.setId(null);
        assertThat(detailFactureDTO1).isNotEqualTo(detailFactureDTO2);
    }
}
