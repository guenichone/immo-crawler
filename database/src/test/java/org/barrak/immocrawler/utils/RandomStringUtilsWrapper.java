package org.barrak.immocrawler.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

public class RandomStringUtilsWrapper {

    private Random random;

    public RandomStringUtilsWrapper(Random random) {
        this.random = random;
    }

    public String randomAlphabetic(int count) {
        return RandomStringUtils.random(count, 0, 0, true, false, null, random);
    }

    public String randomAlphanumeric(int count) {
        RandomStringUtils.randomAlphanumeric(count);
        return RandomStringUtils.random(count, 0, 0, true, true, null, random);
    }
}
