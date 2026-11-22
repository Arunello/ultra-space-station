package station.modules;

public class MedicalCabin extends ResidentialModule {

    private static final ModuleTypeInfo TYPE_INFO = new ModuleTypeInfo(
        100,
        15,
        85,
        50,
        8
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

    private MedicalCabin(MedicalCabin other) {
        super(other);
    }

    @Override
    public MedicalCabin clone() {
        return new MedicalCabin(this);
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
