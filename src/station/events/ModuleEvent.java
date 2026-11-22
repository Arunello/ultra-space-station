package station.events;

import java.time.LocalDateTime;

public record ModuleEvent(
    String type,
    int moduleId,
    int damage,
    int amount,
    LocalDateTime timestamp
) {
    public static final String ACTIVATED = "Activated";
    public static final String DEACTIVATED = "Deactivated";
    public static final String DAMAGED = "Damaged";
    public static final String DESTROYED = "Destroyed";
    public static final String ENERGY_CONSUMED = "EnergyConsumed";
}
