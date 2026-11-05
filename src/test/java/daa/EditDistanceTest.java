package daa;

import org.junit.jupiter.api.Test;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EditDistanceTest {
    private final EditDistance[] algorithms = {
            new RecursiveDistance(), new TableDistance(), new TwoRowDistance()
    };

    @Test
    void referenceValues() {
        for (EditDistance algorithm : algorithms) {
            assertEquals(3, algorithm.distance("kitten", "sitting"));
            assertEquals(2, algorithm.distance("flaw", "lawn"));
            assertEquals(5, algorithm.distance("intention", "execution"));
            assertEquals(3, algorithm.distance("", "abc"));
            assertEquals(3, algorithm.distance("abc", ""));
            assertEquals(0, algorithm.distance("", ""));
            assertEquals(0, algorithm.distance("abc", "abc"));
        }
    }

    @Test
    void oneHundredRandomPairsAgree() {
        Random random = new Random(20261105L);
        for (int test = 0; test < 100; test++) {
            String a = randomString(random, random.nextInt(6));
            String b = randomString(random, random.nextInt(6));
            int expected = algorithms[0].distance(a, b);
            assertEquals(expected, algorithms[1].distance(a, b));
            assertEquals(expected, algorithms[2].distance(a, b));
        }
    }

    private String randomString(Random random, int n) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < n; i++) result.append("ABC".charAt(random.nextInt(3)));
        return result.toString();
    }
}
