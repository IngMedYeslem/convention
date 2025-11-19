package com.convention.domain;

import static com.convention.domain.ClientEntityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.convention.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClientEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClientEntity.class);
        ClientEntity clientEntity1 = getClientEntitySample1();
        ClientEntity clientEntity2 = new ClientEntity();
        assertThat(clientEntity1).isNotEqualTo(clientEntity2);

        clientEntity2.setId(clientEntity1.getId());
        assertThat(clientEntity1).isEqualTo(clientEntity2);

        clientEntity2 = getClientEntitySample2();
        assertThat(clientEntity1).isNotEqualTo(clientEntity2);
    }
}
