package station.modules;

public class ReactorModule extends EnergyModule {

    private static final int MAX_OUTPUT_PER_SHIFT = 60;

    private static final ModuleTypeInfo TYPE_INFO = new ModuleTypeInfo(
        100,
        0,
        95,
        80,
        8
    );

    public ReactorModule(String state, Capacity capacity) {
        super(
            TYPE_INFO.getBaseEfficiency(),
            state,
            new Durability(TYPE_INFO.getMaxHealth()),
            new EnergyConsumption(TYPE_INFO.getBaseEnergyConsumption()),
            capacity
        );
    }

    private ReactorModule(ReactorModule other) {
        super(other);
    }

    @Override
    public int getOutputPerShift() {
        return (int) Math.round(MAX_OUTPUT_PER_SHIFT * getWorkingEfficiency() / 100.0);
    }

    @Override
    public ReactorModule clone() {
        return new ReactorModule(this);
    }

    @Override
    public ModuleTypeInfo getTypeInfo() {
        return TYPE_INFO;
    }

    @Override
    public Module.Type getType() {
        return Module.Type.REACTOR_MODULE;
    }
}
