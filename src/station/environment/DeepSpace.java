package station.environment;

import station.environment.strategy.*;
import station.modules.Module;

public class DeepSpace implements Environment {

    public static final double ENERGY_MODIFIER = 1.5;
    private static final double DAMAGE_MODIFIER = 1.0;

    private final InteractionStrategy strategy = new DeepSpaceStrategy();

    @Override
    public void applyEffect(Module module) {
        strategy.apply(module);
    }

    public String getName() {
        return "DeepSpace";
    }

    public double getEnergyModifier() {
        return ENERGY_MODIFIER;
    }

    public double getDamageModifier() {
        return DAMAGE_MODIFIER;
    }
}
