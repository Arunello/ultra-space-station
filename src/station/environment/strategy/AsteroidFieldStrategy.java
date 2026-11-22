package station.environment.strategy;

import station.environment.AsteroidField;
import station.modules.Damage;
import station.modules.Module;
import station.modules.ResearchModule;

public class AsteroidFieldStrategy implements InteractionStrategy {

    @Override
    public void apply(Module module) {
        if (module instanceof ResearchModule lab) {
            lab.setActivationBlocked(true);
        }
        module.setEnergyModifier(AsteroidField.ENERGY_MODIFIER);
        int damage = (int) Math.round(
            module.getTypeInfo().getAsteroidDamage() * AsteroidField.DAMAGE_MODIFIER
        );
        module.subtractDurability(new Damage(damage, "ENVIRONMENT"));
    }
}
