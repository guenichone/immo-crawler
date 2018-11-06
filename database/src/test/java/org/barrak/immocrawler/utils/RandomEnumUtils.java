package org.barrak.immocrawler.utils;

import java.util.Random;

public class RandomEnumUtils {

    private Random random;

    public RandomEnumUtils(Random random) {
        this.random = random;
    }

    public <T extends Enum<?>> T randomEnum(Class<T> clazz){
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }
}
