package ru.eventflow.model;

/**
 * Доклад для задач BR-5: у него есть длительность (для рюкзака),
 * интерес (ценность для рюкзака) и интервал времени (для расписания без пересечений).
 */
public class ScheduleTalk implements Comparable<ScheduleTalk> {

    private final int id;
    private final String title;
    private final int startMin;
    private final int durationMin;
    private final int interest;

    public ScheduleTalk(int id, String title, int startMin, int durationMin, int interest) {
        this.id = id;
        this.title = title;
        this.startMin = startMin;
        this.durationMin = durationMin;
        this.interest = interest;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public int getStartMin() { return startMin; }
    public int getEndMin() { return startMin + durationMin; }
    public int getDurationMin() { return durationMin; }
    public int getInterest() { return interest; }

    /** Естественный порядок — по времени окончания (для interval scheduling). */
    @Override public int compareTo(ScheduleTalk o) {
        return Integer.compare(this.getEndMin(), o.getEndMin());
    }

    @Override public String toString() {
        return String.format("ID %-4d | %02d:%02d–%02d:%02d | длит. %3d мин | интерес %d | %s",
                id, startMin / 60, startMin % 60,
                getEndMin() / 60, getEndMin() % 60,
                durationMin, interest, title);
    }
}