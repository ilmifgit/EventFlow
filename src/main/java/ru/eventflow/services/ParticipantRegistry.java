package ru.eventflow.services;

import ru.eventflow.model.Participant;
import ru.eventflow.structures.MyBinarySearchTree;

public class ParticipantRegistry {

    private final MyBinarySearchTree<Integer, Participant> tree = new MyBinarySearchTree<>();

    public boolean add(Participant participant) {
        boolean isNew = !tree.contains(participant.getId());
        tree.insert(participant.getId(), participant);
        return isNew;
    }

    public Participant findById(int id) {
        return tree.search(id);
    }

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
