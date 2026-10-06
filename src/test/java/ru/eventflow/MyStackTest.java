package ru.eventflow;

import org.junit.jupiter.api.Test;
import ru.eventflow.structures.MyStack;

import static org.junit.jupiter.api.Assertions.*;

class MyStackTest {

    @Test void emptyStack_popReturnsNull() {
        MyStack<Integer> s = new MyStack<>();
        assertTrue(s.isEmpty());
        assertNull(s.pop());
    }

    @Test void pushPop_reverseOrder() {
        MyStack<String> s = new MyStack<>();
        s.push("A"); s.push("B"); s.push("C");
        assertEquals("C", s.pop());
        assertEquals("B", s.pop());
        assertEquals("A", s.pop());
        assertNull(s.pop());
    }

    @Test void peek_doesNotRemove() {
        MyStack<Integer> s = new MyStack<>();
        s.push(42);
        assertEquals(42, s.peek());
        assertEquals(1, s.size());
    }
}