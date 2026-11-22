package station.environment.strategy;

import station.environment.EarthOrbit;
import station.logging.LoggingProxy;
import station.modules.Module;
import station.modules.ResearchModule;

public class EarthOrbitStrategy implements InteractionStrategy {

    @Override
    public void apply(Module module) {
        if (module instanceof ResearchModule lab) {
            lab.setActivationBlocked(false);
        }
        module.setEnergyModifier(EarthOrbit.ENERGY_MODIFIER);
        LoggingProxy.log(
            LoggingProxy.Source.ENV,
            "EarthOrbit: " + module.getName() + " работает в штатном режиме"
        );
    }
}
