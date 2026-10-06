package ru.eventflow.structures;

import java.util.function.Consumer;

/**
 * Собственное бинарное дерево поиска (BST).
 * Запрещено использовать java.util.TreeMap и java.util.TreeSet,
 * поэтому реестр (BR-2) строится на этой структуре.
 * <p>
 * Сложности:
 * <ul>
 *   <li>insert — O(log n) в среднем, O(n) в худшем (вырожденное дерево).</li>
 *   <li>search — O(log n) в среднем, O(n) в худшем.</li>
 *   <li>inOrder — O(n), выдаёт значения в порядке возрастания ключей.</li>
 * </ul>
 *
 * @param <K> тип ключа, должен быть Comparable
 * @param <V> тип значения
 */
public class MyBinarySearchTree<K extends Comparable<K>, V> {

    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> left;
        Node<K, V> right;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node<K, V> root;
    private int size;

    /** Вставка или замена значения по ключу. */
    public void insert(K key, V value) {
        root = insert(root, key, value);
    }

    private Node<K, V> insert(Node<K, V> node, K key, V value) {
        if (node == null) {
            size++;
            return new Node<>(key, value);
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = insert(node.left, key, value);
        } else if (cmp > 0) {
            node.right = insert(node.right, key, value);
        } else {
            node.value = value; // ключ уже есть — заменяем значение
        }
        return node;
    }

    /** Поиск значения по ключу. Возвращает null, если ключ не найден. */
    public V search(K key) {
        Node<K, V> current = root;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp < 0) current = current.left;
            else if (cmp > 0) current = current.right;
            else return current.value;
        }
        return null;
    }

    public boolean contains(K key) {
        return search(key) != null;
    }

    /** Симметричный обход — обходит значения в порядке возрастания ключей. */
    public void inOrder(Consumer<V> visitor) {
        inOrder(root, visitor);
    }

    private void inOrder(Node<K, V> node, Consumer<V> visitor) {
        if (node == null) return;
        inOrder(node.left, visitor);
        visitor.accept(node.value);
        inOrder(node.right, visitor);
    }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }
}