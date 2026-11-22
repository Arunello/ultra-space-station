package station.environment;

import station.environment.strategy.*;
import station.modules.Module;

public class EarthOrbit implements Environment {

    public static final double ENERGY_MODIFIER = 1.0;
    private static final double DAMAGE_MODIFIER = 1.0;

    public void applyEffect(Module module) {
        InteractionStrategy strategy = switch (module.getCategory()) {
            case RESIDENTIAL -> new EarthOrbitResidentialStrategy();
            case ENERGY -> new EarthOrbitEnergyStrategy();
            case RESEARCH -> new EarthOrbitResearchStrategy();
        };
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
