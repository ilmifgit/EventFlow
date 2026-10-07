package ru.eventflow;

import ru.eventflow.io.ConsoleMenu;
import ru.eventflow.io.InputReader;

public class Main {
    public static void main(String[] args) {
        InputReader input = new InputReader();
        try {
            new ConsoleMenu(input).run();
        } finally {
            input.close();
        }
    }
}
