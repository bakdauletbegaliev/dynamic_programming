package daa;

public class RecursiveDistance implements EditDistance {
    private long operations;

    // State: distance(i, j) uses the first i and j characters of the strings.
    // Transition: matching characters cost 0; otherwise try insert, delete, replace.
    // Base case: if one prefix is empty, return the length of the other prefix.
    @Override
    public int distance(String a, String b) {
        operations = 0;
        return solve(a, b, a.length(), b.length());
    }

    private int solve(String a, String b, int i, int j) {
        operations++;
        if (i == 0) return j;
        if (j == 0) return i;
        if (a.charAt(i - 1) == b.charAt(j - 1)) {
            return solve(a, b, i - 1, j - 1);
        }
        int insert = solve(a, b, i, j - 1);
        int delete = solve(a, b, i - 1, j);
        int replace = solve(a, b, i - 1, j - 1);
        return 1 + Math.min(insert, Math.min(delete, replace));
    }

    @Override
    public long getOperations() {
        return operations;
    }
}
