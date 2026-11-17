package station.objects;

import station.core.CommandCenter;
import station.logging.LoggingProxy;
import station.modules.Module;
import station.objects.strategy.SolarStormStrategy;

public class SolarStorm extends ExternalObject {

    private final SolarStormStrategy strategy;

    public SolarStorm() {
        this(1.0);
    }

    public SolarStorm(double strength) {
        super("SolarStorm", strength);
        this.strategy = new SolarStormStrategy(strength);
    }

    public void interact(CommandCenter commandCenter, Module module) {
        LoggingProxy.log(
            LoggingProxy.Source.OBJECT,
            "Солнечная буря (сила " + getStrength() + ") воздействует на " + module.getName()
        );
        strategy.apply(commandCenter, module);
    }
}
