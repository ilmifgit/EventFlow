package ru.eventflow.io;

import ru.eventflow.model.Talk;
import java.util.Random;

public final class DataGenerator {

    private DataGenerator() { }

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
