package station.modules;

public class SolarModule extends EnergyModule {

    private static final ModuleTypeInfo TYPE_INFO = new ModuleTypeInfo(
        100,
        0,
        85,
        40
    );

    public SolarModule(String state, Capacity capacity) {
        super(
            TYPE_INFO.getBaseEfficiency(),
            state,
            new Durability(TYPE_INFO.getMaxHealth()),
            new EnergyConsumption(TYPE_INFO.getBaseEnergyConsumption()),
            capacity
        );
    }

    private SolarModule(
        int efficiency,
        String state,
        Durability durability,
        EnergyConsumption energyConsumption,
        Capacity capacity
    ) {
        super(efficiency, state, durability, energyConsumption, capacity);
    }

    @Override
    public SolarModule clone() {
        return new SolarModule(
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
        return Module.Type.SOLAR_MODULE;
    }
}
