package station.modules;

public class BiologyLab extends ResearchModule {

    private static final ModuleTypeInfo TYPE_INFO = new ModuleTypeInfo(
        100,
        40,
        80,
        60,
        12
    );

    public BiologyLab(String state, Capacity capacity) {
        super(
            TYPE_INFO.getBaseEfficiency(),
            state,
            new Durability(TYPE_INFO.getMaxHealth()),
            new EnergyConsumption(TYPE_INFO.getBaseEnergyConsumption()),
            capacity
        );
    }

    private BiologyLab(BiologyLab other) {
        super(other);
    }

    @Override
    public BiologyLab clone() {
        return new BiologyLab(this);
    }

    @Override
    public ModuleTypeInfo getTypeInfo() {
        return TYPE_INFO;
    }

    @Override
    public Module.Type getType() {
        return Module.Type.BIOLOGY_LAB;
    }
    @Override
    protected String describeFindings(boolean discovery) {
        return discovery ? "обнаружен новый микроорганизм" : "получены данные о росте культуры";
    }
}
