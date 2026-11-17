package station.modules;

public class ReactorModule extends EnergyModule {

    private static final ModuleTypeInfo TYPE_INFO = new ModuleTypeInfo(100, 0, 95, 80);

    public ReactorModule(
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
    public ReactorModule clone() {
        return new ReactorModule(
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
        return "ReactorModule";
    }
}
