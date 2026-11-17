package station.environment.strategy;

import station.modules.*;
import station.modules.Module;

public class AsteroidFieldEnergyStrategy implements InteractionStrategy {

    private static final double ENERGY_MODIFIER = 2.0;
    private static final double DAMAGE_MODIFIER = 1.2;

    private static final int SOLAR_MODULE_DAMAGE = 15;
    private static final int REACTOR_MODULE_DAMAGE = 8;

    public void apply(Module module) {
        switch (module) {
            case SolarModule value -> {
                value.setEnergyModifier(ENERGY_MODIFIER);

                int damage = (int) Math.round(
                    SOLAR_MODULE_DAMAGE * DAMAGE_MODIFIER
                );

                value.subtractDurability(new Damage(damage, "ENVIRONMENT"));
            }
            case ReactorModule value -> {
                value.setEnergyModifier(ENERGY_MODIFIER);

                int damage = (int) Math.round(
                    REACTOR_MODULE_DAMAGE * DAMAGE_MODIFIER
                );

                value.subtractDurability(new Damage(damage, "ENVIRONMENT"));
            }
            default -> {
                return;
            }
        }
    }
}
