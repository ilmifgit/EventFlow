package ru.eventflow.structures;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<T> implements Iterable<T> {

    private static class Node<T> {
        T value;
        Node<T> next;
        Node<T> prev;
        Node(T value) { this.value = value; }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    public void addFirst(T value) {
        Node<T> node = new Node<>(value);
        if (head == null) {
            head = tail = node;
        } else {
            node.next = head;
            head.prev = node;
            head = node;
        }
        size++;
    }

    public void addLast(T value) {
        Node<T> node = new Node<>(value);
        if (tail == null) {
            head = tail = node;
        } else {
            node.prev = tail;
            tail.next = node;
            tail = node;
        }
        size++;
    }

    public T removeFirst() {
        if (head == null) return null;
        T value = head.value;
        head = head.next;
        if (head != null) head.prev = null;
        else tail = null;
        size--;
        return value;
    }

    public T peekFirst() {
        return head == null ? null : head.value;
    }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private Node<T> current = head;
            @Override public boolean hasNext() { return current != null; }
            @Override public T next() {
                if (current == null) throw new NoSuchElementException();
                T v = current.value;
                current = current.next;
                return v;
            }
        };
    }
}
