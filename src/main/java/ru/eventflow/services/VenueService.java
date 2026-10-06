package ru.eventflow.services;

import ru.eventflow.model.Hall;
import ru.eventflow.structures.MyGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * BR-4: схема площадки. Обходы, изолированные группы, циклы, Дейкстра.
 */
public class VenueService {

    private final MyGraph graph = new MyGraph();
    private final List<Hall> halls = new ArrayList<>();
    private int entryHall = -1;

    public void clear() { halls.clear(); }
    public MyGraph getGraph() { return graph; }
    public List<Hall> getHalls() { return halls; }
    public int getEntryHall() { return entryHall; }

    public void addHall(Hall hall) { halls.add(hall); }

    public void addTransition(int from, int to, int minutes) {
        graph.addEdge(from, to, minutes);
    }

    public void setEntryHall(int id) { this.entryHall = id; }

    /** Демо-граф с известной структурой для проверки. */
    public void loadDemo() {
        clear();
        graph.addVertex(1); // вход
        addHall(new Hall(1, "Главный вход"));
        addHall(new Hall(2, "Зал A"));
        addHall(new Hall(3, "Зал B"));
        addHall(new Hall(4, "Зал C"));
        addHall(new Hall(5, "Зал D"));
        addHall(new Hall(6, "Изолированный зал E"));
        for (int i = 2; i <= 6; i++) graph.addVertex(i);
        addTransition(1, 2, 5);
        addTransition(1, 3, 10);
        addTransition(2, 3, 3);
        addTransition(3, 4, 2);
        addTransition(4, 2, 1);   // цикл: 2 -> 3 -> 4 -> 2
        addTransition(2, 5, 8);
        setEntryHall(1);
    }

    public String describeHall(int id) {
        for (Hall h : halls) if (h.getId() == id) return h.toString();
        return "Зал " + id;
    }

    public Map<Integer, Integer> shortestFromEntry() {
        return ru.eventflow.algorithms.GraphAlgorithms.dijkstra(graph, entryHall);
    }
}