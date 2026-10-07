package ru.eventflow.io;

import java.util.Scanner;

public class InputReader {

    private final Scanner scanner = new Scanner(System.in);

    public String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public int readInt(String prompt, int defaultValue) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) return defaultValue;
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("  Ошибка: введите целое число или Enter для значения по умолчанию.");
            }
        }
    }

    public long readLong(String prompt, long defaultValue) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) return defaultValue;
            try {
                return Long.parseLong(line);
            } catch (NumberFormatException e) {
                System.out.println("  Ошибка: введите целое число или Enter.");
            }
        }
    }

    public void close() {
        scanner.close();
    }
}
