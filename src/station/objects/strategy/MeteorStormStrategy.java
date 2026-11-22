package station.objects.strategy;

import station.core.CommandCenter;
import station.modules.Damage;
import station.modules.Module;

public class MeteorStormStrategy implements ExternalObjectStrategy {

    private static final int RESIDENTIAL_DAMAGE = 10;
    private static final int ENERGY_DAMAGE = 15;
    private static final int RESEARCH_DAMAGE = 12;
    private static final String DAMAGE_TYPE = "PHYSICAL";

    private final double strength;

    public MeteorStormStrategy() {
        this(1.0);
    }

    public MeteorStormStrategy(double strength) {
        this.strength = strength;
    }

    public void apply(CommandCenter commandCenter, Module module) {
        int damage = switch (module.getCategory()) {
            case RESIDENTIAL -> RESIDENTIAL_DAMAGE;
            case ENERGY -> ENERGY_DAMAGE;
            case RESEARCH -> RESEARCH_DAMAGE;
        };

        int scaled = (int) Math.round(damage * strength);
        module.subtractDurability(
            new Damage(commandCenter.applySafety(scaled), DAMAGE_TYPE)
        );
    }
}
