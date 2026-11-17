package station.environment.strategy;

import java.util.concurrent.ThreadLocalRandom;
import station.modules.*;
import station.modules.Module;

public class DeepSpaceResidentialStrategy implements InteractionStrategy {

    private static final double ENERGY_MODIFIER = 1.5;
    private static final double DAMAGE_CHANCE = 0.3;
    private static final int MIN_DAMAGE = 1;
    private static final int MAX_DAMAGE = 15;

    public void apply(Module module) {
        switch (module) {
            case CrewCabin value -> {
                value.setEnergyModifier(ENERGY_MODIFIER);

                if (ThreadLocalRandom.current().nextDouble() < DAMAGE_CHANCE) {
                    int damage = ThreadLocalRandom.current()
                        .nextInt(MIN_DAMAGE, MAX_DAMAGE + 1);

                    value.subtractDurability(new Damage(damage, "ENVIRONMENT"));
                }
            }
            case MedicalCabin value -> {
                value.setEnergyModifier(ENERGY_MODIFIER);

                if (ThreadLocalRandom.current().nextDouble() < DAMAGE_CHANCE) {
                    int damage = ThreadLocalRandom.current()
                        .nextInt(MIN_DAMAGE, MAX_DAMAGE + 1);

                    value.subtractDurability(new Damage(damage, "ENVIRONMENT"));
                }
            }
            default -> {
                return;
            }
        }
    }
}
