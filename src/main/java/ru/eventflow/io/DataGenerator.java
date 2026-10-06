package ru.eventflow.io;

import ru.eventflow.model.Talk;

import java.util.Random;

/**
 * Генератор тестовых данных с фиксированным seed —
 * чтобы замеры производительности были воспроизводимы.
 */
public final class DataGenerator {

    private DataGenerator() { }

    /**
     * Генерирует массив докладов. Начало — случайное время в минутах от 0 до 23*60.
     * Длительность — от 30 до 120 минут.
     */
    public static Talk[] generateTalks(int n, long seed) {
        Random rnd = new Random(seed);
        Talk[] result = new Talk[n];
        for (int i = 0; i < n; i++) {
            int start = rnd.nextInt(23 * 60);
            int dur = 30 + rnd.nextInt(91);
            result[i] = new Talk(i + 1, "Talk-" + (i + 1), start, dur);
        }
        return result;
    }
}