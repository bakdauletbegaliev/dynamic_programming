package daa;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

class SearchTest {
    private final StringSearch[] algorithms = {new NaiveSearch(), new KMPSearch()};

    @Test
    void referenceLpsArrays() {
        KMPSearch kmp = new KMPSearch();
        assertArrayEquals(new int[]{0, 1, 2, 0, 1, 2, 3, 3}, kmp.lps("AAACAAAA"));
        assertArrayEquals(new int[]{0, 0, 1, 2, 0, 1, 2, 3, 4}, kmp.lps("ABABCABAB"));
        assertArrayEquals(new int[]{}, kmp.lps(""));
    }

    @Test
    void referenceMatchesAndOverlaps() {
        for (StringSearch algorithm : algorithms) {
            assertEquals(List.of(10), algorithm.search("ABABDABACDABABCABAB", "ABABCABAB"));
            assertEquals(List.of(0, 1, 2, 3), algorithm.search("AAAAA", "AA"));
            assertEquals(List.of(0, 2, 4), algorithm.search("ABABABA", "ABA"));
            assertEquals(List.of(), algorithm.search("HELLO", "XYZ"));
            assertEquals(List.of(), algorithm.search("AB", "ABCD"));
            assertEquals(List.of(), algorithm.search("", "A"));
            assertEquals(List.of(0), algorithm.search("A", "A"));
        }
    }

    @Test
    void emptyPatternThrows() {
        for (StringSearch algorithm : algorithms) {
            assertThrows(IllegalArgumentException.class, () -> algorithm.search("ABC", ""));
            assertThrows(IllegalArgumentException.class, () -> algorithm.search("", ""));
        }
    }

    @Test
    void oneHundredRandomCasesAgree() {
        Random random = new Random(20261105L);
        for (int test = 0; test < 100; test++) {
            String text = randomString(random, random.nextInt(30));
            String pattern = randomString(random, 1 + random.nextInt(8));
            assertEquals(algorithms[0].search(text, pattern), algorithms[1].search(text, pattern));
        }
    }

    private String randomString(Random random, int length) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) result.append("ABC".charAt(random.nextInt(3)));
        return result.toString();
    }
}
