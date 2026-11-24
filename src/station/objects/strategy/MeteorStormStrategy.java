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

    public String apply(CommandCenter commandCenter, Module module) {
        int damage = switch (module.getCategory()) {
            case RESIDENTIAL -> RESIDENTIAL_DAMAGE;
            case ENERGY -> ENERGY_DAMAGE;
            case RESEARCH -> RESEARCH_DAMAGE;
        };

        int scaled = (int) Math.round(damage * strength);
        int before = module.getDurability().getValue();
        int incoming = commandCenter.applySafety(scaled);
        module.subtractDurability(new Damage(incoming, DAMAGE_TYPE));
        int health = module.getDurability().getValue();
        String result = "Метеоритный поток: защита поглотила %d из %d урона. «%s»: урон %d"
            .formatted(scaled - incoming, scaled, module.getName(), before - health);
        if (health == 0) return result + ", модуль уничтожен.";
        result += ", осталось прочности " + health + "/" + module.getTypeInfo().getMaxHealth() + ".";
        return result;
    }
}
