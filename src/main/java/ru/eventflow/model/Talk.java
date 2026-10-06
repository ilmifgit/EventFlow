package ru.eventflow.model;

/**
 * Доклад конференции. Используется в BR-3 (сортировка и поиск
 * по времени) и далее в BR-5, BR-6, BR-7.
 */
public class Talk implements Comparable<Talk> {

    private final int id;
    private final String title;
    private final int startMinute;   // начало доклада, минуты от начала дня
    private final int durationMin;   // длительность в минутах

    public Talk(int id, String title, int startMinute, int durationMin) {
        this.id = id;
        this.title = title;
        this.startMinute = startMinute;
        this.durationMin = durationMin;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public int getStartMinute() { return startMinute; }
    public int getEndMinute() { return startMinute + durationMin; }
    public int getDurationMin() { return durationMin; }

    /** Естественный порядок — по времени начала. */
    @Override
    public int compareTo(Talk other) {
        return Integer.compare(this.startMinute, other.startMinute);
    }

    @Override
    public String toString() {
        return String.format("ID %-5d | %02d:%02d–%02d:%02d | %-30s",
                id,
                startMinute / 60, startMinute % 60,
                getEndMinute() / 60, getEndMinute() % 60,
                title);
    }
}