package station.objects.strategy;

import station.core.CommandCenter;
import station.modules.Damage;
import station.modules.EnergyModule;
import station.modules.Module;
import station.modules.ResearchModule;
import station.modules.ResidentialModule;

public class SolarStormStrategy implements ExternalObjectStrategy {

    private static final int RESIDENTIAL_DAMAGE = 5;
    private static final int ENERGY_DAMAGE = 15;
    private static final int RESEARCH_DAMAGE = 10;
    private static final String DAMAGE_TYPE = "RADIATION";

    private final double strength;

    public SolarStormStrategy() {
        this(1.0);
    }

    public SolarStormStrategy(double strength) {
        this.strength = strength;
    }

    public void apply(CommandCenter commandCenter, Module module) {
        int damage;

        if (module instanceof ResidentialModule) {
            damage = RESIDENTIAL_DAMAGE;
        } else if (module instanceof EnergyModule) {
            damage = ENERGY_DAMAGE;
        } else if (module instanceof ResearchModule) {
            damage = RESEARCH_DAMAGE;
        } else {
            return;
        }

        int scaled = (int) Math.round(damage * strength);
        module.subtractDurability(
            new Damage(commandCenter.applySafety(scaled), DAMAGE_TYPE)
        );
        if (module instanceof EnergyModule) {
            module.deactivate();
        }
    }
}
