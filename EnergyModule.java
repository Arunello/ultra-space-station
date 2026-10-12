public abstract class EnergyModule extends Module {

    public EnergyModule(
        String name,
        int efficiency,
        String state,
        Durability durability,
        EnergyConsumption energyConsumption,
        Capacity capacity
    ) {
        super(name, efficiency, state, durability, energyConsumption, capacity);
    }

    public EnergyModule(
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
}
