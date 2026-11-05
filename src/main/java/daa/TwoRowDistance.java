package daa;

public class TwoRowDistance implements EditDistance {
    private long operations;

    // State: previous[j] and current[j] store the previous and current DP rows.
    // Transition: same minimum of insert, delete, replace as the full table.
    // Base case: the first row and column hold prefix lengths.
    @Override
    public int distance(String a, String b) {
        operations = 0;
        int n = a.length();
        int m = b.length();
        int[] previous = new int[m + 1];
        int[] current = new int[m + 1];

        for (int j = 0; j <= m; j++) previous[j] = j;

        for (int i = 1; i <= n; i++) {
            current[0] = i;
            for (int j = 1; j <= m; j++) {
                operations++;
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    current[j] = previous[j - 1];
                } else {
                    int insert = current[j - 1];
                    int delete = previous[j];
                    int replace = previous[j - 1];
                    current[j] = 1 + Math.min(insert, Math.min(delete, replace));
                }
            }
            int[] temp = previous;
            previous = current;
            current = temp;
        }
        return previous[m];
    }

    @Override
    public long getOperations() {
        return operations;
    }
}
