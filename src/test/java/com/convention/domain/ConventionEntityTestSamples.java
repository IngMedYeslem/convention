package com.convention.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ConventionEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ConventionEntity getConventionEntitySample1() {
        return new ConventionEntity().id(1L).numConvention(1L).nomResponsable("nomResponsable1");
    }

    public static ConventionEntity getConventionEntitySample2() {
        return new ConventionEntity().id(2L).numConvention(2L).nomResponsable("nomResponsable2");
    }

    public static ConventionEntity getConventionEntityRandomSampleGenerator() {
        return new ConventionEntity()
            .id(longCount.incrementAndGet())
            .numConvention(longCount.incrementAndGet())
            .nomResponsable(UUID.randomUUID().toString());
    }
}
