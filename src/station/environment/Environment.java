package station.environment;

import station.modules.Module;

public interface Environment {
    void applyEffect(Module module);

    String getName();

    double getEnergyModifier();

    double getDamageModifier();
}
