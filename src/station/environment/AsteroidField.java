package station.environment;

import station.environment.strategy.*;
import station.modules.Module;

public class AsteroidField implements Environment {

    public static final double ENERGY_MODIFIER = 2.0;
    public static final double DAMAGE_MODIFIER = 1.2;

    private final InteractionStrategy strategy = new AsteroidFieldStrategy();

    @Override
    public void applyEffect(Module module) {
        strategy.apply(module);
    }

    public String getName() {
        return "Поле астероидов";
    }

    public double getEnergyModifier() {
        return ENERGY_MODIFIER;
    }

    public double getDamageModifier() {
        return DAMAGE_MODIFIER;
    }
}
