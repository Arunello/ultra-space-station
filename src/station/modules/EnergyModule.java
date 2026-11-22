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

    @Override
    public final Module.Category getCategory() {
        return Module.Category.ENERGY;
    }
}
