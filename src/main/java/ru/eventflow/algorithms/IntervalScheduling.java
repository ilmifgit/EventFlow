package ru.eventflow.algorithms;

import ru.eventflow.model.ScheduleTalk;

import java.util.ArrayList;
import java.util.List;

/**
 * Задача о выборе максимального числа докладов без пересечений (BR-5, вторая задача).
 * Жадный алгоритм: сортируем по времени окончания и берём те, что начинаются
 * не раньше конца последнего взятого. Оптимален по теореме об обмене.
 * Сложность: O(n log n).
 */
public final class IntervalScheduling {

    private IntervalScheduling() { }

    public static List<ScheduleTalk> selectMax(List<ScheduleTalk> talks) {
        List<ScheduleTalk> sorted = new ArrayList<>(talks);
        sorted.sort(null); // по getEndMin() через compareTo
        List<ScheduleTalk> chosen = new ArrayList<>();
        int lastEnd = -1;
        for (ScheduleTalk t : sorted) {
            if (t.getStartMin() >= lastEnd) {
                chosen.add(t);
                lastEnd = t.getEndMin();
            }
        }
        return chosen;
    }
}