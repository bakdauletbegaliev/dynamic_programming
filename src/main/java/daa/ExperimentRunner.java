package daa;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ExperimentRunner {
    private static final int RUNS = 5;
    private static final int[] SMALL_SIZES = {2, 4, 6, 9};
    private static final int[] BIG_SIZES = {128, 256, 512, 1024};
    private static final int[] SEARCH_SIZES = {512, 2048, 8192, 32768};

    public static void main(String[] args) throws IOException {
        Files.createDirectories(Path.of("results"));
        try (PrintWriter csv = new PrintWriter(Files.newBufferedWriter(
                Path.of("results/results.csv"), StandardCharsets.UTF_8))) {
            csv.println("algorithm,input,n,pattern_length,run,time_ms,operations,result");
            System.out.println("Running deterministic inputs with seed " + InputGenerator.SEED);
            runDistances(csv);
            runSearches(csv);
        }
        System.out.println("Saved results/results.csv");
    }

    private static void runDistances(PrintWriter csv) {
        EditDistance[] algorithms = {
                new RecursiveDistance(), new TableDistance(), new TwoRowDistance()
        };
        String[] names = {"Recursive", "Table", "TwoRows"};

        for (int index = 0; index < algorithms.length; index++) {
            int[] sizes = index == 0 ? SMALL_SIZES : combine(SMALL_SIZES, BIG_SIZES);
            for (int n : sizes) {
                for (String input : new String[]{"same", "different"}) {
                    String a = input.equals("same")
                            ? InputGenerator.randomText(n, InputGenerator.SEED + n) : InputGenerator.repeated('A', n);
                    String b = input.equals("same") ? a : InputGenerator.repeated('B', n);
                    EditDistance algorithm = algorithms[index];
                    algorithm.distance(a, b);
                    List<Double> times = new ArrayList<>();
                    for (int run = 1; run <= RUNS; run++) {
                        long start = System.nanoTime();
                        int answer = algorithm.distance(a, b);
                        long elapsed = System.nanoTime() - start;
                        double ms = elapsed / 1_000_000.0;
                        times.add(ms);
                        csv.printf(Locale.US, "%s,%s,%d,0,%d,%.6f,%d,%d%n",
                                names[index], input, n, run, ms, algorithm.getOperations(), answer);
                    }
                    printMedian(names[index], input, n, times);
                }
            }
        }
    }

    private static void runSearches(PrintWriter csv) {
        StringSearch[] algorithms = {new NaiveSearch(), new KMPSearch()};
        String[] names = {"Naive", "KMP"};

        for (int index = 0; index < algorithms.length; index++) {
            for (int n : SEARCH_SIZES) {
                int m = n / 16;
                for (String input : new String[]{"random", "worst"}) {
                    String text;
                    String pattern;
                    if (input.equals("random")) {
                        text = InputGenerator.randomText(n, InputGenerator.SEED + n);
                        pattern = text.substring(n / 3, n / 3 + m);
                    } else {
                        text = InputGenerator.repeated('A', n);
                        pattern = InputGenerator.repeated('A', m - 1) + "B";
                    }
                    StringSearch algorithm = algorithms[index];
                    algorithm.search(text, pattern);
                    List<Double> times = new ArrayList<>();
                    for (int run = 1; run <= RUNS; run++) {
                        long start = System.nanoTime();
                        int matches = algorithm.search(text, pattern).size();
                        long elapsed = System.nanoTime() - start;
                        double ms = elapsed / 1_000_000.0;
                        times.add(ms);
                        csv.printf(Locale.US, "%s,%s,%d,%d,%d,%.6f,%d,%d%n",
                                names[index], input, n, m, run, ms, algorithm.getComparisons(), matches);
                    }
                    printMedian(names[index], input, n, times);
                }
            }
        }
    }

    private static int[] combine(int[] a, int[] b) {
        int[] result = new int[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    private static void printMedian(String algorithm, String input, int n, List<Double> times) {
        Collections.sort(times);
        System.out.printf(Locale.US, "%-10s %-10s n=%-6d median=%.6f ms%n",
                algorithm, input, n, times.get(times.size() / 2));
    }
}
