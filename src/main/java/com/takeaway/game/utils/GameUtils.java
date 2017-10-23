package com.takeaway.game.utils;

import java.util.Arrays;

/**
 * @author abhishekrai
 * @since 0.1.0
 */
public class GameUtils {

    private static final int[] a = new int[]{-1, 0, 1};

    /**
     * Method processes input number to be divisible by {@code 3}
     * Every number is divisible by three if processed with either of the {@code new int[]{-1, 0, 1}} element.
     *
     * {@link Arrays} primitive {@code Stream} method processes the number and reduces it to divisible by {@code 3}.
     * @param number input number
     * @return number divisible by {@code 3}
     */
    public static int makeNumberDivisibleByThree(int number) {
        return Arrays.stream(a).map(n -> n + number).filter(n -> (n % 3 == 0)).reduce(0, (m, n) -> m + n);
    }
}
