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

    @Override
    public PhysicsLab clone() {
        return new PhysicsLab(
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
