package station.objects.strategy;

import station.core.CommandCenter;
import station.modules.Module;

public interface ExternalObjectStrategy {
    void apply(CommandCenter commandCenter, Module module);
}
