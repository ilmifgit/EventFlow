package ru.eventflow.algorithms;

/**
 * Скользящее окно фиксированной длины. Сложность O(n), O(1) доп. памяти.
 */
public final class SlidingWindow {

    private SlidingWindow() { }

    public static class Result {
        public final int startIndex;
        public final long sum;
        public Result(int s, long v) { startIndex = s; sum = v; }
    }

    /** Максимальная сумма k подряд идущих элементов. */
    public static Result maxSumK(long[] a, int k) {
        if (a == null || a.length == 0 || k <= 0 || k > a.length) return null;
        long sum = 0;
        for (int i = 0; i < k; i++) sum += a[i];
        long best = sum;
        int bestStart = 0;
        for (int i = k; i < a.length; i++) {
            sum += a[i] - a[i - k];
            if (sum > best) { best = sum; bestStart = i - k + 1; }
        }
        return new Result(bestStart, best);
    }
}