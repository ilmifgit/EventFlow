package ru.eventflow.model;

public class Talk implements Comparable<Talk> {

    private final int id;
    private final String title;
    private final int startMinute;  
    private final int durationMin;  

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
