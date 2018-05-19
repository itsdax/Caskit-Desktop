package utils;


import java.time.Duration;

public class Timing {

    public static long timeSince(long ms) {
        return System.currentTimeMillis() - ms;
    }

    public static long timeSinceAbs(long ms) {
        return Math.abs(timeSince(ms));
    }

    public static String formatDuration(long millis) {
        return formatDuration(Duration.ofMillis(millis));
    }

    public static String formatDuration(Duration duration) {
        long seconds = duration.getSeconds();
        long absSeconds = Math.abs(seconds);
        String positive = String.format(
                "%02d:%02d",
                (absSeconds % 3600) / 60,
                absSeconds % 60);
        return seconds < 0 ? "-" + positive : positive;
    }

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
