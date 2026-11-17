package station.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
        String category,
        String name,
        int efficiency,
        Durability durability,
        EnergyConsumption energyConsumption,
        Capacity capacity
    ) {
        String state = "выключен";
        Module module = switch (category) {
            case "residential" -> factory.createResidentialModule(
                name,
                efficiency,
                state,
                durability,
                energyConsumption,
                capacity
            );
            case "energy" -> factory.createEnergyModule(
                name,
                efficiency,
                state,
                durability,
                energyConsumption,
                capacity
            );
            case "research" -> factory.createResearchModule(
                name,
                efficiency,
                state,
                durability,
                energyConsumption,
                capacity
            );
            default -> throw new IllegalArgumentException(
                "Unknown category: " + category
            );
        };

        int cost = module.getTypeInfo().getCost();
        if (energy < cost) {
            throw new IllegalStateException("Not enough energy to build module");
        }

        addModule(module);
        energy -= cost;
        LoggingProxy.log(
            LoggingProxy.Source.CENTER,
            "Создан " + module.getType() + " " + name + ", стоимость " + cost
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
        station.removeById(module.getId());
        module.unsubscribe(this);
    }

    public void teleportModule(Module module, int index) {
        if (station.getIndexById(module.getId()) >= 0) {
            module.moveToIndex(station, index);
        }
    }

    public void chargeModule(Module module, int amount) {
        if (amount < 0 || amount > energy) {
            throw new IllegalStateException("Not enough energy to charge");
        }
        energy -= amount;
        module.chargeEnergy(amount);
    }

    public void addResources(ResourcePackage resources) {
        addEnergy(resources.energy());
        addOxygen(resources.oxygen());
        addSafetyLevel(resources.safetyLevel());
    }

    public void addEnergy(int amount) {
        energy += amount;
        LoggingProxy.log(
            LoggingProxy.Source.CENTER,
            "+" + amount + " энергии, всего " + energy
        );
    }

    public void addOxygen(int amount) {
        oxygen += amount;
        LoggingProxy.log(
            LoggingProxy.Source.CENTER,
            "+" + amount + " кислорода, всего " + oxygen
        );
    }

    public void addSafetyLevel(int amount) {
        safetyLevel += amount;
    }

    public int applySafety(int damage) {
        int percent = Math.min(safetyLevel, MAX_SAFETY_RESISTANCE);
        return damage - damage * percent / 100;
    }

    public void setEnvironment(Environment environment) {
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
        object.interact(this, module);
    }

    public void update(ModuleEvents.ModuleEvent event) {
        onModuleEvent(event);
    }

    public void onModuleEvent(ModuleEvents.ModuleEvent event) {
        switch (event.type()) {
            case ModuleEvents.DAMAGED -> {
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
            case ModuleEvents.DESTROYED -> {
                safetyLevel = Math.max(0, safetyLevel - DESTROYED_SAFETY_PENALTY);
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
            case ModuleEvents.ENERGY_CONSUMED -> {
                energy = Math.max(0, energy - event.amount());
            }
            case ModuleEvents.ACTIVATED -> {
                Module module = station.findById(event.moduleId());
                if (module instanceof ResidentialModule) {
                    oxygen = Math.max(0, oxygen - module.getCapacity().getValue());
                }
            }
            default -> {}
        }
    }
}
