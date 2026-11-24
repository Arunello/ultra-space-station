package station.objects;

import station.core.CommandCenter;
import station.logging.LoggingProxy;
import station.objects.strategy.CargoContainerStrategy;

public class CargoContainer extends ExternalObject {

    private final CargoContainerStrategy cargo;

    public CargoContainer() {
        this(1.0);
    }

    public CargoContainer(double strength) {
        this(strength, new CargoContainerStrategy(strength));
    }

    private CargoContainer(double strength, CargoContainerStrategy cargo) {
        super(
            "CargoContainer",
            strength,
            "Грузовой контейнер (сила " + strength + ")",
            cargo
        );
        this.cargo = cargo;
    }

    public String deliver(CommandCenter center) {
        LoggingProxy.log(LoggingProxy.Source.OBJECT, "Грузовой контейнер прибыл на станцию");
        return cargo.deliver(center);
    }
}
