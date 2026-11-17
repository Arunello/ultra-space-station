package station.logging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class LoggingProxy {

    public enum Source {
        ENV,
        OBJECT,
        MODULE,
        CENTER,
    }

    private static final List<String> history = new ArrayList<>();

    private LoggingProxy() {}

    public static void log(Source source, String message) {
        String line = "[" + source + "] " + message;
        history.add(line);
        System.out.println(line);
    }

    public static List<String> getHistory() {
        return Collections.unmodifiableList(history);
    }

    public static void clear() {
        history.clear();
    }
}
