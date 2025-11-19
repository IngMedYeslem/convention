package com.convention.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ClientEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ClientEntity getClientEntitySample1() {
        return new ClientEntity()
            .id(1L)
            .numClient(1L)
            .nomClient("nomClient1")
            .adresseClient("adresseClient1")
            .emailClient("emailClient1")
            .whatsClient("whatsClient1");
    }

    public static ClientEntity getClientEntitySample2() {
        return new ClientEntity()
            .id(2L)
            .numClient(2L)
            .nomClient("nomClient2")
            .adresseClient("adresseClient2")
            .emailClient("emailClient2")
            .whatsClient("whatsClient2");
    }

    public static ClientEntity getClientEntityRandomSampleGenerator() {
        return new ClientEntity()
            .id(longCount.incrementAndGet())
            .numClient(longCount.incrementAndGet())
            .nomClient(UUID.randomUUID().toString())
            .adresseClient(UUID.randomUUID().toString())
            .emailClient(UUID.randomUUID().toString())
            .whatsClient(UUID.randomUUID().toString());
    }
}
