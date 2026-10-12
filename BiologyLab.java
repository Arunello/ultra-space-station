public class BiologyLab extends ResearchModule {

    public BiologyLab(
        String name,
        int efficiency,
        String state,
        Durability durability,
        EnergyConsumption energyConsumption,
        Capacity capacity
    ) {
        super(name, efficiency, state, durability, energyConsumption, capacity);
    }

    public BiologyLab(
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
    public BiologyLab clone() {
        return new BiologyLab(
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
        return "BiologyLab";
    }
}
