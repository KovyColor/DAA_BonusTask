package BonusTask;

import java.util.ArrayList;
import java.util.List;


public class RabinKarp {

    // Large prime modulus for hashing (to reduce collisions)
    private static final long MOD = 1_000_000_007L;

    // Base for polynomial rolling hash. Must be < MOD.
    private static final long BASE = 911_382_323L;


    public static List<Integer> search(String text, String pattern) {
        List<Integer> result = new ArrayList<>();

        int n = text.length();
        int m = pattern.length();

        // Edge cases: empty pattern or pattern longer than text
        if (m == 0 || m > n) {
            return result;
        }

        // Precompute powers of BASE up to length n:
        // power[i] = BASE^i mod MOD
        long[] power = new long[n + 1];
        power[0] = 1;
        for (int i = 1; i <= n; i++) {
            power[i] = (power[i - 1] * BASE) % MOD;
        }

        // Prefix hashes for text:
        // prefix[i] = hash of text[0..i-1]
        long[] prefix = new long[n + 1];
        prefix[0] = 0;
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = (prefix[i] * BASE + text.charAt(i)) % MOD;
        }

        // Hash for pattern
        long patternHash = 0;
        for (int i = 0; i < m; i++) {
            patternHash = (patternHash * BASE + pattern.charAt(i)) % MOD;
        }

        // Slide a window of length m over text
        for (int start = 0; start + m <= n; start++) {
            int end = start + m;

            // Hash of substring text[start..end-1]
            long currentHash = prefix[end] - (prefix[start] * power[m] % MOD);
            if (currentHash < 0) currentHash += MOD; // keep it positive

            // If hash matches, verify by direct comparison to avoid collisions
            if (currentHash == patternHash) {
                if (text.regionMatches(start, pattern, 0, m)) {
                    result.add(start);
                }
            }
        }

        return result;
    }


    private static void printMatches(String testName, String text, String pattern) {
        System.out.println("=== " + testName + " ===");
        System.out.println("Text   : " + text);
        System.out.println("Pattern: " + pattern);

        List<Integer> matches = search(text, pattern);
        if (matches.isEmpty()) {
            System.out.println("No matches found.\n");
        } else {
            System.out.println("Matches at positions: " + matches);
            System.out.println();
        }
    }

    public static void main(String[] args) {
        // 1) Short example
        String text1 = "abracadabra";
        String pattern1 = "abra";
        printMatches("Short example", text1, pattern1);

        // 2) Medium example (~80 characters)
        String text2 = "the quick brown fox jumps over the lazy dog and the quick blue hare";
        String pattern2 = "quick";
        printMatches("Medium example", text2, pattern2);

        // 3) Long example: build a long text by repetition
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            sb.append("abcxabcdabxabcdabcdabcy");
        }
        String text3 = sb.toString();
        String pattern3 = "abcdabcy";
        printMatches("Long example", text3, pattern3);
    }
}
