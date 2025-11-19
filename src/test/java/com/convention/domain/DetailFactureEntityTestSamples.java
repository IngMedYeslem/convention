package com.convention.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DetailFactureEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static DetailFactureEntity getDetailFactureEntitySample1() {
        return new DetailFactureEntity().id(1L).designation("designation1").quantite(1).observations("observations1");
    }

    public static DetailFactureEntity getDetailFactureEntitySample2() {
        return new DetailFactureEntity().id(2L).designation("designation2").quantite(2).observations("observations2");
    }

    public static DetailFactureEntity getDetailFactureEntityRandomSampleGenerator() {
        return new DetailFactureEntity()
            .id(longCount.incrementAndGet())
            .designation(UUID.randomUUID().toString())
            .quantite(intCount.incrementAndGet())
            .observations(UUID.randomUUID().toString());
    }
}
