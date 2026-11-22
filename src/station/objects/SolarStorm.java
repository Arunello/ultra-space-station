package station.objects;

import station.objects.strategy.SolarStormStrategy;

public class SolarStorm extends ExternalObject {

    public SolarStorm() {
        this(1.0);
    }

    public SolarStorm(double strength) {
        super(
            "SolarStorm",
            strength,
            "Солнечная буря (сила " + strength + ")",
            new SolarStormStrategy(strength)
        );
    }
}
