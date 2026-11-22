package station.events;

import java.time.LocalDateTime;

public record ModuleEvent(
    String type,
    int moduleId,
    int damage,
    int amount,
    LocalDateTime timestamp
) {}
