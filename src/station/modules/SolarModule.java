package station.modules;

public class SolarModule extends EnergyModule {

    private static final int MAX_OUTPUT_PER_SHIFT = 40;

    private static final ModuleTypeInfo TYPE_INFO = new ModuleTypeInfo(
        100,
        0,
        85,
        40,
        15
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

    @Override
    public int getOutputPerShift() {
        return (int) Math.round(MAX_OUTPUT_PER_SHIFT * getWorkingEfficiency() / (100.0 * getEnergyModifier()));
    }

    private SolarModule(SolarModule other) {
        super(other);
    }

    @Override
    public SolarModule clone() {
        return new SolarModule(this);
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
