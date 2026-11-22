package station.modules;

public abstract class ResidentialModule extends Module {

    protected ResidentialModule(
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
        return Module.Category.RESIDENTIAL;
    }
}
