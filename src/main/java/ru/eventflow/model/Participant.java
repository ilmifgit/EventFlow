package ru.eventflow.model;

/**
 * Участник конференции. У каждого уникальный ID.
 * Используется в реестре (BR-2), построенном на собственном BST.
 */
public class Participant {

    private final int id;
    private final String name;
    private final int experienceYears;

    public Participant(int id, String name, int experienceYears) {
        this.id = id;
        this.name = name;
        this.experienceYears = experienceYears;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getExperienceYears() { return experienceYears; }

    @Override
    public String toString() {
        return String.format("ID %-6d | %-25s | опыт %d лет",
                id, name, experienceYears);
    }
}