import java.util.*;

/**
 * Missions 3 and 4. Find a word in a long text.
 */
public class Marker {

    /**
     * Mission 3a. The prefix function of p. pi[i] = the length of the longest proper prefix of p[0..i] that is
     * also a suffix of p[0..i] ("proper" means shorter than p[0..i] itself).
     * Example: p = "ACGAC" gives [0, 0, 0, 1, 2]. The last number is the "Border length" line.
     */

    public static int[] prefixFunction(String p) {
        int[] pi = new int[p.length()];

        for (int i = 1; i < p.length(); i++) {
            int j = pi[i - 1];

            while (j > 0 && p.charAt(i) != p.charAt(j)) {
                j = pi[j - 1];
            }

            if (p.charAt(i) == p.charAt(j)) {
                j++;
            }

            pi[i] = j;
        }

        return pi;
    }


    /**
     * Mission 3b. KMP. All start positions (counting from 0, in increasing order) where p occurs in text.
     * Overlapping occurrences count: "ACG" occurs in "ACGACG" at 0 and 3, and "AA" in "AAA" at 0 and 1.
     * Use the prefix function: the text pointer never goes back. The number of positions is "Marker hits",
     * the first one is "First hit" (-1 when there is none).
     */

    public static int[] findAll(String text, String p) {
        if (p.isEmpty()) {
            return new int[0];
        }

        int[] pi = prefixFunction(p);
        List<Integer> found = new ArrayList<>();
        int j = 0;

        for (int i = 0; i < text.length(); i++) {
            while (j > 0 && text.charAt(i) != p.charAt(j)) {
                j = pi[j - 1];
            }

            if (text.charAt(i) == p.charAt(j)) {
                j++;
            }

            if (j == p.length()) {
                found.add(i - p.length() + 1);
                j = pi[j - 1];
            }
        }

        int[] result = new int[found.size()];

        for (int i = 0; i < result.length; i++) {
            result[i] = found.get(i);
        }

        return result;
    }

    /**
     * Mission 4. Rabin-Karp. The number of positions i in a such that the word of k letters starting at i
     * (a[i..i+k-1]) also occurs somewhere in b. Count positions, not different words. This is the "Shared words" line.
     * The idea: give every window of k letters a number (a rolling hash, updated in O(1) when the window moves by one),
     * look the number up among the windows of b, and when it is found COMPARE THE LETTERS, because two different words
     * can have the same hash.
     * Example: a = "ACGTAC", b = "TTACGT", k = 4: the words of a are ACGT, CGTA, GTAC; only ACGT is in b, so the answer is 1.
     */


    private static final long BASE = 31;
    private static final long MOD = 1000000007L;

    private static long firstHash(String s, int start, int k) {
        long hash = 0;

        for (int i = 0; i < k; i++) {
            hash = (hash * BASE + s.charAt(start + i)) % MOD;
        }

        return hash;
    }

    private static boolean sameLetters(String a, int ai,
                                       String b, int bi, int k) {
        for (int i = 0; i < k; i++) {
            if (a.charAt(ai + i) != b.charAt(bi + i)) {
                return false;
            }
        }
        return true;
    }

    public static int sharedWords(String a, String b, int k) {
        if (k <= 0 || a.length() < k || b.length() < k) {
            return 0;
        }

        long power = 1;

        for (int i = 1; i < k; i++) {
            power = (power * BASE) % MOD;
        }

        Map<Long, List<Integer>> words = new HashMap<>();

        long hashB = firstHash(b, 0, k);

        for (int i = 0; i + k <= b.length(); i++) {
            if (i > 0) {
                hashB = (hashB - b.charAt(i - 1) * power % MOD + MOD) % MOD;
                hashB = (hashB * BASE + b.charAt(i + k - 1)) % MOD;
            }

            words.computeIfAbsent(hashB, h -> new ArrayList<>()).add(i);
        }

        int count = 0;
        long hashA = firstHash(a, 0, k);

        for (int i = 0; i + k <= a.length(); i++) {
            if (i > 0) {
                hashA = (hashA - a.charAt(i - 1) * power % MOD + MOD) % MOD;
                hashA = (hashA * BASE + a.charAt(i + k - 1)) % MOD;
            }

            List<Integer> positions = words.get(hashA);

            if (positions == null) {
                continue;
            }

            for (int pos : positions) {
                if (sameLetters(a, i, b, pos, k)) {
                    count++;
                    break;
                }
            }
        }

        return count;
    }

}
