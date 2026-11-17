package station.modules;

public class MedicalCabin extends ResidentialModule {

    private static final ModuleTypeInfo TYPE_INFO = new ModuleTypeInfo(100, 15, 85, 50);

    public MedicalCabin(
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
    public MedicalCabin clone() {
        return new MedicalCabin(
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
        return "MedicalCabin";
    }
}
