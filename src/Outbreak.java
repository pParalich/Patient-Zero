public class Outbreak {

    public static boolean[] cheapestTests(int n, int[][] nb, int[] cost) {
        boolean[] chosen = new boolean[n];

        if (n == 0) {
            return chosen;
        }

        long[][] dp = new long[n][2];

        calculate(0, -1, nb, cost, dp);

        boolean rootTested = dp[0][1] <= dp[0][0];
        restore(0, -1, rootTested, nb, dp, chosen);

        return chosen;
    }

    private static void calculate(int v, int parent,
                                  int[][] nb, int[] cost, long[][] dp) {
        dp[v][0] = 0;
        dp[v][1] = cost[v];

        for (int child : nb[v]) {
            if (child == parent) {
                continue;
            }

            calculate(child, v, nb, cost, dp);

            dp[v][0] += dp[child][1];
            dp[v][1] += Math.min(dp[child][0], dp[child][1]);
        }
    }

    private static void restore(int v, int parent, boolean test,
                                int[][] nb, long[][] dp, boolean[] chosen) {
        chosen[v] = test;

        for (int child : nb[v]) {
            if (child == parent) {
                continue;
            }

            boolean testChild = !test || dp[child][1] <= dp[child][0];

            restore(child, v, testChild, nb, dp, chosen);
        }
    }

    public static int longestChain(int n, int[][] nb) {
        if (n == 0) {
            return 0;
        }

        int[] longest = {0};

        depth(0, -1, nb, longest);

        return longest[0];
    }

    private static int depth(int v, int parent,
                             int[][] nb, int[] longest) {
        int first = 0;
        int second = 0;

        for (int child : nb[v]) {
            if (child == parent) {
                continue;
            }

            int length = 1 + depth(child, v, nb, longest);

            if (length > first) {
                second = first;
                first = length;
            } else if (length > second) {
                second = length;
            }
        }

        longest[0] = Math.max(longest[0], first + second);

        return first;
    }
}
