package station.modules;

public class PhysicsLab extends ResearchModule {

    private static final ModuleTypeInfo TYPE_INFO = new ModuleTypeInfo(
        100,
        60,
        88,
        70,
        10
    );

    public PhysicsLab(String state, Capacity capacity) {
        super(
            TYPE_INFO.getBaseEfficiency(),
            state,
            new Durability(TYPE_INFO.getMaxHealth()),
            new EnergyConsumption(TYPE_INFO.getBaseEnergyConsumption()),
            capacity
        );
    }

    private PhysicsLab(PhysicsLab other) {
        super(other);
    }

    @Override
    public PhysicsLab clone() {
        return new PhysicsLab(this);
    }

    @Override
    public ModuleTypeInfo getTypeInfo() {
        return TYPE_INFO;
    }

    @Override
    public Module.Type getType() {
        return Module.Type.PHYSICS_LAB;
    }
    @Override
    protected String describeFindings(boolean discovery) {
        return discovery ? "обнаружен новый спектральный сигнал" : "получен спектр излучения";
    }
}
