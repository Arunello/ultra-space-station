package station.environment.strategy;

import station.modules.Module;

public class DeepSpaceEnergyStrategy implements InteractionStrategy {

    @Override
    public void apply(Module module) {
        EnvironmentEffects.applyDeepSpace(module);
    }
}
