
# 1. Data Summary

| Test Case      | Text Length          | Pattern      | Pattern Length | Expected Matches |
| -------------- | -------------------- | ------------ | -------------- | ---------------- |
| **short.txt**  | 11 (`"abracadabra"`) | `"abra"`     | 4              | `[0, 7]`         |
| **medium.txt** | 68                   | `"quick"`    | 5              | `[4, 52]`        |
| **long.txt**   | ~22,000              | `"abcdabcy"` | 8              | 1000 occurrences |

**Dataset Description:**
To observe performance under different conditions, three datasets of increasing size were tested.
The long dataset was synthesized by repeating the string `"abcxabcdabxabcdabcdabcy"` 1000 times.

---

# 2. Results

## A. Pattern Match Detection (Correctness)

| Dataset        | Found Matches | Correct | Notes                        |
| -------------- | ------------- | ------- | ---------------------------- |
| **short.txt**  | `[0, 7]`      | ✔ Yes   | Both occurrences detected    |
| **medium.txt** | `[4, 52]`     | ✔ Yes   | Matches two real occurrences |
| **long.txt**   | 1000          | ✔ Yes   | All repeated patterns found  |

---

## B. Execution Time (Rabin–Karp with Polynomial Rolling Hash)

| Dataset        | Time (ms) | Observations                    |
| -------------- | --------- | ------------------------------- |
| **short.txt**  | 0.2 ms    | Very small input → near-instant |
| **medium.txt** | 0.7 ms    | Stable linear performance       |
| **long.txt**   | 14.4 ms   | Efficient across 22k characters |

---

## C. Hash Collision Analysis

| Property                | Value                           |
| ----------------------- | ------------------------------- |
| **Hash Base (B)**       | 911,382,323                     |
| **Modulus (MOD)**       | 1,000,000,007                   |
| **Collisions Observed** | 0                               |
| **Collision Handling**  | Verified with `regionMatches()` |

All matches were confirmed to avoid false positives.

---

# 3. Analysis

## Algorithm Complexity

### Time Complexity

| Operation                | Complexity                            |
| ------------------------ | ------------------------------------- |
| Prefix hash computation  | **O(n)**                              |
| Pattern hash computation | **O(m)**                              |
| Sliding window hashing   | **O(n)**                              |
| Collision verification   | **O(1)** average                      |
| **Total (average)**      | **O(n + m)**                          |
| **Worst-case**           | **O(n · m)** (rare collision cascade) |

### Space Complexity

| Structure         | Space    |
| ----------------- | -------- |
| Prefix hash array | **O(n)** |
| Power array       | **O(n)** |
| Aux variables     | **O(1)** |
| **Total**         | **O(n)** |

---

## Performance Observations

* Performance scales **linearly with text length**.
* Even on large input (~22k characters), runtime stayed under **15 ms**.
* Verification of actual matches prevents incorrect reporting even if hash collision occurs.
* Rolling hash allowed computing substring hashes in **O(1)**, significantly reducing total runtime.

---

# 4. Conclusions

| Aspect                      | Strengths                                                            | Weaknesses                                       |
| --------------------------- | -------------------------------------------------------------------- | ------------------------------------------------ |
| **Rabin–Karp Algorithm**    | Fast in practice, simple to implement, good for multi-pattern search | Worst-case degrades if many collisions           |
| **Polynomial Rolling Hash** | Efficient, predictable performance, low collision probability        | Requires large prime MOD and careful base choice |
| **Verification Step**       | Eliminates false positives                                           | Adds small extra cost on real matches            |
| **Overall Suitability**     | Excellent for searching repeated patterns in large text              | Needs careful hash parameter selection           |

Rabin–Karp performed reliably across all datasets, producing correct and collision-free results with near-linear runtime.

---
