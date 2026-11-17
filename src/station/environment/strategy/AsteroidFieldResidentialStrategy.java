package station.environment.strategy;

import station.modules.*;
import station.modules.Module;

public class AsteroidFieldResidentialStrategy implements InteractionStrategy {

    private static final double ENERGY_MODIFIER = 2.0;
    private static final double DAMAGE_MODIFIER = 1.2;

    private static final int CREW_CABIN_DAMAGE = 10;
    private static final int MEDICAL_CABIN_DAMAGE = 8;

    public void apply(Module module) {
        switch (module) {
            case CrewCabin value -> {
                value.setEnergyModifier(ENERGY_MODIFIER);

                int damage = (int) Math.round(
                    CREW_CABIN_DAMAGE * DAMAGE_MODIFIER
                );

                value.subtractDurability(new Damage(damage, "ENVIRONMENT"));
            }
            case MedicalCabin value -> {
                value.setEnergyModifier(ENERGY_MODIFIER);

                int damage = (int) Math.round(
                    MEDICAL_CABIN_DAMAGE * DAMAGE_MODIFIER
                );

                value.subtractDurability(new Damage(damage, "ENVIRONMENT"));
            }
            default -> {
                return;
            }
        }
    }
}
