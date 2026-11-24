package station.environment;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import station.logging.LoggingProxy;
import station.modules.Module;
import station.modules.ResearchModule;

public class EnvironmentProxy implements Environment {

    private final Environment realEnvironment;

    private final Map<Integer, Integer> damageCache = new HashMap<>();

    public EnvironmentProxy(Environment realEnvironment) {
        this.realEnvironment = Objects.requireNonNull(realEnvironment);
    }

    public void applyEffect(Module module) {
        String prefix = "Proxy[" + getName() + "]: " + module.getName();

        if ("авария".equals(module.getState())) {
            LoggingProxy.log(
                LoggingProxy.Source.ENV,
                prefix + " в аварии, воздействие заблокировано"
            );
            return;
        }

        if (module instanceof ResearchModule) {
            LoggingProxy.log(
                LoggingProxy.Source.ENV,
                prefix + " - исследовательский модуль, проверка пройдена"
            );
        }

        int before = module.getDurability().getValue();
        realEnvironment.applyEffect(module);
        int lost = before - module.getDurability().getValue();

        damageCache.put(module.getId(), lost);
        if (lost > 0) {
            LoggingProxy.log(LoggingProxy.Source.ENV, prefix + ", потеря прочности: " + lost);
        }
    }

    public Map<Integer, Integer> copyCache() {
        return Map.copyOf(damageCache);
    }

    public void restoreCache(Map<Integer, Integer> saved) {
        damageCache.clear();
        damageCache.putAll(saved);
    }

    public int getCachedDamage(int moduleId) {
        return damageCache.getOrDefault(moduleId, 0);
    }

    public String getName() {
        return realEnvironment.getName();
    }

    public double getEnergyModifier() {
        return realEnvironment.getEnergyModifier();
    }

    public double getDamageModifier() {
        return realEnvironment.getDamageModifier();
    }
}
