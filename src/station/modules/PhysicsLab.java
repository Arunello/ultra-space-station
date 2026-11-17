package station.modules;

public class PhysicsLab extends ResearchModule {

    private static final ModuleTypeInfo TYPE_INFO = new ModuleTypeInfo(100, 60, 88, 70);

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
    public ModuleTypeInfo getTypeInfo() {
        return TYPE_INFO;
    }

    @Override
    public String getType() {
        return "PhysicsLab";
    }
}
