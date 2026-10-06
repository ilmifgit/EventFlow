package ru.eventflow.services;

import ru.eventflow.algorithms.BinarySearch;
import ru.eventflow.algorithms.Sortings;
import ru.eventflow.model.Talk;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * BR-3: сортировка списков докладов и быстрый поиск интервала времени.
 * Для больших массивов используется сортировка слиянием (O(n log n)),
 * для малых — вставками. Выбор подтверждается замерами в {@link #benchmark(int[])}.
 */
public class ListService {

    private Talk[] talks = new Talk[0];

    /** Установить список докладов (например, сгенерированный). */
    public void setTalks(Talk[] talks) {
        this.talks = talks == null ? new Talk[0] : talks;
    }

    public Talk[] getTalks() {
        return talks;
    }

    public int size() { return talks.length; }

    /**
     * Сортирует доклады по времени начала.
     * Для n < 64 — вставками, иначе — слиянием.
     * @return число сравнений
     */
    public long sortByTime() {
        if (talks.length < 2) return 0;
        Comparator<Talk> cmp = Comparator.naturalOrder();
        if (talks.length < 64) {
            return Sortings.insertionSort(talks, cmp);
        } else {
            return Sortings.mergeSort(talks, cmp);
        }
    }

    /**
     * Возвращает список докладов, чьё время начала попадает в [from, to] (включительно).
     * Использует lower/upper bound на отсортированном массиве, без полного перебора.
     * Время: O(log n + k), где k — размер результата.
     */
    public List<Talk> findInInterval(int from, int to) {
        if (from > to) {
            int tmp = from; from = to; to = tmp;
        }
        Comparator<Talk> byStart = Comparator.comparingInt(Talk::getStartMinute);
        // фиктивные границы
        Talk fromKey = new Talk(-1, "", from, 0);
        Talk toKey   = new Talk(-1, "", to, 0);

        int lo = BinarySearch.lowerBound(talks, fromKey, byStart);
        int hi = BinarySearch.upperBound(talks, toKey, byStart);

        List<Talk> result = new ArrayList<>();
        for (int i = lo; i < hi; i++) {
            result.add(talks[i]);
        }
        return result;
    }

    /**
     * Замеры производительности на массивах размера n.
     * Возвращает строки отчёта.
     */
    public List<String> benchmark(int[] sizes) {
        List<String> report = new ArrayList<>();
        report.add(String.format("%-10s | %-14s | %-14s | %-14s",
                "Размер", "Вставками(мс)", "Слиянием(мс)", "Сравнений(млн)"));
        report.add("-".repeat(60));

        for (int n : sizes) {
            // Копии одного и того же набора
            Talk[] base = DataGeneratorForBench.generate(n, 42L);

            Talk[] forInsertion = base.clone();
            Talk[] forMerge     = base.clone();

            long t1 = System.nanoTime();
            long c1 = Sortings.insertionSort(forInsertion, Comparator.naturalOrder());
            long t2 = System.nanoTime();

            long t3 = System.nanoTime();
            long c2 = Sortings.mergeSort(forMerge, Comparator.naturalOrder());
            long t4 = System.nanoTime();

            report.add(String.format("%-10d | %-14.2f | %-14.2f | %-14.2f",
                    n,
                    (t2 - t1) / 1_000_000.0,
                    (t4 - t3) / 1_000_000.0,
                    Math.max(c1, c2) / 1_000_000.0));
        }
        return report;
    }

    /**
     * Вспомогательный класс, чтобы не тянуть DataGenerator из io в algorithms.
     */
    private static final class DataGeneratorForBench {
        static Talk[] generate(int n, long seed) {
            java.util.Random rnd = new java.util.Random(seed);
            Talk[] arr = new Talk[n];
            for (int i = 0; i < n; i++) {
                arr[i] = new Talk(i + 1, "T" + i, rnd.nextInt(23 * 60), 60);
            }
            return arr;
        }
    }
}