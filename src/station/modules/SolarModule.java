package station.modules;

public class SolarModule extends EnergyModule {

    private static final ModuleTypeInfo TYPE_INFO = new ModuleTypeInfo(100, 0, 85, 40);

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

    @Override
    public SolarModule clone() {
        return new SolarModule(
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
        return "SolarModule";
    }
}
