package ru.eventflow;

import org.junit.jupiter.api.Test;
import ru.eventflow.algorithms.GraphAlgorithms;
import ru.eventflow.structures.MyGraph;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GraphAlgorithmsTest {

    private MyGraph buildGraph() {
        MyGraph g = new MyGraph();
        g.addEdge(1, 2, 5);
        g.addEdge(1, 3, 10);
        g.addEdge(2, 3, 3);
        g.addEdge(2, 5, 8);
        g.addEdge(3, 4, 2);
        g.addEdge(4, 2, 1);
        g.addVertex(6);
        return g;
    }

    @Test void bfs_correctOrder() {
        assertEquals(List.of(1, 2, 3, 5, 4), GraphAlgorithms.bfs(buildGraph(), 1));
    }

    @Test void hasCycle_true() {
        assertTrue(GraphAlgorithms.hasCycle(buildGraph()));
    }

    @Test void components_isolatedCounted() {
        assertEquals(2, GraphAlgorithms.weakComponentCount(buildGraph()));
    }

    @Test void dijkstra_correctDistances() {
        Map<Integer, Integer> d = GraphAlgorithms.dijkstra(buildGraph(), 1);
        assertEquals(0, d.get(1));
        assertEquals(5, d.get(2));
        assertEquals(8, d.get(3));
        assertEquals(10, d.get(4));
        assertEquals(13, d.get(5));
        assertEquals(Integer.MAX_VALUE, d.get(6));
    }

    @Test void emptyGraph_ok() {
        MyGraph g = new MyGraph();
        assertTrue(GraphAlgorithms.bfs(g, 1).isEmpty());
        assertFalse(GraphAlgorithms.hasCycle(g));
    }
}