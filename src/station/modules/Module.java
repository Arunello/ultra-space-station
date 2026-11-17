package station.modules;

import java.time.LocalDateTime;
import station.core.Station;
import station.events.ModuleEvents;
import station.events.Observer;
import station.logging.LoggingProxy;

public abstract class Module {

    private static final int MIN_EFFICIENCY = 0;
    private static final int MAX_EFFICIENCY = 100;

    private int id;
    private String name;
    private int efficiency;
    private String state;
    private Durability durability;
    private EnergyConsumption energyConsumption;
    private Capacity capacity;

    private final ModuleEvents events = new ModuleEvents();
    private double energyModifier = 1.0;
    private int storedEnergy = 0;

    private static int nextId = 0;

    public Module(
        String name,
        int efficiency,
        String state,
        Durability durability,
        EnergyConsumption energyConsumption,
        Capacity capacity
    ) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (efficiency < MIN_EFFICIENCY || efficiency > MAX_EFFICIENCY) {
            throw new IllegalArgumentException(
                "Efficiency must be between MIN_EFFICIENCY and MAX_EFFICIENCY"
            );
        }
        if (
            !"включён".equals(state) &&
            !"выключен".equals(state) &&
            !"ремонт".equals(state) &&
            !"авария".equals(state)
        ) {
            throw new IllegalArgumentException("Invalid state");
        }
        if (
            durability == null || energyConsumption == null || capacity == null
        ) {
            throw new IllegalArgumentException(
                "Module attributes cannot be null"
            );
        }

        this.id = ++nextId;
        this.name = name;
        this.efficiency = efficiency;
        this.state = state;
        this.durability = durability;
        this.energyConsumption = energyConsumption;
        this.capacity = capacity;
    }

    public abstract Module clone();

    public abstract String getType();

    public abstract ModuleTypeInfo getTypeInfo();

    public final int getId() {
        return id;
    }

    public final String getName() {
        return name;
    }

    public final int getEfficiency() {
        return efficiency;
    }

    public final String getState() {
        return state;
    }

    public final Durability getDurability() {
        return durability;
    }

    public final EnergyConsumption getEnergyConsumption() {
        return energyConsumption;
    }

    public final Capacity getCapacity() {
        return capacity;
    }

    public final boolean isActive() {
        return "включён".equals(state);
    }

    public final double getEnergyModifier() {
        return energyModifier;
    }

    public final int getStoredEnergy() {
        return storedEnergy;
    }

    public final int getEffectiveEnergyConsumption() {
        return (int) Math.round(energyConsumption.getValue() * energyModifier);
    }

    public void activate() {
        if (isActive()) {
            return;
        }
        setState("включён");
        notifyEvent(ModuleEvents.ACTIVATED, 0, 0);
        notifyEvent(
            ModuleEvents.ENERGY_CONSUMED,
            0,
            getEffectiveEnergyConsumption()
        );
    }

    public void deactivate() {
        setState("выключен");
    }

    public void setState(String state) {
        if (
            !"включён".equals(state) &&
            !"выключен".equals(state) &&
            !"ремонт".equals(state) &&
            !"авария".equals(state)
        ) {
            throw new IllegalArgumentException("Invalid state");
        }
        this.state = state;
    }

    public void setEnergyModifier(double modifier) {
        this.energyModifier = modifier;
    }

    public void subscribe(Observer observer) {
        events.subscribe(observer);
    }

    public void unsubscribe(Observer observer) {
        events.unsubscribe(observer);
    }

    private void notifyEvent(String type, int damage, int amount) {
        LoggingProxy.log(LoggingProxy.Source.MODULE, name + ": " + type);
        events.notifyObservers(
            new ModuleEvents.ModuleEvent(
                type,
                id,
                damage,
                amount,
                LocalDateTime.now()
            )
        );
    }

    public void subtractDurability(Damage damage) {
        int lost = Math.min(damage.amount(), durability.getValue());
        if (lost == 0) {
            return;
        }
        durability.decrease(lost);
        notifyEvent(ModuleEvents.DAMAGED, lost, 0);

        if (durability.getValue() == 0) {
            setState("авария");
            notifyEvent(ModuleEvents.DESTROYED, lost, 0);
        }
    }

    public void multiplyEfficiency(double factor) {
        int result = (int) Math.round(efficiency * factor);
        efficiency = Math.min(MAX_EFFICIENCY, Math.max(MIN_EFFICIENCY, result));
    }

    public void chargeEnergy(int amount) {
        storedEnergy += amount;
    }

    public void transferEnergyTo(Module target, int amount) {
        if (amount < 0 || amount > storedEnergy) {
            throw new IllegalArgumentException("Not enough stored energy");
        }
        storedEnergy -= amount;
        target.storedEnergy += amount;
        LoggingProxy.log(
            LoggingProxy.Source.MODULE,
            name + " передал " + amount + " энергии модулю " + target.name
        );
    }

    public void moveToIndex(Station station, int index) {
        int fromIndex = 0;

        for (Module module : station) {
            if (module == this) {
                station.moveModule(fromIndex, index);
                return;
            }
            fromIndex++;
        }

        throw new IllegalArgumentException("Module not found in station");
    }
}
