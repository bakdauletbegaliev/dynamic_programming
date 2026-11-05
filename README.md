# Assignment 4 - Dynamic Programming and String Algorithms

**Student:** Begaliev Bakdaulet  
**Group:** SE-2526  
**Repository:** https://github.com/bakdauletbegaliev/dynamic_programming

## Project idea

This project compares three Edit Distance implementations and two string-search algorithms for a city search and spell-check system. The code uses basic Java, loops, arrays, recursion and lists.

### Algorithms

| Algorithm | Idea | Time complexity | Extra space |
|---|---|---|---|
| Recursive Edit Distance | Try insert, delete and replace without caching | Exponential in the worst case | O(n + m) call stack |
| Table Edit Distance | Fill the full 2D DP table | O(nm) | O(nm) |
| Two Rows Edit Distance | Keep only current and previous row | O(nm) | O(m) |
| Naive Search | Try the pattern at every text position | O(nm) worst case | O(1) apart from answers |
| KMP Search | Use LPS to avoid repeating comparisons | O(n + m) | O(m) apart from answers |

Here n and m mean the lengths of the input strings (for search, text and pattern).

## Requirements

- Java 17 or newer
- Maven 3.9 or newer
- Optional: Python 3 and matplotlib to regenerate the charts

## Run from the project folder

```bash
mvn clean test
mvn -q exec:java -Dexec.mainClass=daa.Demo
mvn -q exec:java
```

The last command runs the experiment and replaces `results/results.csv` with new measurements. It prints the median of five runs for each case.

To recreate the charts after running the experiment:

```bash
python scripts/plot_results.py
```

The charts already exist in `results/plots/`, so Python is not required to view the submitted results.

If Maven is not installed but Java is available, a quick demo can also be run with:

```bash
javac -d out src/main/java/daa/*.java
java -cp out daa.Demo
```

On Windows PowerShell, `javac -d out (Get-ChildItem src/main/java/daa/*.java)` is not necessary: `javac -d out src/main/java/daa/*.java` works in common shells, but Maven is the recommended option.

## File structure

```text
src/main/java/daa/       Five algorithms, two interfaces, generator, runner, demo
src/test/java/daa/       JUnit 5 tests
results/results.csv      280 measured runs (one row per run)
results/plots/           Four PNG charts
results/experiment_console.txt  Console output from the measured run
scripts/plot_results.py Chart generator
REPORT.md                Experiment design and conclusions
REPORT.pdf               Four-page printable report
UPLOAD_TO_GITHUB.md      Step-by-step Git commands
pom.xml                  Maven configuration
```

## How the experiments work

- Fixed random seed: `20261105`.
- Edit Distance types: `same` and `different`. Different strings use only A in the first and B in the second, to make the recursive method work hard.
- Search types: `random` (guaranteed pattern occurrence) and `worst` (A repeated in the text, pattern ends in B).
- Each case is warmed up once, then measured five times.
- `operations` counts real recursion calls, computed DP cells, or actual character comparisons (including building the LPS array for KMP).
- The input generator and operation counts are deterministic. Millisecond timings can change on another laptop because of the JVM and computer load.

## Simple explanation for the defense

**Edit Distance:** the minimum insertions, deletions, or replacements needed to turn one word into another. Recursion repeatedly solves the same problem, while DP stores useful previous answers. Two rows use less memory than a full table.

**Naive vs KMP:** Naive restarts the search at every position. KMP uses the LPS array to know which prefix can still match after a mismatch. Both return all matches, including overlapping ones.

**Experiment:** the expensive test for search is `AAAA...A` against `AAAA...AB`. Naive checks many A characters again for every position, but KMP skips that repeated work.

## AI-use acknowledgement

ChatGPT assisted in preparing the Java implementations, experiment design, tests, plotting script, and report wording. The source code and generated experimental results are provided for review and should be understood before the defense.
