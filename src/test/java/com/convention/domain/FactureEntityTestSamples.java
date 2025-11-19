package com.convention.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FactureEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static FactureEntity getFactureEntitySample1() {
        return new FactureEntity().id(1L).numFacture(1L).ancienneRef("ancienneRef1");
    }

    public static FactureEntity getFactureEntitySample2() {
        return new FactureEntity().id(2L).numFacture(2L).ancienneRef("ancienneRef2");
    }

    public static FactureEntity getFactureEntityRandomSampleGenerator() {
        return new FactureEntity()
            .id(longCount.incrementAndGet())
            .numFacture(longCount.incrementAndGet())
            .ancienneRef(UUID.randomUUID().toString());
    }
}
