package station.objects;

import station.objects.strategy.CargoContainerStrategy;

public class CargoContainer extends ExternalObject {

    public CargoContainer() {
        this(1.0);
    }

    public CargoContainer(double strength) {
        super(
            "CargoContainer",
            strength,
            "Грузовой контейнер (сила " + strength + ")",
            new CargoContainerStrategy(strength)
        );
    }
}
