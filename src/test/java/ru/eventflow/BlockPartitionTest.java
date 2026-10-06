package ru.eventflow;

import org.junit.jupiter.api.Test;
import ru.eventflow.algorithms.BlockPartition;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class BlockPartitionTest {

    @Test void baseCases() {
        assertEquals(BigInteger.ONE, BlockPartition.iterative(0));
        assertEquals(BigInteger.ONE, BlockPartition.iterative(1));
        assertEquals(BigInteger.valueOf(2), BlockPartition.iterative(2));
    }

    @Test void threeImpls_agreeOnSmallN() {
        for (int n = 0; n <= 20; n++) {
            assertEquals(BlockPartition.naive(n), BlockPartition.memoized(n));
            assertEquals(BlockPartition.naive(n), BlockPartition.iterative(n));
        }
    }

    @Test void iterativeN60_ok() {
        assertNotNull(BlockPartition.iterative(60));
    }

    @Test void negative_zero() {
        assertEquals(BigInteger.ZERO, BlockPartition.iterative(-5));
    }
}