# DAA_BonusTask
Introduction

In this bonus task I implemented the Rabin–Karp string matching algorithm in Java using a polynomial rolling hash.
The goal is to find all occurrences of a pattern string P of length m inside a text string T of length n.

Instead of comparing the pattern with every substring character by character, Rabin–Karp converts strings into integers (hashes) and compares those much faster. If two hashes are equal, I then confirm the match with a direct string comparison to avoid false positives due to hash collisions.

Algorithm overview

For a string s[0..m-1], the polynomial hash is:

𝐻
(
𝑠
)
=
(
𝑠
[
0
]
⋅
𝐵
𝐴
𝑆
𝐸
𝑚
−
1
+
𝑠
[
1
]
⋅
𝐵
𝐴
𝑆
𝐸
𝑚
−
2
+
⋯
+
𝑠
[
𝑚
−
1
]
)
 
m
o
d
 
𝑀
𝑂
𝐷
H(s)=(s[0]⋅BASE
m−1
+s[1]⋅BASE
m−2
+⋯+s[m−1])modMOD

I choose:

BASE = 911382323

MOD = 1,000,000,007 (a large prime)

To compute substring hashes efficiently, I precompute:

power[i] = BASE^i mod MOD for i = 0..n

prefix hashes of the text:

𝑝
𝑟
𝑒
𝑓
𝑖
𝑥
[
𝑖
]
=
𝐻
(
𝑇
[
0..
𝑖
−
1
]
)
prefix[i]=H(T[0..i−1])

Then any substring T[l..r-1] (length m = r - l) can be computed in O(1):

𝐻
(
𝑇
[
𝑙
.
.
𝑟
−
1
]
)
=
𝑝
𝑟
𝑒
𝑓
𝑖
𝑥
[
𝑟
]
−
𝑝
𝑟
𝑒
𝑓
𝑖
𝑥
[
𝑙
]
⋅
𝑝
𝑜
𝑤
𝑒
𝑟
[
𝑚
]
m
o
d
 
 
𝑀
𝑂
𝐷
H(T[l..r−1])=prefix[r]−prefix[l]⋅power[m]modMOD

I also compute the hash of the pattern P.
Then I slide a window of length m across the text and compare the substring hash with the pattern hash. When hashes are equal, I verify the candidate by text.regionMatches(...).

Correctness reasoning

Prefix hashes ensure every substring hash is computed over exactly the same polynomial basis as the pattern hash.

Using a large prime modulus significantly reduces collision probability.

Even if a collision happens, I confirm using a direct substring comparison.
Therefore, the algorithm never reports a false match.

Time complexity

Let n be the length of text and m be the length of pattern.

Precomputing powers:

We compute power[0..n] in a single loop → O(n).

Building prefix hashes for the text:

One pass over the text → O(n).

Computing the pattern hash:

One pass over the pattern → O(m).

Sliding the window:

There are n - m + 1 windows at most.

Each window:

Computes substring hash in O(1) using prefix hashes and power.

Compares with patternHash.

If hashes are equal, we do a direct comparison:

Worst case: O(m) for that position.

Expected / average case: collisions are rare, so direct comparisons happen only when there is a real match.

Average / typical time complexity (few collisions):

𝑇
𝑎
𝑣
𝑔
=
𝑂
(
𝑛
+
𝑚
)
≈
𝑂
(
𝑛
)
T
avg
	​

=O(n+m)≈O(n)

Worst-case time complexity:

In the pathological case where many hashes collide, we might do an O(m) check for almost every position:

𝑇
𝑤
𝑜
𝑟
𝑠
𝑡
=
𝑂
(
𝑛
⋅
𝑚
)
T
worst
	​

=O(n⋅m)

However, with a good choice of BASE and MOD, this worst case is extremely unlikely in practice.

Space complexity

We store:

power array of size n + 1

prefix array of size n + 1

a small number of long variables for hashes

So the space complexity is:

𝑆
=
𝑂
(
𝑛
)
S=O(n)

The pattern itself uses O(m) additional space (the string), but this is already part of the input.

Testing summary

I used three test cases of increasing size:

Short string

text = "abracadabra"

pattern = "abra"

Expected matches at indices [0, 7] → the algorithm produced [0, 7].

Medium-length string

text = "the quick brown fox jumps over the lazy dog and the quick blue hare"

pattern = "quick"

The pattern appears twice. The algorithm returned [4, 52], which correspond to both occurrences.

Long string

text is ~20000 characters long, built by repeating "abcxabcdabxabcdabcdabcy" 1000 times.

pattern = "abcdabcy"

The pattern appears once inside each repetition, so there are 1000 matches.

The algorithm successfully found all of them, and performance remained fast.

These experiments show that the implementation behaves as expected on short, medium, and long inputs.
