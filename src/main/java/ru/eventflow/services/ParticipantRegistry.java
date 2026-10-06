package ru.eventflow.services;

import ru.eventflow.model.Participant;
import ru.eventflow.structures.MyBinarySearchTree;

/**
 * BR-2: реестр участников.
 * Добавление, поиск по ID и вывод в порядке возрастания ID —
 * без использования готовых упорядоченных коллекций Java.
 */
public class ParticipantRegistry {

    private final MyBinarySearchTree<Integer, Participant> tree = new MyBinarySearchTree<>();

    /**
     * Добавляет участника. Если ID уже существует — заменяет запись.
     * @return true, если участник добавлен впервые; false, если запись заменена
     */
    public boolean add(Participant participant) {
        boolean isNew = !tree.contains(participant.getId());
        tree.insert(participant.getId(), participant);
        return isNew;
    }

    /** Находит участника по ID. Возвращает null, если не найден. */
    public Participant findById(int id) {
        return tree.search(id);
    }

    /** Печатает всех участников в порядке возрастания ID. */
    public void printAllSorted() {
        if (tree.isEmpty()) {
            System.out.println("  Реестр пуст.");
            return;
        }
        System.out.println("  Участники (в порядке возрастания ID):");
        tree.inOrder(p -> System.out.println("   " + p));
    }

    public int size() { return tree.size(); }
    public boolean isEmpty() { return tree.isEmpty(); }
}