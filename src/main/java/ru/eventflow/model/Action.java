package ru.eventflow.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Action {

    public enum Type {
        ADD_TALK("Добавление доклада"),
        MOVE_TALK("Перенос доклада"),
        CANCEL_TALK("Отмена доклада");

        private final String label;
        Type(String label) { this.label = label; }
        public String getLabel() { return label; }
    }

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm:ss");

    private final int id;
    private final Type type;
    private final String description;
    private final LocalDateTime createdAt;

    public Action(int id, Type type, String description) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.createdAt = LocalDateTime.now();
    }

    public int getId() { return id; }
    public Type getType() { return type; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return String.format("#%d [%s] %s (%s)",
                id, type.getLabel(), description, createdAt.format(FORMATTER));
    }
}
