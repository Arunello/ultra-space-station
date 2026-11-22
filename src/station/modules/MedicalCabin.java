package station.modules;

public class MedicalCabin extends ResidentialModule {

    private static final ModuleTypeInfo TYPE_INFO = new ModuleTypeInfo(
        100,
        15,
        85,
        50
    );

    public MedicalCabin(String state, Capacity capacity) {
        super(
            TYPE_INFO.getBaseEfficiency(),
            state,
            new Durability(TYPE_INFO.getMaxHealth()),
            new EnergyConsumption(TYPE_INFO.getBaseEnergyConsumption()),
            capacity
        );
    }

    private MedicalCabin(
        int efficiency,
        String state,
        Durability durability,
        EnergyConsumption energyConsumption,
        Capacity capacity
    ) {
        super(efficiency, state, durability, energyConsumption, capacity);
    }

    @Override
    public MedicalCabin clone() {
        return new MedicalCabin(
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
    public Module.Type getType() {
        return Module.Type.MEDICAL_CABIN;
    }
}
