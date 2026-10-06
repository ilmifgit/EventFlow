package ru.eventflow.algorithms;

import java.util.Comparator;

/**
 * Бинарный поиск и его варианты (lower/upper bound).
 * Запрещено использовать Arrays.binarySearch и Collections.binarySearch —
 * они допускаются только в тестах для проверки результата.
 */
public final class BinarySearch {

    private BinarySearch() { }

    /**
     * Индекс первого элемента, который >= key (lower bound).
     * @return индекс в [0, n], где n = a.length
     */
    public static <T> int lowerBound(T[] a, T key, Comparator<? super T> cmp) {
        int lo = 0, hi = a.length;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (cmp.compare(a[mid], key) < 0) lo = mid + 1;
            else hi = mid;
        }
        return lo;
    }

    /**
     * Индекс первого элемента, который > key (upper bound).
     */
    public static <T> int upperBound(T[] a, T key, Comparator<? super T> cmp) {
        int lo = 0, hi = a.length;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (cmp.compare(a[mid], key) <= 0) lo = mid + 1;
            else hi = mid;
        }
        return lo;
    }

    /**
     * Классический бинарный поиск. Возвращает индекс или -1.
     */
    public static <T> int indexOf(T[] a, T key, Comparator<? super T> cmp) {
        int lo = 0, hi = a.length - 1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            int c = cmp.compare(a[mid], key);
            if (c < 0) lo = mid + 1;
            else if (c > 0) hi = mid - 1;
            else return mid;
        }
        return -1;
    }
}