package station.objects;

import station.core.CommandCenter;
import station.logging.LoggingProxy;
import station.modules.Module;
import station.objects.strategy.MeteorStormStrategy;

public class MeteorStorm extends ExternalObject {

    private final MeteorStormStrategy strategy;

    public MeteorStorm() {
        this(1.0);
    }

    public MeteorStorm(double strength) {
        super("MeteorStorm", strength);
        this.strategy = new MeteorStormStrategy(strength);
    }

    public void interact(CommandCenter commandCenter, Module module) {
        LoggingProxy.log(
            LoggingProxy.Source.OBJECT,
            "Метеоритный поток (сила " + getStrength() + ") воздействует на " + module.getName()
        );
        strategy.apply(commandCenter, module);
    }
}
