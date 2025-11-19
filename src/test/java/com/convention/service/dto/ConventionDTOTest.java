package com.convention.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.convention.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConventionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConventionDTO.class);
        ConventionDTO conventionDTO1 = new ConventionDTO();
        conventionDTO1.setId(1L);
        ConventionDTO conventionDTO2 = new ConventionDTO();
        assertThat(conventionDTO1).isNotEqualTo(conventionDTO2);
        conventionDTO2.setId(conventionDTO1.getId());
        assertThat(conventionDTO1).isEqualTo(conventionDTO2);
        conventionDTO2.setId(2L);
        assertThat(conventionDTO1).isNotEqualTo(conventionDTO2);
        conventionDTO1.setId(null);
        assertThat(conventionDTO1).isNotEqualTo(conventionDTO2);
    }
}
