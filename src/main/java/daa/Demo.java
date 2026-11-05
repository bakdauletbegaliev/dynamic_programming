package daa;

public class Demo {
    public static void main(String[] args) {
        EditDistance recursive = new RecursiveDistance();
        EditDistance table = new TableDistance();
        EditDistance twoRows = new TwoRowDistance();
        StringSearch naive = new NaiveSearch();
        KMPSearch kmp = new KMPSearch();

        System.out.println("Edit Distance: kitten -> sitting");
        System.out.println("Recursive: " + recursive.distance("kitten", "sitting"));
        System.out.println("Table: " + table.distance("kitten", "sitting"));
        System.out.println("Two rows: " + twoRows.distance("kitten", "sitting"));
        System.out.println("Naive matches: " + naive.search("ABABABA", "ABA"));
        System.out.println("KMP matches: " + kmp.search("ABABABA", "ABA"));
        System.out.println("LPS: " + java.util.Arrays.toString(kmp.lps("ABABCABAB")));
    }
}
