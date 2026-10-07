package ru.eventflow.algorithms;

import java.math.BigInteger;

public final class BlockPartition {

    private BlockPartition() { }

    public static BigInteger naive(int n) {
        if (n < 0) return BigInteger.ZERO;
        if (n == 0 || n == 1) return BigInteger.ONE;
        if (n == 2) return BigInteger.valueOf(2);
        return naive(n - 1).add(naive(n - 2)).add(naive(n - 3));
    }

    public static BigInteger memoized(int n) {
        BigInteger[] memo = new BigInteger[Math.max(n + 1, 4)];
        for (int i = 0; i < memo.length; i++) memo[i] = null;
        memo[0] = BigInteger.ONE;
        memo[1] = BigInteger.ONE;
        memo[2] = BigInteger.valueOf(2);
        return memoRec(n, memo);
    }

    private static BigInteger memoRec(int n, BigInteger[] memo) {
        if (memo[n] != null) return memo[n];
        BigInteger v = memoRec(n - 1, memo).add(memoRec(n - 2, memo)).add(memoRec(n - 3, memo));
        memo[n] = v;
        return v;
    }

    public static BigInteger iterative(int n) {
        if (n < 0) return BigInteger.ZERO;
        if (n == 0 || n == 1) return BigInteger.ONE;
        if (n == 2) return BigInteger.valueOf(2);
        BigInteger a = BigInteger.ONE;             
        BigInteger b = BigInteger.ONE;             
        BigInteger c = BigInteger.valueOf(2);      
        for (int i = 3; i <= n; i++) {
            BigInteger next = a.add(b).add(c);
            a = b; b = c; c = next;
        }
        return c;
    }
}
