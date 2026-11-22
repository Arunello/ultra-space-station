package station.environment.strategy;

import java.util.Random;
import station.environment.DeepSpace;
import station.modules.Damage;
import station.modules.Module;
import station.modules.ResearchModule;

public class DeepSpaceStrategy implements InteractionStrategy {

    private final Random random = new Random();

    @Override
    public void apply(Module module) {
        if (module instanceof ResearchModule lab) {
            lab.setActivationBlocked(false);
        }
        module.setEnergyModifier(DeepSpace.ENERGY_MODIFIER);
        int damage = random.nextDouble() < 0.3 ? random.nextInt(15) + 1 : 0;
        module.subtractDurability(new Damage(damage, "ENVIRONMENT"));
    }
}
