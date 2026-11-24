package station.core;

import station.environment.Environment;
import station.environment.EnvironmentProxy;
import station.modules.Module;

public class EnvironmentSystem {
    private EnvironmentProxy current;

    public EnvironmentProxy getCurrent() {
        return current;
    }

    void restore(EnvironmentProxy saved) {
        current = saved;
    }

    public void change(Environment environment) {
        current = new EnvironmentProxy(environment);
    }

    public void applyEffect(Module module) {
        if (current != null) current.applyEffect(module);
    }
}
