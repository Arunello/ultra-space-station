package station.objects;

import station.objects.strategy.SpaceAnomalyStrategy;

public class SpaceAnomaly extends ExternalObject {

    public SpaceAnomaly() {
        super(
            "SpaceAnomaly",
            1.0,
            "Космическая аномалия",
            new SpaceAnomalyStrategy()
        );
    }
}
