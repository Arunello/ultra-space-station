package station.objects;

import station.core.CommandCenter;
import station.modules.Module;

public abstract class ExternalObject {

    private final String type;
    private final double strength;

    protected ExternalObject(String type, double strength) {
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Type cannot be empty");
        }
        if (strength < 0) {
            throw new IllegalArgumentException("Negative strength");
        }
        this.type = type;
        this.strength = strength;
    }

    public String getType() {
        return type;
    }

    public double getStrength() {
        return strength;
    }

    public abstract void interact(CommandCenter commandCenter, Module module);
}
