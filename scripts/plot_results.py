import csv
import statistics
from collections import defaultdict
from pathlib import Path

import matplotlib.pyplot as plt

ROOT = Path(__file__).resolve().parent.parent
RESULTS = ROOT / "results"
PLOTS = RESULTS / "plots"
PLOTS.mkdir(parents=True, exist_ok=True)

data = defaultdict(list)
with (RESULTS / "results.csv").open(newline="", encoding="utf-8") as file:
    for row in csv.DictReader(file):
        key = (row["algorithm"], row["input"], int(row["n"]))
        data[key].append(row)


def operations(algorithm, input_type, n):
    return int(data[(algorithm, input_type, n)][0]["operations"])


def median_time(algorithm, input_type, n):
    values = [float(row["time_ms"]) for row in data[(algorithm, input_type, n)]]
    return statistics.median(values)


def save(name):
    plt.tight_layout()
    plt.savefig(PLOTS / name, dpi=160)
    plt.close()


small = [2, 4, 6, 9]
plt.figure(figsize=(8, 4.8))
for algorithm in ["Recursive", "Table", "TwoRows"]:
    plt.plot(small, [operations(algorithm, "different", n) for n in small],
             marker="o", label=algorithm)
plt.title("Edit Distance: Operations on Different Strings")
plt.xlabel("String length n (characters)")
plt.ylabel("Operations (count, log scale)")
plt.yscale("log")
plt.xticks(small)
plt.grid(alpha=0.25)
plt.legend()
save("edit_operations.png")

large = [128, 256, 512, 1024]
plt.figure(figsize=(8, 4.8))
for algorithm in ["Table", "TwoRows"]:
    plt.plot(large, [median_time(algorithm, "different", n) for n in large],
             marker="o", label=algorithm)
plt.title("Edit Distance: Median Time on Different Strings")
plt.xlabel("String length n (characters)")
plt.ylabel("Median time (ms, 5 runs)")
plt.grid(alpha=0.25)
plt.legend()
save("edit_median_time.png")

search = [512, 2048, 8192, 32768]
plt.figure(figsize=(8, 4.8))
for algorithm in ["Naive", "KMP"]:
    plt.plot(search, [operations(algorithm, "worst", n) for n in search],
             marker="o", label=algorithm)
plt.title("String Search: Character Comparisons (Worst Case)")
plt.xlabel("Text length n (characters)")
plt.ylabel("Character comparisons (count, log scale)")
plt.yscale("log")
plt.grid(alpha=0.25)
plt.legend()
save("search_comparisons.png")

ratios = [operations("Naive", "worst", n) / operations("KMP", "worst", n)
          for n in search]
plt.figure(figsize=(8, 4.8))
plt.plot(search, ratios, marker="o")
plt.title("Worst-Case Search: Naive / KMP Comparison Ratio")
plt.xlabel("Text length n (characters)")
plt.ylabel("Comparison ratio (times)")
plt.grid(alpha=0.25)
save("search_ratio.png")

print("Created four PNG charts in results/plots/")
