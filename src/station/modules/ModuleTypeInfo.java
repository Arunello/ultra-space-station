package station.modules;

public class ModuleTypeInfo {

    private final int maxHealth;
    private final int baseEnergyConsumption;
    private final int baseEfficiency;
    private final int cost;

    public ModuleTypeInfo(
        int maxHealth,
        int baseEnergyConsumption,
        int baseEfficiency,
        int cost
    ) {
        this.maxHealth = maxHealth;
        this.baseEnergyConsumption = baseEnergyConsumption;
        this.baseEfficiency = baseEfficiency;
        this.cost = cost;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getBaseEnergyConsumption() {
        return baseEnergyConsumption;
    }

    public int getBaseEfficiency() {
        return baseEfficiency;
    }

    public int getCost() {
        return cost;
    }
}
