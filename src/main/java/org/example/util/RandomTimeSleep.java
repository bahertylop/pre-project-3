package org.example.util;

import java.util.concurrent.ThreadLocalRandom;

public class RandomTimeSleep {

    public static void randomSleep() {
        try {
            int sleepTime = ThreadLocalRandom.current().nextInt(1000, 4000 + 1);
            System.out.println("Задержка: " + (sleepTime / 1000.0) + " секунд");
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
