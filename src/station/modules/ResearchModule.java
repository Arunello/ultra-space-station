package station.modules;

public abstract class ResearchModule extends Module {

    protected ResearchModule(
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
        return Module.Category.RESEARCH;
    }
}
