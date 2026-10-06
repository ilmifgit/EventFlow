package ru.eventflow;

import org.junit.jupiter.api.Test;
import ru.eventflow.algorithms.TwoPointers;

import static org.junit.jupiter.api.Assertions.*;

class TwoPointersTest {

    @Test void exactMatch() {
        TwoPointers.Pair p = TwoPointers.closestPair(new long[]{ 1, 2, 3, 4, 5, 6 }, 7);
        assertEquals(7, p.sum);
        assertEquals(0, p.diff);
    }

    @Test void closestWhenExactImpossible() {
        TwoPointers.Pair p = TwoPointers.closestPair(new long[]{ 1, 10, 20, 30 }, 15);
        assertEquals(11, p.sum);
        assertEquals(4, p.diff);
    }

    @Test void edgeCases_null() {
        assertNull(TwoPointers.closestPair(new long[]{ 5 }, 10));
        assertNull(TwoPointers.closestPair(new long[]{}, 10));
    }
}