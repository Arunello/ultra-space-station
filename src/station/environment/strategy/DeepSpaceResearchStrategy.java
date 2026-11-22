package station.environment.strategy;

import station.modules.Module;
import station.modules.ResearchModule;

public class DeepSpaceResearchStrategy implements InteractionStrategy {

    @Override
    public void apply(Module module) {
        ((ResearchModule) module).setActivationBlocked(false);
        EnvironmentEffects.applyDeepSpace(module);
    }
}
