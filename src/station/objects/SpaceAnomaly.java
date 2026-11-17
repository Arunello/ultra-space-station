package station.objects;

import station.core.CommandCenter;
import station.logging.LoggingProxy;
import station.modules.Module;
import station.objects.strategy.SpaceAnomalyStrategy;

public class SpaceAnomaly extends ExternalObject {

    private final SpaceAnomalyStrategy strategy = new SpaceAnomalyStrategy();

    public SpaceAnomaly() {
        super("SpaceAnomaly", 1.0);
    }

    public void interact(CommandCenter commandCenter, Module module) {
        LoggingProxy.log(
            LoggingProxy.Source.OBJECT,
            "Космическая аномалия воздействует на " + module.getName()
        );
        strategy.apply(commandCenter, module);
    }
}
