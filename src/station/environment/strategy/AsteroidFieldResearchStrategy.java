package station.environment.strategy;

import station.modules.Module;
import station.modules.ResearchModule;

public class AsteroidFieldResearchStrategy implements InteractionStrategy {

    @Override
    public void apply(Module module) {
        ((ResearchModule) module).setActivationBlocked(true);
        EnvironmentEffects.applyAsteroidField(module);
    }
}
