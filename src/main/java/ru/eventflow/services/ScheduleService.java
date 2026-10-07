package ru.eventflow.services;

import ru.eventflow.algorithms.IntervalScheduling;
import ru.eventflow.algorithms.Knapsack;
import ru.eventflow.model.ScheduleTalk;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ScheduleService {

    private final List<ScheduleTalk> talks = new ArrayList<>();

    public void clear() { talks.clear(); }
    public List<ScheduleTalk> getTalks() { return talks; }
    public int size() { return talks.size(); }

    public void add(ScheduleTalk t) { talks.add(t); }

    public void loadDemo() {
        clear();
        add(new ScheduleTalk(1, "Ключевой доклад",  9 * 60, 180, 100));
        add(new ScheduleTalk(2, "Средний A",       11 * 60, 120,  60)); 
        add(new ScheduleTalk(3, "Средний B",       13 * 60, 120,  55)); 
        add(new ScheduleTalk(4, "Короткий A",      15 * 60,  60,  35)); 
        add(new ScheduleTalk(5, "Короткий B",      16 * 60,  60,  34)); 
        add(new ScheduleTalk(6, "Короткий C",      17 * 60,  60,  33));
    }

    public void loadRandom(int n, long seed) {
        clear();
        Random rnd = new Random(seed);
        int start = 9 * 60;
        for (int i = 1; i <= n; i++) {
            int dur = 30 + rnd.nextInt(90);
            int interest = 10 + rnd.nextInt(100);
            add(new ScheduleTalk(i, "Talk-" + i, start, dur, interest));
            start += dur + rnd.nextInt(30);
        }
    }

    public Knapsack.Result solveKnapsackOptimal(int limitMin) {
        return Knapsack.solveOptimal(talks, limitMin);
    }

    public Knapsack.Result solveKnapsackFast(int limitMin) {
        return Knapsack.solveFast(talks, limitMin);
    }

    public List<ScheduleTalk> selectNonOverlapping() {
        return IntervalScheduling.selectMax(talks);
    }
}
