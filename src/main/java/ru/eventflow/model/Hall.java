package ru.eventflow.model;

public class Hall {
    private final int id;
    private final String name;
    public Hall(int id, String name) { this.id = id; this.name = name; }
    public int getId() { return id; }
    public String getName() { return name; }
    @Override public String toString() { return "Зал " + id + " (" + name + ")"; }
}
