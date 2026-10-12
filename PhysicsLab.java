public class PhysicsLab extends ResearchModule {

    public PhysicsLab(
        String name,
        int efficiency,
        String state,
        Durability durability,
        EnergyConsumption energyConsumption,
        Capacity capacity
    ) {
        super(name, efficiency, state, durability, energyConsumption, capacity);
    }

    public PhysicsLab(
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
    public PhysicsLab clone() {
        return new PhysicsLab(
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
        return "PhysicsLab";
    }
}
