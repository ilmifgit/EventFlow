package ru.eventflow.algorithms;

import ru.eventflow.model.ScheduleTalk;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Задача 0/1 рюкзака для BR-5.
 * <ul>
 *   <li>{@link #solveOptimal} — DP, гарантированный максимум интереса, O(n·W).</li>
 *   <li>{@link #solveFast} — жадный по отношению интерес/длительность,
 *       быстрый, но в общем случае неоптимальный.</li>
 * </ul>
 * На тестовых данных видно, где жадный проигрывает DP.
 */
public final class Knapsack {

    private Knapsack() { }

    public static class Result {
        public final int totalInterest;
        public final int totalDuration;
        public final List<ScheduleTalk> chosen;
        public Result(int totalInterest, int totalDuration, List<ScheduleTalk> chosen) {
            this.totalInterest = totalInterest;
            this.totalDuration = totalDuration;
            this.chosen = chosen;
        }
    }

    /** DP на максимум интереса при ограничении времени. */
    public static Result solveOptimal(List<ScheduleTalk> talks, int limitMin) {
        int n = talks.size();
        int[] dp = new int[limitMin + 1];
        // Восстановление выбора: keep[i][w] = true, если i-й доклад взят при лимите w
        boolean[][] keep = new boolean[n + 1][limitMin + 1];
        for (int i = 1; i <= n; i++) {
            ScheduleTalk t = talks.get(i - 1);
            int w = t.getDurationMin();
            int v = t.getInterest();
            for (int cap = limitMin; cap >= 0; cap--) {
                if (w <= cap && dp[cap - w] + v > dp[cap]) {
                    dp[cap] = dp[cap - w] + v;
                    keep[i][cap] = true;
                }
            }
        }
        // Восстановление
        List<ScheduleTalk> chosen = new ArrayList<>();
        int cap = limitMin;
        for (int i = n; i >= 1; i--) {
            if (keep[i][cap]) {
                chosen.add(talks.get(i - 1));
                cap -= talks.get(i - 1).getDurationMin();
            }
        }
        int dur = 0;
        for (ScheduleTalk t : chosen) dur += t.getDurationMin();
        return new Result(dp[limitMin], dur, chosen);
    }

    /** Жадный «быстрый» режим: сортируем по убыванию interest/duration и набираем, пока влезает. */
    public static Result solveFast(List<ScheduleTalk> talks, int limitMin) {
        List<ScheduleTalk> sorted = new ArrayList<>(talks);
        sorted.sort(Comparator.comparingDouble(
                (ScheduleTalk t) -> (double) t.getInterest() / t.getDurationMin()).reversed());
        List<ScheduleTalk> chosen = new ArrayList<>();
        int remaining = limitMin;
        int totalInterest = 0;
        for (ScheduleTalk t : sorted) {
            if (t.getDurationMin() <= remaining) {
                chosen.add(t);
                remaining -= t.getDurationMin();
                totalInterest += t.getInterest();
            }
        }
        return new Result(totalInterest, limitMin - remaining, chosen);
    }
}