package station.modules;

public abstract class EnergyModule extends Module {

    protected EnergyModule(
        int efficiency,
        String state,
        Durability durability,
        EnergyConsumption energyConsumption,
        Capacity capacity
    ) {
        super(efficiency, state, durability, energyConsumption, capacity);
    }

    protected EnergyModule(EnergyModule other) {
        super(other);
    }

    @Override
    public final Module.Category getCategory() {
        return Module.Category.ENERGY;
    }
}
