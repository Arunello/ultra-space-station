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

    private final EnergySystem power;
    private final StationMediator mediator;
    private int oxygen;
    private int completedShifts;
    private int safetyLevel;
    private final Station station;
    private final EnvironmentSystem environment = new EnvironmentSystem();

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
        this.power = new EnergySystem(energy);
        this.mediator = new StationMediator(this);
        this.oxygen = oxygen;
        this.safetyLevel = safetyLevel;
    }

    public int getEnergy() {
        return power.getAvailable();
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
        return environment.getCurrent();
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
        if (getCurrentEnvironment() != null) {
            environmentName = getCurrentEnvironment().getName();
        }

        return (
            "Энергия: " +
            getEnergy() +
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
        return createModule(factory, category, capacity, null);
    }

    public Module createModule(
        ModuleFactory factory,
        Module.Category category,
        Capacity capacity,
        String name
    ) {
        Objects.requireNonNull(factory);
        Objects.requireNonNull(category);

        String state = "выключен";

        Module module = factory.createModule(category, state, capacity);

        if (name != null) module.setName(name);
        int cost = module.getTypeInfo().getCost();
        if (getEnergy() < cost) {
            throw new IllegalStateException(
                "Для строительства нужно " + cost + " энергии, доступно " + getEnergy()
            );
        }

        addModule(module);
        power.spend(cost);
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
        if (module.getMediator() != null) throw new IllegalStateException(
            "Модуль уже на станции"
        );
        station.addModule(module);
        module.attach(mediator);
        module.subscribe(mediator);
        environment.applyEffect(module);
    }

    public void removeModule(Module module) {
        Objects.requireNonNull(module);
        checkModuleOwnership(module);

        station.removeById(module.getId());
        module.unsubscribe(mediator);
        module.detach(mediator);
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

        if (amount < 0 || amount > getEnergy()) {
            throw new IllegalStateException("Not enough energy to charge");
        }

        module.chargeEnergy(amount);
        power.spend(amount);
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
        power.add(amount);
        LoggingProxy.log(
            LoggingProxy.Source.CENTER,
            "+" + amount + " энергии, всего " + getEnergy()
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
        return damage - (int) ((long) damage * getProtectionPercent() / 100);
    }

    public int getProtectionPercent() {
        return Math.min(safetyLevel, MAX_SAFETY_RESISTANCE);
    }

    public void setEnvironment(Environment environment) {
        Objects.requireNonNull(environment);

        this.environment.change(environment);
        LoggingProxy.log(
            LoggingProxy.Source.CENTER,
            "Окружение: " + getCurrentEnvironment().getName()
        );
        applyEnvironment();
    }

    private void applyEnvironment() {
        for (Module module : getModules()) {
            environment.applyEffect(module);
        }
    }

    public String interactWith(ExternalObject object, Module module) {
        Objects.requireNonNull(module);
        Objects.requireNonNull(object);

        checkModuleOwnership(module);

        return object.interact(this, module);
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
            default -> {
            }
        }
    }

    public int getCompletedShifts() {
        return completedShifts;
    }

    void advanceEnvironment() {
        applyEnvironment();
        completedShifts++;
    }

    void spendOxygen(int amount) {
        if (amount < 0) throw new IllegalArgumentException("Отрицательный расход кислорода");
        if (oxygen < amount) throw new IllegalStateException("Недостаточно кислорода");
        oxygen -= amount;
    }

    public StationMediator getMediator() {
        return mediator;
    }

    public Module getModuleByIndex(int index) {
        return station.getModuleByIndex(index);
    }

    void spendEnergy(int amount) {
        power.spend(amount);
    }

    public record Snapshot(
        int energy,
        int oxygen,
        int safety,
        int completedShifts,
        List<Module> order,
        List<Module.State> states,
        EnvironmentProxy environment,
        java.util.Map<Integer, Integer> damageCache
    ) {}

    public Snapshot snapshot() {
        var order = getModules();
        var proxy = getCurrentEnvironment();
        return new Snapshot(
            getEnergy(),
            oxygen,
            safetyLevel,
            completedShifts,
            order,
            order.stream().map(Module::saveState).toList(),
            proxy,
            proxy == null ? java.util.Map.of() : proxy.copyCache()
        );
    }

    public void restore(Snapshot snapshot) {
        for (Module module : getModules()) removeModule(module);
        power.restore(snapshot.energy());
        oxygen = snapshot.oxygen();
        completedShifts = snapshot.completedShifts();
        safetyLevel = snapshot.safety();
        for (int i = 0; i < snapshot.order().size(); i++) {
            Module module = snapshot.order().get(i);
            module.restoreState(snapshot.states().get(i));
            station.addModule(module);
            module.attach(mediator);
            module.subscribe(mediator);
        }
        environment.restore(snapshot.environment());
        if (snapshot.environment() != null) snapshot.environment().restoreCache(snapshot.damageCache());
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
