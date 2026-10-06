package ru.eventflow.algorithms;

import java.math.BigInteger;

/**
 * BR-7: разбиение N докладов на блоки по 1, 2 или 3 подряд.
 * <p>Рекуррентность: f(0)=1, f(1)=1, f(2)=2, f(n)=f(n-1)+f(n-2)+f(n-3).
 * <ul>
 *   <li>{@link #naive} — рекурсия, O(3^n), годится только для маленьких n.</li>
 *   <li>{@link #memoized} — мемоизация, O(n) времени, O(n) памяти.</li>
 *   <li>{@link #iterative} — итеративный DP, O(n) времени, O(1) памяти.</li>
 * </ul>
 * Используется BigInteger, т.к. при n=60 значение превышает long.
 */
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
        BigInteger a = BigInteger.ONE;              // f(0)
        BigInteger b = BigInteger.ONE;              // f(1)
        BigInteger c = BigInteger.valueOf(2);       // f(2)
        for (int i = 3; i <= n; i++) {
            BigInteger next = a.add(b).add(c);
            a = b; b = c; c = next;
        }
        return c;
    }
}