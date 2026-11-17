package station.environment;

import station.environment.strategy.*;
import station.modules.*;
import station.modules.Module;

public class AsteroidField implements Environment {

    private static final double ENERGY_MODIFIER = 2.0;
    private static final double DAMAGE_MODIFIER = 1.2;

    public void applyEffect(Module module) {
        InteractionStrategy strategy;

        if (module instanceof ResidentialModule) {
            strategy = new AsteroidFieldResidentialStrategy();
        } else if (module instanceof EnergyModule) {
            strategy = new AsteroidFieldEnergyStrategy();
        } else if (module instanceof ResearchModule) {
            strategy = new AsteroidFieldResearchStrategy();
        } else {
            return;
        }

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
