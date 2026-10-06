package ru.eventflow;

import org.junit.jupiter.api.Test;
import ru.eventflow.structures.MyBinarySearchTree;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MyBinarySearchTreeTest {

    @Test void emptyTree_searchNull() {
        MyBinarySearchTree<Integer, String> t = new MyBinarySearchTree<>();
        assertTrue(t.isEmpty());
        assertNull(t.search(1));
    }

    @Test void inOrder_sorted() {
        MyBinarySearchTree<Integer, Integer> t = new MyBinarySearchTree<>();
        for (int k : new int[]{50, 20, 70, 10, 30, 60, 80}) t.insert(k, k);
        List<Integer> v = new ArrayList<>();
        t.inOrder(v::add);
        assertEquals(List.of(10, 20, 30, 50, 60, 70, 80), v);
    }

    @Test void duplicate_replacesValue() {
        MyBinarySearchTree<Integer, String> t = new MyBinarySearchTree<>();
        t.insert(1, "first");
        t.insert(1, "second");
        assertEquals(1, t.size());
        assertEquals("second", t.search(1));
    }
}