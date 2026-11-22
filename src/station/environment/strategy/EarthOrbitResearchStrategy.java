package station.environment.strategy;

import station.modules.Module;
import station.modules.ResearchModule;

public class EarthOrbitResearchStrategy implements InteractionStrategy {

    @Override
    public void apply(Module module) {
        ((ResearchModule) module).setActivationBlocked(false);
        EnvironmentEffects.applyEarthOrbit(module);
    }
}
