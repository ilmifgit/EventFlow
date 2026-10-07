package ru.eventflow.algorithms;

import java.util.Arrays;
import java.util.Comparator;

public final class TwoPointers {

    private TwoPointers() { }

    public static class Pair {
        public final int i, j;  
        public final long sum;
        public final long diff;
        public Pair(int i, int j, long sum, long diff) {
            this.i = i; this.j = j; this.sum = sum; this.diff = diff;
        }
    }

    public static Pair closestPair(long[] a, long target) {
        if (a == null || a.length < 2) return null;
        int n = a.length;
        Integer[] idx = new Integer[n];
        for (int i = 0; i < n; i++) idx[i] = i;
        mergeSortIdx(idx, a, 0, n - 1);

        int l = 0, r = n - 1;
        long bestDiff = Long.MAX_VALUE, bestSum = 0;
        int bi = idx[0], bj = idx[n - 1];
        while (l < r) {
            long sum = a[idx[l]] + a[idx[r]];
            long diff = Math.abs(sum - target);
            if (diff < bestDiff) {
                bestDiff = diff; bestSum = sum;
                bi = idx[l]; bj = idx[r];
            }
            if (sum < target) l++;
            else if (sum > target) r--;
            else break;
        }
        return new Pair(bi, bj, bestSum, bestDiff);
    }

    private static void mergeSortIdx(Integer[] idx, long[] a, int lo, int hi) {
        if (lo >= hi) return;
        int mid = (lo + hi) >>> 1;
        mergeSortIdx(idx, a, lo, mid);
        mergeSortIdx(idx, a, mid + 1, hi);
        Integer[] buf = new Integer[hi - lo + 1];
        int i = lo, j = mid + 1, k = 0;
        while (i <= mid && j <= hi) {
            if (a[idx[i]] <= a[idx[j]]) buf[k++] = idx[i++];
            else buf[k++] = idx[j++];
        }
        while (i <= mid) buf[k++] = idx[i++];
        while (j <= hi) buf[k++] = idx[j++];
        System.arraycopy(buf, 0, idx, lo, buf.length);
    }
}
