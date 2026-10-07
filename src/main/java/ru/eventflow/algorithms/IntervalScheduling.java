package ru.eventflow.algorithms;

import ru.eventflow.model.ScheduleTalk;

import java.util.ArrayList;
import java.util.List;

public final class IntervalScheduling {

    private IntervalScheduling() { }

    public static List<ScheduleTalk> selectMax(List<ScheduleTalk> talks) {
        List<ScheduleTalk> sorted = new ArrayList<>(talks);
        sorted.sort(null); 
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
