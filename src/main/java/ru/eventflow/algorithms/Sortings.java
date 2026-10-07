package ru.eventflow.algorithms;

import java.util.Comparator;

public final class Sortings {

    private Sortings() { }

    public static <T> long insertionSort(T[] a, Comparator<? super T> cmp) {
        long comparisons = 0;
        for (int i = 1; i < a.length; i++) {
            T key = a[i];
            int j = i - 1;
            while (j >= 0) {
                comparisons++;
                if (cmp.compare(a[j], key) > 0) {
                    a[j + 1] = a[j];
                    j--;
                } else {
                    break;
                }
            }
            a[j + 1] = key;
        }
        return comparisons;
    }

    public static <T> long mergeSort(T[] a, Comparator<? super T> cmp) {
        if (a.length < 2) return 0;
        @SuppressWarnings("unchecked")
        T[] buffer = (T[]) new Object[a.length];
        return mergeSort(a, buffer, 0, a.length - 1, cmp);
    }

    private static <T> long mergeSort(T[] a, T[] buf, int lo, int hi, Comparator<? super T> cmp) {
        if (lo >= hi) return 0;
        int mid = (lo + hi) >>> 1;
        long c = 0;
        c += mergeSort(a, buf, lo, mid, cmp);
        c += mergeSort(a, buf, mid + 1, hi, cmp);
        c += merge(a, buf, lo, mid, hi, cmp);
        return c;
    }

    private static <T> long merge(T[] a, T[] buf, int lo, int mid, int hi, Comparator<? super T> cmp) {
        long comparisons = 0;
        System.arraycopy(a, lo, buf, lo, hi - lo + 1);
        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid) {
                a[k] = buf[j++];
            } else if (j > hi) {
                a[k] = buf[i++];
            } else {
                comparisons++;
                if (cmp.compare(buf[j], buf[i]) < 0) {
                    a[k] = buf[j++];
                } else {
                    a[k] = buf[i++];
                }
            }
        }
        return comparisons;
    }
}
