package ru.eventflow.structures;

import java.util.Iterator;

public class MyStack<T> implements Iterable<T> {

    private final MyLinkedList<T> list = new MyLinkedList<>();

    public void push(T value) { list.addFirst(value); }

    public T pop() { return list.removeFirst(); }
    public T peek() { return list.peekFirst(); }

    public boolean isEmpty() { return list.isEmpty(); }
    public int size() { return list.size(); }

    @Override
    public Iterator<T> iterator() { return list.iterator(); }
}
