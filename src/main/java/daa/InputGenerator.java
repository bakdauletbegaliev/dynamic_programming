package daa;

import java.util.Random;

public class InputGenerator {
    public static final long SEED = 20261105L;

    public static String randomText(int length, long seed) {
        Random random = new Random(seed);
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append("ABCD".charAt(random.nextInt(4)));
        }
        return text.toString();
    }

    public static String repeated(char letter, int length) {
        return String.valueOf(letter).repeat(length);
    }
}
