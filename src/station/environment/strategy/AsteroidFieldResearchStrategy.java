package station.environment.strategy;

import station.modules.*;
import station.modules.Module;

public class AsteroidFieldResearchStrategy implements InteractionStrategy {

    private static final double ENERGY_MODIFIER = 2.0;
    private static final double DAMAGE_MODIFIER = 1.2;

    private static final int BIOLOGY_LAB_DAMAGE = 12;
    private static final int PHYSICS_LAB_DAMAGE = 10;

    private static final String ACTIVE_STATE = "включён";

    public void apply(Module module) {
        switch (module) {
            case BiologyLab value -> {
                value.setEnergyModifier(ENERGY_MODIFIER);

                int damage = (int) Math.round(
                    BIOLOGY_LAB_DAMAGE * DAMAGE_MODIFIER
                );

                value.subtractDurability(new Damage(damage, "ENVIRONMENT"));

                if (ACTIVE_STATE.equals(value.getState())) {
                    value.deactivate();
                }
            }
            case PhysicsLab value -> {
                value.setEnergyModifier(ENERGY_MODIFIER);

                int damage = (int) Math.round(
                    PHYSICS_LAB_DAMAGE * DAMAGE_MODIFIER
                );

                value.subtractDurability(new Damage(damage, "ENVIRONMENT"));

                if (ACTIVE_STATE.equals(value.getState())) {
                    value.deactivate();
                }
            }
            default -> {
                return;
            }
        }
    }
}
