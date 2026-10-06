package ru.eventflow.structures;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Собственный ориентированный взвешенный граф на списках смежности.
 * Готовые графовые библиотеки запрещены, поэтому структура пишется вручную.
 * <p>Память: O(V + E). Списки смежности выбраны вместо матрицы, потому что
 * граф площадки разреженный.
 */
public class MyGraph {

    public static class Edge {
        public final int to;
        public final int weight;
        public Edge(int to, int weight) { this.to = to; this.weight = weight; }
    }

    private final Map<Integer, List<Edge>> adj = new LinkedHashMap<>();

    public void addVertex(int v) { adj.putIfAbsent(v, new ArrayList<>()); }

    public void addEdge(int from, int to, int weight) {
        addVertex(from);
        addVertex(to);
        adj.get(from).add(new Edge(to, weight));
    }

    public Set<Integer> vertices() { return adj.keySet(); }
    public List<Edge> neighbors(int v) { return adj.getOrDefault(v, List.of()); }
    public boolean hasVertex(int v) { return adj.containsKey(v); }
    public int vertexCount() { return adj.size(); }
    public int edgeCount() {
        int sum = 0;
        for (List<Edge> list : adj.values()) sum += list.size();
        return sum;
    }
}