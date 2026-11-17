package station.environment;

import java.util.Random;
import station.environment.strategy.*;
import station.modules.*;
import station.modules.Module;

public class DeepSpace implements Environment {

    private static final double ENERGY_MODIFIER = 1.5;
    private static final double DAMAGE_MODIFIER = 1.0;

    private static final Random random = new Random();

    public void applyEffect(Module module) {
        InteractionStrategy strategy;

        if (module instanceof ResidentialModule) {
            strategy = new DeepSpaceResidentialStrategy();
        } else if (module instanceof EnergyModule) {
            strategy = new DeepSpaceEnergyStrategy();
        } else if (module instanceof ResearchModule) {
            strategy = new DeepSpaceResearchStrategy();
        } else {
            return;
        }

        strategy.apply(module);
    }

    public String getName() {
        return "DeepSpace";
    }

    public double getEnergyModifier() {
        return ENERGY_MODIFIER;
    }

    public double getDamageModifier() {
        return DAMAGE_MODIFIER;
    }

    public int getRandomDamage() {
        if (random.nextDouble() < 0.3) {
            return random.nextInt(15) + 1;
        }

        return 0;
    }
}
