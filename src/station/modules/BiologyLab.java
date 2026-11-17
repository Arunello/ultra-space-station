package station.modules;

public class BiologyLab extends ResearchModule {

    private static final ModuleTypeInfo TYPE_INFO = new ModuleTypeInfo(100, 40, 80, 60);

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

    @Override
    public BiologyLab clone() {
        return new BiologyLab(
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
        return "BiologyLab";
    }
}
