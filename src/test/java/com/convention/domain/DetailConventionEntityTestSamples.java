package com.convention.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DetailConventionEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static DetailConventionEntity getDetailConventionEntitySample1() {
        return new DetailConventionEntity().id(1L).designation("designation1").quantite(1);
    }

    public static DetailConventionEntity getDetailConventionEntitySample2() {
        return new DetailConventionEntity().id(2L).designation("designation2").quantite(2);
    }

    public static DetailConventionEntity getDetailConventionEntityRandomSampleGenerator() {
        return new DetailConventionEntity()
            .id(longCount.incrementAndGet())
            .designation(UUID.randomUUID().toString())
            .quantite(intCount.incrementAndGet());
    }
}
