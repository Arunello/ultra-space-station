package station.environment;

import station.environment.strategy.*;
import station.modules.*;
import station.modules.Module;

public class EarthOrbit implements Environment {

    private static final double ENERGY_MODIFIER = 1.0;
    private static final double DAMAGE_MODIFIER = 1.0;

    public void applyEffect(Module module) {
        InteractionStrategy strategy;

        if (module instanceof ResidentialModule) {
            strategy = new EarthOrbitResidentialStrategy();
        } else if (module instanceof EnergyModule) {
            strategy = new EarthOrbitEnergyStrategy();
        } else if (module instanceof ResearchModule) {
            strategy = new EarthOrbitResearchStrategy();
        } else {
            return;
        }

        strategy.apply(module);
    }

    public String getName() {
        return "EarthOrbit";
    }

    public double getEnergyModifier() {
        return ENERGY_MODIFIER;
    }

    public double getDamageModifier() {
        return DAMAGE_MODIFIER;
    }
}
