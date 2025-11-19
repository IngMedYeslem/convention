package com.convention.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.convention.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DetailConventionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DetailConventionDTO.class);
        DetailConventionDTO detailConventionDTO1 = new DetailConventionDTO();
        detailConventionDTO1.setId(1L);
        DetailConventionDTO detailConventionDTO2 = new DetailConventionDTO();
        assertThat(detailConventionDTO1).isNotEqualTo(detailConventionDTO2);
        detailConventionDTO2.setId(detailConventionDTO1.getId());
        assertThat(detailConventionDTO1).isEqualTo(detailConventionDTO2);
        detailConventionDTO2.setId(2L);
        assertThat(detailConventionDTO1).isNotEqualTo(detailConventionDTO2);
        detailConventionDTO1.setId(null);
        assertThat(detailConventionDTO1).isNotEqualTo(detailConventionDTO2);
    }
}
