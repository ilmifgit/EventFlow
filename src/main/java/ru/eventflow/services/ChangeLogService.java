package ru.eventflow.services;

import ru.eventflow.model.Action;
import ru.eventflow.structures.MyStack;

public class ChangeLogService {

    private final MyStack<Action> history = new MyStack<>();
    private int nextId = 1;

    public Action log(Action.Type type, String description) {
        Action action = new Action(nextId++, type, description);
        history.push(action);
        return action;
    }

    public Action undo() {
        return history.pop();
    }

    public void printHistory() {
        if (history.isEmpty()) {
            System.out.println("  Журнал пуст.");
            return;
        }
        System.out.println("  Последние действия (сверху — самые новые):");
        for (Action a : history) {
            System.out.println("   " + a);
        }
    }

    public int size() { return history.size(); }
    public boolean isEmpty() { return history.isEmpty(); }
}
