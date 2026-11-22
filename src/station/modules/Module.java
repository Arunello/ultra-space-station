package station.modules;

import java.time.LocalDateTime;
import java.util.Objects;
import station.core.Station;
import station.events.ModuleEvent;
import station.events.ModuleEvents;
import station.events.Observable;
import station.events.Observer;
import station.logging.LoggingProxy;

public abstract class Module implements Observable {

    private static final int MIN_EFFICIENCY = 0;
    private static final int MAX_EFFICIENCY = 100;

    private final int id;
    private int efficiency;
    private String state;
    private Durability durability;
    private EnergyConsumption energyConsumption;
    private Capacity capacity;

    private final ModuleEvents events = new ModuleEvents();
    private double energyModifier = 1.0;
    private int storedEnergy = 0;

    private static int nextId = 0;

    public enum Type {
        CREW_CABIN,
        MEDICAL_CABIN,
        SOLAR_MODULE,
        REACTOR_MODULE,
        BIOLOGY_LAB,
        PHYSICS_LAB,
    }

    public enum Category {
        RESIDENTIAL,
        ENERGY,
        RESEARCH,
    }

    public Module(
        int efficiency,
        String state,
        Durability durability,
        EnergyConsumption energyConsumption,
        Capacity capacity
    ) {
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

        this.durability = Objects.requireNonNull(durability);
        this.energyConsumption = Objects.requireNonNull(energyConsumption);
        this.capacity = Objects.requireNonNull(capacity);

        this.id = ++nextId;
        this.efficiency = efficiency;
        this.state = state;
    }

    protected Module(Module other) {
        this(
            other.efficiency,
            other.state,
            other.durability.clone(),
            other.energyConsumption.clone(),
            other.capacity.clone()
        );
        this.energyModifier = other.energyModifier;
        this.storedEnergy = other.storedEnergy;
    }

    public abstract Module clone();

    public abstract Module.Category getCategory();

    public abstract Module.Type getType();

    public abstract ModuleTypeInfo getTypeInfo();

    public final int getId() {
        return id;
    }

    public String getName() {
        return getType().toString();
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
        if ("авария".equals(state) || "включён".equals(state)) {
            return;
        }
        setState("включён");
        notifyEvent(ModuleEvent.ACTIVATED, 0, 0);
        notifyEvent(
            ModuleEvent.ENERGY_CONSUMED,
            0,
            getEffectiveEnergyConsumption()
        );
    }

    public void deactivate() {
        if ("авария".equals(state) || "выключен".equals(state)) {
            return;
        }
        setState("выключен");
        notifyEvent(ModuleEvent.DEACTIVATED, 0, 0);
    }

    private void setState(String state) {
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
        if (!Double.isFinite(modifier) || modifier < 0) {
            throw new IllegalArgumentException("Invalid energy modifier");
        }
        this.energyModifier = modifier;
    }

    @Override
    public void subscribe(Observer observer) {
        events.subscribe(observer);
    }

    @Override
    public void unsubscribe(Observer observer) {
        events.unsubscribe(observer);
    }

    @Override
    public void notify(ModuleEvent event) {
        events.notifyObservers(event);
    }

    private void notifyEvent(String type, int damage, int amount) {
        LoggingProxy.log(LoggingProxy.Source.MODULE, getName() + ": " + type);
        notify(new ModuleEvent(type, id, damage, amount, LocalDateTime.now()));
    }

    public void subtractDurability(Damage damage) {
        int lost = Math.min(damage.amount(), durability.getValue());
        if (lost == 0) {
            return;
        }
        durability.decrease(lost);
        notifyEvent(ModuleEvent.DAMAGED, lost, 0);

        if (durability.getValue() == 0) {
            setState("авария");
            notifyEvent(ModuleEvent.DESTROYED, lost, 0);
        }
    }

    public void multiplyEfficiency(double factor) {
        if (!Double.isFinite(factor) || factor < 0) {
            throw new IllegalArgumentException("Invalid efficiency factor");
        }

        int result = (int) Math.round(efficiency * factor);
        efficiency = Math.min(MAX_EFFICIENCY, Math.max(MIN_EFFICIENCY, result));
    }

    public void chargeEnergy(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException(
                "Energy amount cannot be negative"
            );
        }
        storedEnergy += amount;
    }

    public void transferEnergyTo(Module target, int amount) {
        Objects.requireNonNull(target);

        if (amount < 0 || amount > storedEnergy) {
            throw new IllegalArgumentException("Not enough stored energy");
        }
        storedEnergy -= amount;
        target.storedEnergy += amount;
        LoggingProxy.log(
            LoggingProxy.Source.MODULE,
            getName() +
                " передал " +
                amount +
                " энергии модулю " +
                target.getName()
        );
    }

    public void moveToIndex(Station station, int index) {
        Objects.requireNonNull(station);

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
