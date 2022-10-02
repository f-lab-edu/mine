package com.mine.util;

public class IncrementUtil {

    private IncrementUtil() {

    }

    public static int getIncrement(long price) {
        int increment = 0;

        if (price < 10000) {
            increment = 500;
        } else if (price < 50000) {
            increment = 800;
        } else if (price < 150000) {
            increment = 2000;
        } else if (price < 350000) {
            increment = 4000;
        } else if (price < 700000) {
            increment = 7500;
        } else if (price < 1350000) {
            increment = 15000;
        } else if (price < 3500000) {
            increment = 38000;
        } else if (price < 7000000) {
            increment = 75000;
        } else {
            increment = 150000;
        }

        return increment;
    }
}
