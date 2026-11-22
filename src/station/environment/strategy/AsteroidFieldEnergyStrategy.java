package station.environment.strategy;

import station.modules.Module;

public class AsteroidFieldEnergyStrategy implements InteractionStrategy {

    @Override
    public void apply(Module module) {
        EnvironmentEffects.applyAsteroidField(module);
    }
}
