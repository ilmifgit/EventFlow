package ru.eventflow;

import org.junit.jupiter.api.Test;
import ru.eventflow.algorithms.Knapsack;
import ru.eventflow.model.ScheduleTalk;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KnapsackTest {

    @Test void greedyLosesToDp() {
        List<ScheduleTalk> talks = List.of(
                new ScheduleTalk(1, "A",  0, 180, 100),
                new ScheduleTalk(2, "B1", 0, 120,  60),
                new ScheduleTalk(3, "B2", 0, 120,  55)
        );
        Knapsack.Result opt  = Knapsack.solveOptimal(talks, 240);
        Knapsack.Result fast = Knapsack.solveFast(talks, 240);
        assertEquals(115, opt.totalInterest);
        assertTrue(fast.totalInterest <= opt.totalInterest);
    }

    @Test void empty_zero() {
        Knapsack.Result r = Knapsack.solveOptimal(List.of(), 100);
        assertEquals(0, r.totalInterest);
    }

    @Test void zeroLimit_zero() {
        List<ScheduleTalk> talks = List.of(new ScheduleTalk(1, "X", 0, 60, 50));
        assertEquals(0, Knapsack.solveOptimal(talks, 0).totalInterest);
    }
}