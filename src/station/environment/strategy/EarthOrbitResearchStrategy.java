package station.environment.strategy;

import station.logging.LoggingProxy;
import station.modules.*;
import station.modules.Module;

public class EarthOrbitResearchStrategy implements InteractionStrategy {

    private static final double ENERGY_MODIFIER = 1.0;
    private static final double DAMAGE_MODIFIER = 0.0;

    private static final int BASE_DAMAGE = 0;

    public void apply(Module module) {
        switch (module) {
            case BiologyLab value -> {
                value.setEnergyModifier(ENERGY_MODIFIER);

                int damage = (int) Math.round(
                    BASE_DAMAGE * DAMAGE_MODIFIER
                );

                if (damage > 0) {
                    value.subtractDurability(new Damage(damage, "ENVIRONMENT"));
                }

                LoggingProxy.log(
                    LoggingProxy.Source.ENV,
                    "EarthOrbit: " + value.getName() + " работает в штатном режиме"
                );
            }
            case PhysicsLab value -> {
                value.setEnergyModifier(ENERGY_MODIFIER);

                int damage = (int) Math.round(
                    BASE_DAMAGE * DAMAGE_MODIFIER
                );

                if (damage > 0) {
                    value.subtractDurability(new Damage(damage, "ENVIRONMENT"));
                }

                LoggingProxy.log(
                    LoggingProxy.Source.ENV,
                    "EarthOrbit: " + value.getName() + " работает в штатном режиме"
                );
            }
            default -> {
                return;
            }
        }
    }
}
