package ru.eventflow;

import org.junit.jupiter.api.Test;
import ru.eventflow.algorithms.BinarySearch;
import ru.eventflow.algorithms.Sortings;

import java.util.Arrays;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class SortingsTest {

    @Test void insertionAndMergeSort_correct() {
        Integer[] a = { 5, 2, 8, 1, 9, 3 };
        Sortings.insertionSort(a, Comparator.naturalOrder());
        assertArrayEquals(new Integer[]{ 1, 2, 3, 5, 8, 9 }, a);

        Integer[] b = { 5, 2, 8, 1, 9, 3 };
        Sortings.mergeSort(b, Comparator.naturalOrder());
        assertArrayEquals(new Integer[]{ 1, 2, 3, 5, 8, 9 }, b);
    }

    @Test void emptyAndSingle_ok() {
        Integer[] empty = {};
        Sortings.mergeSort(empty, Comparator.naturalOrder());
        Integer[] one = { 42 };
        Sortings.mergeSort(one, Comparator.naturalOrder());
        assertArrayEquals(new Integer[]{ 42 }, one);
    }

    @Test void matchesJavaSort() {
        Integer[] a = { 7, 3, 9, 1, 4, 8, 2, 6, 5, 0 };
        Integer[] expected = a.clone();
        Arrays.sort(expected);
        Sortings.mergeSort(a, Comparator.naturalOrder());
        assertArrayEquals(expected, a);
    }

    @Test void lowerUpperBound_correct() {
        Integer[] a = { 1, 3, 3, 5, 7, 7, 7, 9 };
        Comparator<Integer> cmp = Comparator.naturalOrder();

        assertEquals(1, BinarySearch.lowerBound(a, 3, cmp));   // первый >= 3
        assertEquals(3, BinarySearch.upperBound(a, 3, cmp));   // первый > 3

        assertEquals(0, BinarySearch.lowerBound(a, 0, cmp));   // меньше минимума
        assertEquals(a.length, BinarySearch.upperBound(a, 100, cmp)); // больше максимума

        int p = BinarySearch.indexOf(a, 7, cmp);
        assertTrue(p >= 4 && p <= 6);                          // любой из 4/5/6
        assertEquals(7, a[p]);

        assertEquals(-1, BinarySearch.indexOf(a, 4, cmp));     // нет такого
    }
}