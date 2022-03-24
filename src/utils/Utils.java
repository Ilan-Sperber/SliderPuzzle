package utils;

import java.util.concurrent.ThreadLocalRandom;

public class Utils {
    private Utils() { }

    public static String pad(int n, int length) {
        return n == 0 ? " ".repeat(length) : ("%0" + length + "d").formatted(n);
    }

    public static void swap(int[][] array, int srcRow, int srcCol, int destRow, int destCol) {
        int temp = array[srcRow][srcCol];
        array[srcRow][srcCol] = array[destRow][destCol];
        array[destRow][destCol] = temp;
    }
}
