package ru.eventflow;

import org.junit.jupiter.api.Test;
import ru.eventflow.algorithms.SlidingWindow;

import static org.junit.jupiter.api.Assertions.*;

class SlidingWindowTest {

    @Test void maxSumK_correct() {
        long[] a = { 1, 4, 2, 10, 23, 3, 1, 0, 20 };
        SlidingWindow.Result r = SlidingWindow.maxSumK(a, 4);
        assertEquals(39, r.sum);
        assertEquals(1, r.startIndex);
    }

    @Test void kEqualsLength() {
        SlidingWindow.Result r = SlidingWindow.maxSumK(new long[]{ 1, 2, 3 }, 3);
        assertEquals(6, r.sum);
    }

    @Test void edgeCases_null() {
        assertNull(SlidingWindow.maxSumK(new long[]{ 1, 2 }, 5));
        assertNull(SlidingWindow.maxSumK(new long[]{}, 3));
        assertNull(SlidingWindow.maxSumK(new long[]{ 1, 2, 3 }, 0));
    }
}