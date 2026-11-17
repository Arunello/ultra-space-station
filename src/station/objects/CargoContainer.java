package station.objects;

import station.core.CommandCenter;
import station.logging.LoggingProxy;
import station.modules.Module;
import station.objects.strategy.CargoContainerStrategy;

public class CargoContainer extends ExternalObject {

    private final CargoContainerStrategy strategy;

    public CargoContainer() {
        this(1.0);
    }

    public CargoContainer(double strength) {
        super("CargoContainer", strength);
        this.strategy = new CargoContainerStrategy(strength);
    }

    public void interact(CommandCenter commandCenter, Module module) {
        LoggingProxy.log(
            LoggingProxy.Source.OBJECT,
            "Грузовой контейнер (сила " + getStrength() + ") воздействует на " + module.getName()
        );
        strategy.apply(commandCenter, module);
    }
}
