package ru.eventflow.algorithms;

import ru.eventflow.structures.MyGraph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Алгоритмы на графах для BR-4: BFS, DFS, поиск циклов,
 * компоненты слабой связности, Дейкстра.
 * <p>Для очереди BFS используется ArrayDeque, для Дейкстры — PriorityQueue
 * (оба разрешены условиями). Граф — собственный {@link MyGraph}.
 */
public final class GraphAlgorithms {

    private GraphAlgorithms() { }

    /** BFS от start. Возвращает порядок обхода. */
    public static List<Integer> bfs(MyGraph g, int start) {
        List<Integer> order = new ArrayList<>();
        if (!g.hasVertex(start)) return order;
        Set<Integer> visited = new HashSet<>();
        Deque<Integer> queue = new ArrayDeque<>();
        visited.add(start);
        queue.add(start);
        while (!queue.isEmpty()) {
            int v = queue.poll();
            order.add(v);
            for (MyGraph.Edge e : g.neighbors(v)) {
                if (visited.add(e.to)) queue.add(e.to);
            }
        }
        return order;
    }

    /** DFS от start. Возвращает порядок обхода. */
    public static List<Integer> dfs(MyGraph g, int start) {
        List<Integer> order = new ArrayList<>();
        if (!g.hasVertex(start)) return order;
        Set<Integer> visited = new HashSet<>();
        dfsRec(g, start, visited, order);
        return order;
    }

    private static void dfsRec(MyGraph g, int v, Set<Integer> visited, List<Integer> order) {
        visited.add(v);
        order.add(v);
        for (MyGraph.Edge e : g.neighbors(v)) {
            if (!visited.contains(e.to)) dfsRec(g, e.to, visited, order);
        }
    }

    /**
     * Обнаружение цикла в ориентированном графе.
     * Цвета: 0 — белый, 1 — серый (в текущем пути), 2 — чёрный.
     */
    public static boolean hasCycle(MyGraph g) {
        Map<Integer, Integer> color = new HashMap<>();
        for (int v : g.vertices()) color.put(v, 0);
        for (int v : g.vertices()) {
            if (color.get(v) == 0 && dfsCycle(g, v, color)) return true;
        }
        return false;
    }

    private static boolean dfsCycle(MyGraph g, int v, Map<Integer, Integer> color) {
        color.put(v, 1);
        for (MyGraph.Edge e : g.neighbors(v)) {
            int c = color.getOrDefault(e.to, 0);
            if (c == 1) return true;
            if (c == 0 && dfsCycle(g, e.to, color)) return true;
        }
        color.put(v, 2);
        return false;
    }

    /**
     * Число компонент слабой связности. Все рёбра считаются неориентированными.
     */
    public static int weakComponentCount(MyGraph g) {
        Map<Integer, List<Integer>> undirected = new HashMap<>();
        for (int v : g.vertices()) undirected.putIfAbsent(v, new ArrayList<>());
        for (int v : g.vertices()) {
            for (MyGraph.Edge e : g.neighbors(v)) {
                undirected.get(v).add(e.to);
                undirected.get(e.to).add(v);
            }
        }
        Set<Integer> visited = new HashSet<>();
        int components = 0;
        for (int v : g.vertices()) {
            if (visited.add(v)) {
                components++;
                Deque<Integer> stack = new ArrayDeque<>();
                stack.push(v);
                while (!stack.isEmpty()) {
                    int u = stack.pop();
                    for (int w : undirected.get(u)) {
                        if (visited.add(w)) stack.push(w);
                    }
                }
            }
        }
        return components;
    }

    /**
     * Кратчайшие расстояния от start алгоритмом Дейкстры.
     * Веса должны быть неотрицательными.
     */
    public static Map<Integer, Integer> dijkstra(MyGraph g, int start) {
        Map<Integer, Integer> dist = new LinkedHashMap<>();
        if (!g.hasVertex(start)) return dist;
        for (int v : g.vertices()) dist.put(v, Integer.MAX_VALUE);
        dist.put(start, 0);
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[1], b[1]));
        pq.add(new int[]{start, 0});
        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int v = cur[0], d = cur[1];
            if (d > dist.get(v)) continue;
            for (MyGraph.Edge e : g.neighbors(v)) {
                int nd = d + e.weight;
                if (nd < dist.get(e.to)) {
                    dist.put(e.to, nd);
                    pq.add(new int[]{e.to, nd});
                }
            }
        }
        return dist;
    }
}