package com.example.lab203.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Random;

/**
 * ???????????
 */
public final class RandomResearchTimeGenerator {

    private RandomResearchTimeGenerator() {
    }

    /**
     * ??????????????????
     *
     * @param studentId ????
     * @param date ??
     * @return 2.5 ? 10.0 ??????????
     */
    public static BigDecimal generate(Long studentId, LocalDate date) {
        long seed = studentId * 97 + date.toEpochDay() * 31;
        Random random = new Random(seed);
        double raw = 2.5D + random.nextInt(16) * 0.5D;
        return BigDecimal.valueOf(raw).setScale(1, RoundingMode.HALF_UP);
    }
}
