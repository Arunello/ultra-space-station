package station.environment.strategy;

import station.modules.Module;

public class EarthOrbitEnergyStrategy implements InteractionStrategy {

    @Override
    public void apply(Module module) {
        EnvironmentEffects.applyEarthOrbit(module);
    }
}
