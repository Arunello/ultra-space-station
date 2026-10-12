public class ReactorModule extends EnergyModule {

    public ReactorModule(
        String name,
        int efficiency,
        String state,
        Durability durability,
        EnergyConsumption energyConsumption,
        Capacity capacity
    ) {
        super(name, efficiency, state, durability, energyConsumption, capacity);
    }

    public ReactorModule(
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
    public ReactorModule clone() {
        return new ReactorModule(
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
        return "ReactorModule";
    }
}
