package it.trenical.trainmanager.utilities;

import java.time.Instant;

public final class Validator {
    private Validator() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isBlank(String str) {
        return str == null || str.isBlank();
    }

    public static boolean isFuture(long time){
        return time > Instant.now().getEpochSecond();
    }
}
