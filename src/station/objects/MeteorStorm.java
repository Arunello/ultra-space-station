package station.objects;

import station.objects.strategy.MeteorStormStrategy;

public class MeteorStorm extends ExternalObject {

    public MeteorStorm() {
        this(1.0);
    }

    public MeteorStorm(double strength) {
        super(
            "MeteorStorm",
            strength,
            "Метеоритный поток (сила " + strength + ")",
            new MeteorStormStrategy(strength)
        );
    }
}
