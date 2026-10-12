public class SolarModule extends EnergyModule {

    public SolarModule(
        String name,
        int efficiency,
        String state,
        Durability durability,
        EnergyConsumption energyConsumption,
        Capacity capacity
    ) {
        super(name, efficiency, state, durability, energyConsumption, capacity);
    }

    public SolarModule(
        int id,
        String name,
        int efficiency,
        String state,
        Durability durability,
        EnergyConsumption energyConsumption,
        Capacity capacity
    ) {
        super(
            id,
            name,
            efficiency,
            state,
            durability,
            energyConsumption,
            capacity
        );
    }

    @Override
    public SolarModule clone() {
        return new SolarModule(
            getId(),
            getName(),
            getEfficiency(),
            getState(),
            getDurability().clone(),
            getEnergyConsumption().clone(),
            getCapacity().clone()
        );
    }

    @Override
    public String getType() {
        return "SolarModule";
    }
}
