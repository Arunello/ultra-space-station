package station.objects.strategy;

import station.core.CommandCenter;
import station.modules.Module;

public interface ExternalObjectStrategy {
    String apply(CommandCenter commandCenter, Module module);
}
