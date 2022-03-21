package utils;

import java.util.concurrent.ThreadLocalRandom;

public class Utils {
    private Utils() { }

    public static String pad(int n, int length) {
        return n == 0 ? " ".repeat(length) : ("%0" + length + "d").formatted(n);
    }
}
