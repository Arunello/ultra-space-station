package station.environment.strategy;

import station.environment.AsteroidField;
import station.environment.DeepSpace;
import station.environment.EarthOrbit;
import station.logging.LoggingProxy;
import station.modules.Damage;
import station.modules.Module;

final class EnvironmentEffects {

    private EnvironmentEffects() {}

    static void applyEarthOrbit(Module module) {
        module.setEnergyModifier(EarthOrbit.ENERGY_MODIFIER);
        LoggingProxy.log(
            LoggingProxy.Source.ENV,
            "EarthOrbit: " + module.getName() + " работает в штатном режиме"
        );
    }

    static void applyDeepSpace(Module module) {
        module.setEnergyModifier(DeepSpace.ENERGY_MODIFIER);
        module.subtractDurability(new Damage(DeepSpace.getRandomDamage(), "ENVIRONMENT"));
    }

    static void applyAsteroidField(Module module) {
        module.setEnergyModifier(AsteroidField.ENERGY_MODIFIER);
        int damage = (int) Math.round(
            module.getTypeInfo().getAsteroidDamage() * AsteroidField.DAMAGE_MODIFIER
        );
        module.subtractDurability(new Damage(damage, "ENVIRONMENT"));
    }
}
