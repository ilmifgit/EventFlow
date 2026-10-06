package ru.eventflow.services;

import ru.eventflow.algorithms.SlidingWindow;
import ru.eventflow.algorithms.TwoPointers;

import java.util.Random;

/**
 * BR-6: аналитика посещаемости — окно k и пара с суммой около target.
 */
public class AnalyticsService {

    private long[] perMinute = new long[0];
    private long[] experience = new long[0];

    public void setPerMinute(long[] values) { this.perMinute = values == null ? new long[0] : values; }
    public void setExperience(long[] values) { this.experience = values == null ? new long[0] : values; }

    public long[] getPerMinute() { return perMinute; }
    public long[] getExperience() { return experience; }
    public int sizePerMinute() { return perMinute.length; }
    public int sizeExperience() { return experience.length; }

    public void generatePerMinute(int n, long seed) {
        Random rnd = new Random(seed);
        perMinute = new long[n];
        for (int i = 0; i < n; i++) perMinute[i] = rnd.nextInt(50);
    }

    public void generateExperience(int n, long seed) {
        Random rnd = new Random(seed);
        experience = new long[n];
        for (int i = 0; i < n; i++) experience[i] = rnd.nextInt(40);
    }

    public SlidingWindow.Result peakWindow(int k) {
        return SlidingWindow.maxSumK(perMinute, k);
    }

    public TwoPointers.Pair closestExperiencePair(long target) {
        return TwoPointers.closestPair(experience, target);
    }
}