package station.modules;

public class ModuleTypeInfo {

    private final int maxHealth;
    private final int baseEnergyConsumption;
    private final int baseEfficiency;
    private final int cost;

    private final int asteroidDamage;

    public ModuleTypeInfo(int maxHealth, int baseEnergyConsumption, int baseEfficiency, int cost) {
        this(maxHealth, baseEnergyConsumption, baseEfficiency, cost, 0);
    }

    public ModuleTypeInfo(
        int maxHealth,
        int baseEnergyConsumption,
        int baseEfficiency,
        int cost,
        int asteroidDamage
    ) {
        this.maxHealth = maxHealth;
        this.baseEnergyConsumption = baseEnergyConsumption;
        this.baseEfficiency = baseEfficiency;
        this.cost = cost;
        this.asteroidDamage = asteroidDamage;
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

    public int getAsteroidDamage() {
        return asteroidDamage;
    }

    public int getCost() {
        return cost;
    }
}
