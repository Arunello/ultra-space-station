package station.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import station.environment.*;
import station.events.*;
import station.factory.ModuleFactory;
import station.logging.LoggingProxy;
import station.modules.*;
import station.modules.Module;
import station.objects.ExternalObject;

public class CommandCenter implements Observer {

    private static final int MAX_SAFETY_RESISTANCE = 50;
    private static final int DAMAGED_SAFETY_PENALTY = 1;
    private static final int DESTROYED_SAFETY_PENALTY = 5;

    private int energy;
    private int oxygen;
    private int safetyLevel;
    private final Station station;
    private EnvironmentProxy currentEnvironment;

    public CommandCenter(
        int maxModules,
        int energy,
        int oxygen,
        int safetyLevel
    ) {
        if (energy < 0 || oxygen < 0 || safetyLevel < 0) {
            throw new IllegalArgumentException("Negative station parameter");
        }
        this.station = new Station(maxModules);
        this.energy = energy;
        this.oxygen = oxygen;
        this.safetyLevel = safetyLevel;
    }

    public int getEnergy() {
        return energy;
    }

    public int getOxygen() {
        return oxygen;
    }

    public int getSafetyLevel() {
        return safetyLevel;
    }

    public int getMaxModules() {
        return station.getMaxModules();
    }

    public int getModuleCount() {
        return station.size();
    }

    public EnvironmentProxy getCurrentEnvironment() {
        return currentEnvironment;
    }

    public List<Module> getModules() {
        List<Module> result = new ArrayList<>();
        for (Module module : station) {
            result.add(module);
        }
        return Collections.unmodifiableList(result);
    }

    public String getStatistics() {
        String environmentName = "нет";
        if (currentEnvironment != null) {
            environmentName = currentEnvironment.getName();
        }

        return (
            "Энергия: " +
            energy +
            " | Кислород: " +
            oxygen +
            " | Безопасность: " +
            safetyLevel +
            " | Модулей: " +
            station.size() +
            "/" +
            station.getMaxModules() +
            " | Окружение: " +
            environmentName
        );
    }

    public Module createModule(
        ModuleFactory factory,
        Module.Category category,
        Capacity capacity
    ) {
        Objects.requireNonNull(factory);
        Objects.requireNonNull(category);

        String state = "выключен";

        Module module = factory.createModule(category, state, capacity);

        int cost = module.getTypeInfo().getCost();
        if (energy < cost) {
            throw new IllegalStateException(
                "Not enough energy to build module"
            );
        }

        addModule(module);
        energy -= cost;
        LoggingProxy.log(
            LoggingProxy.Source.CENTER,
            "Создан " +
                module.getType() +
                " " +
                module.getName() +
                ", стоимость " +
                cost
        );
        return module;
    }

    public void addModule(Module module) {
        station.addModule(module);
        module.subscribe(this);
        if (currentEnvironment != null) {
            currentEnvironment.applyEffect(module);
        }
    }

    public void removeModule(Module module) {
        Objects.requireNonNull(module);
        checkModuleOwnership(module);

        station.removeById(module.getId());
        module.unsubscribe(this);
    }

    public void teleportModule(Module module, int index) {
        Objects.requireNonNull(module);
        checkModuleOwnership(module);

        if (station.getIndexById(module.getId()) >= 0) {
            module.moveToIndex(station, index);
        }
    }

    public void chargeModule(Module module, int amount) {
        Objects.requireNonNull(module);
        checkModuleOwnership(module);

        if (amount < 0 || amount > energy) {
            throw new IllegalStateException("Not enough energy to charge");
        }

        module.chargeEnergy(amount);
        energy -= amount;
    }

    public void addResources(ResourcePackage resources) {
        addEnergy(resources.energy());
        addOxygen(resources.oxygen());
        addSafetyLevel(resources.safetyLevel());
    }

    public void addEnergy(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        energy += amount;
        LoggingProxy.log(
            LoggingProxy.Source.CENTER,
            "+" + amount + " энергии, всего " + energy
        );
    }

    public void addOxygen(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        oxygen += amount;
        LoggingProxy.log(
            LoggingProxy.Source.CENTER,
            "+" + amount + " кислорода, всего " + oxygen
        );
    }

    public void addSafetyLevel(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        safetyLevel += amount;
    }

    public int applySafety(int damage) {
        if (damage < 0) {
            throw new IllegalArgumentException("Damage cannot be negative");
        }
        int percent = Math.min(safetyLevel, MAX_SAFETY_RESISTANCE);
        return damage - (damage * percent) / 100;
    }

    public void setEnvironment(Environment environment) {
        Objects.requireNonNull(environment);

        currentEnvironment = new EnvironmentProxy(environment);
        LoggingProxy.log(
            LoggingProxy.Source.CENTER,
            "Окружение: " + currentEnvironment.getName()
        );
        for (Module module : getModules()) {
            currentEnvironment.applyEffect(module);
        }
    }

    public void interactWith(ExternalObject object, Module module) {
        Objects.requireNonNull(module);
        Objects.requireNonNull(object);

        checkModuleOwnership(module);

        object.interact(this, module);
    }

    @Override
    public void onModuleEvent(ModuleEvent event) {
        switch (event.type()) {
            case ModuleEvent.DAMAGED -> {
                safetyLevel = Math.max(0, safetyLevel - DAMAGED_SAFETY_PENALTY);
                LoggingProxy.log(
                    LoggingProxy.Source.CENTER,
                    "Модуль " +
                        event.moduleId() +
                        " получил урон " +
                        event.damage() +
                        ", безопасность: " +
                        safetyLevel
                );
            }
            case ModuleEvent.DESTROYED -> {
                safetyLevel = Math.max(
                    0,
                    safetyLevel - DESTROYED_SAFETY_PENALTY
                );
                Module module = station.findById(event.moduleId());
                if (module != null) {
                    removeModule(module);
                }
                LoggingProxy.log(
                    LoggingProxy.Source.CENTER,
                    "Модуль " +
                        event.moduleId() +
                        " уничтожен и удалён, безопасность: " +
                        safetyLevel
                );
            }
            case ModuleEvent.ENERGY_CONSUMED -> {
                energy = Math.max(0, energy - event.amount());
            }
            case ModuleEvent.ACTIVATED -> {
                Module module = station.findById(event.moduleId());
                if (module instanceof ResidentialModule) {
                    oxygen = Math.max(
                        0,
                        oxygen - module.getCapacity().getValue()
                    );
                }
            }
            default -> {
            }
        }
    }

    private void checkModuleOwnership(Module module) {
        Objects.requireNonNull(module);

        if (station.findById(module.getId()) != module) {
            throw new IllegalArgumentException(
                "Module does not belong to this station"
            );
        }
    }
}
