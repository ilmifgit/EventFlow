package ru.eventflow.structures;

import java.util.Iterator;

/**
 * Собственный стек (LIFO) на основе {@link MyLinkedList}.
 * Запрещено использовать java.util.Stack и java.util.Deque.
 *
 * @param <T> тип хранимых значений
 */
public class MyStack<T> implements Iterable<T> {

    private final MyLinkedList<T> list = new MyLinkedList<>();

    /** Кладёт значение на вершину стека. O(1). */
    public void push(T value) { list.addFirst(value); }

    /** Снимает значение с вершины. Возвращает null, если стек пуст. O(1). */
    public T pop() { return list.removeFirst(); }

    /** Возвращает вершину без снятия. O(1). */
    public T peek() { return list.peekFirst(); }

    public boolean isEmpty() { return list.isEmpty(); }
    public int size() { return list.size(); }

    /** Итерация от вершины стека к его основанию. */
    @Override
    public Iterator<T> iterator() { return list.iterator(); }
}