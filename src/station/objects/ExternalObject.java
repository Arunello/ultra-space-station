package station.objects;

import station.core.CommandCenter;
import station.logging.LoggingProxy;
import station.modules.Module;
import station.objects.strategy.ExternalObjectStrategy;

public abstract class ExternalObject {

    private final String type;
    private final double strength;

    private final String logName;
    private final ExternalObjectStrategy strategy;

    protected ExternalObject(
        String type,
        double strength,
        String logName,
        ExternalObjectStrategy strategy
    ) {
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Type cannot be empty");
        }
        if (strength < 0) {
            throw new IllegalArgumentException("Negative strength");
        }
        this.type = type;
        this.strength = strength;
        this.logName = logName;
        this.strategy = strategy;
    }

    public String getType() {
        return type;
    }

    public double getStrength() {
        return strength;
    }

    public String interact(CommandCenter commandCenter, Module module) {
        LoggingProxy.log(
            LoggingProxy.Source.OBJECT,
            logName + " воздействует на " + module.getName()
        );
        return strategy.apply(commandCenter, module);
    }
}
