package station.modules;

public class CrewCabin extends ResidentialModule {

    private static final ModuleTypeInfo TYPE_INFO = new ModuleTypeInfo(
        100,
        10,
        75,
        30,
        10
    );

    public CrewCabin(String state, Capacity capacity) {
        super(
            TYPE_INFO.getBaseEfficiency(),
            state,
            new Durability(TYPE_INFO.getMaxHealth()),
            new EnergyConsumption(TYPE_INFO.getBaseEnergyConsumption()),
            capacity
        );
    }

    private CrewCabin(CrewCabin other) {
        super(other);
    }

    @Override
    public CrewCabin clone() {
        return new CrewCabin(this);
    }

    @Override
    public ModuleTypeInfo getTypeInfo() {
        return TYPE_INFO;
    }

    @Override
    public Module.Type getType() {
        return Module.Type.CREW_CABIN;
    }
}
