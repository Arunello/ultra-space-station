package station.modules;

public class CrewCabin extends ResidentialModule {

    private static final ModuleTypeInfo TYPE_INFO = new ModuleTypeInfo(100, 10, 75, 30);

    public CrewCabin(
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
    public CrewCabin clone() {
        return new CrewCabin(
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
        return "CrewCabin";
    }
}
