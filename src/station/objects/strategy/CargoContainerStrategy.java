package station.objects.strategy;

import station.core.CommandCenter;
import station.core.ResourcePackage;
import station.modules.Module;

public class CargoContainerStrategy implements ExternalObjectStrategy {

    private static final int ENERGY_BONUS = 60;
    private static final int OXYGEN_BONUS = 40;
    private static final int SAFETY_BONUS = 5;

    private final double strength;

    public CargoContainerStrategy() {
        this(1.0);
    }

    public CargoContainerStrategy(double strength) {
        this.strength = strength;
    }

    public String apply(CommandCenter commandCenter, Module module) {
        return deliver(commandCenter);
    }

    public String deliver(CommandCenter commandCenter) {
        ResourcePackage resources = new ResourcePackage(
                (int) Math.round(ENERGY_BONUS * strength),
                (int) Math.round(OXYGEN_BONUS * strength),
                (int) Math.round(SAFETY_BONUS * strength)
        );
        commandCenter.addResources(resources);
        return "Груз принят. Энергия +%d, кислород +%d, безопасность +%d.".formatted(
            resources.energy(), resources.oxygen(), resources.safetyLevel());
    }
}
