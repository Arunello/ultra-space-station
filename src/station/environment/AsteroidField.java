package station.environment;

import station.environment.strategy.*;
import station.modules.Module;

public class AsteroidField implements Environment {

    public static final double ENERGY_MODIFIER = 2.0;
    public static final double DAMAGE_MODIFIER = 1.2;

    public void applyEffect(Module module) {
        InteractionStrategy strategy = switch (module.getCategory()) {
            case RESIDENTIAL -> new AsteroidFieldResidentialStrategy();
            case ENERGY -> new AsteroidFieldEnergyStrategy();
            case RESEARCH -> new AsteroidFieldResearchStrategy();
        };
        strategy.apply(module);
    }

    public String getName() {
        return "AsteroidField";
    }

    public double getEnergyModifier() {
        return ENERGY_MODIFIER;
    }

    public double getDamageModifier() {
        return DAMAGE_MODIFIER;
    }
}
