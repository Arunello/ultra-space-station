public abstract class Module {

    private static final int MIN_EFFICIENCY = 0;
    private static final int MAX_EFFICIENCY = 100;

    private int id;
    private String name;
    private int efficiency;
    private String state;
    private Durability durability;
    private EnergyConsumption energyConsumption;
    private Capacity capacity;

    private static int nextId = 0;

    public Module(
        String name,
        int efficiency,
        String state,
        Durability durability,
        EnergyConsumption energyConsumption,
        Capacity capacity
    ) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (efficiency < MIN_EFFICIENCY || efficiency > MAX_EFFICIENCY) {
            throw new IllegalArgumentException(
                "Efficiency must be between MIN_EFFICIENCY and MAX_EFFICIENCY"
            );
        }
        if (
            !"включён".equals(state) &&
            !"выключен".equals(state) &&
            !"ремонт".equals(state) &&
            !"авария".equals(state)
        ) {
            throw new IllegalArgumentException("Invalid state");
        }
        if (
            durability == null || energyConsumption == null || capacity == null
        ) {
            throw new IllegalArgumentException(
                "Module attributes cannot be null"
            );
        }

        this.id = ++nextId;
        this.name = name;
        this.efficiency = efficiency;
        this.state = state;
        this.durability = durability;
        this.energyConsumption = energyConsumption;
        this.capacity = capacity;
    }

    public Module(
        int id,
        String name,
        int efficiency,
        String state,
        Durability durability,
        EnergyConsumption energyConsumption,
        Capacity capacity
    ) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (efficiency < MIN_EFFICIENCY || efficiency > MAX_EFFICIENCY) {
            throw new IllegalArgumentException(
                "Efficiency must be between MIN_EFFICIENCY and MAX_EFFICIENCY"
            );
        }
        if (
            !"включён".equals(state) &&
            !"выключен".equals(state) &&
            !"ремонт".equals(state) &&
            !"авария".equals(state)
        ) {
            throw new IllegalArgumentException("Invalid state");
        }
        if (
            durability == null || energyConsumption == null || capacity == null
        ) {
            throw new IllegalArgumentException(
                "Module attributes cannot be null"
            );
        }

        this.id = ++nextId;
        this.name = name;
        this.efficiency = efficiency;
        this.state = state;
        this.durability = durability;
        this.energyConsumption = energyConsumption;
        this.capacity = capacity;
    }

    public abstract Module clone();

    public abstract String getType();

    public final int getId() {
        return id;
    }

    public final String getName() {
        return name;
    }

    public final int getEfficiency() {
        return efficiency;
    }

    public final String getState() {
        return state;
    }

    public final Durability getDurability() {
        return durability;
    }

    public final EnergyConsumption getEnergyConsumption() {
        return energyConsumption;
    }

    public final Capacity getCapacity() {
        return capacity;
    }

    public void activate() {
        setState("включён");
    }

    public void deactivate() {
        setState("выключен");
    }

    public void setState(String state) {
        if (
            !"включён".equals(state) &&
            !"выключен".equals(state) &&
            !"ремонт".equals(state) &&
            !"авария".equals(state)
        ) {
            throw new IllegalArgumentException("Invalid state");
        }
        this.state = state;
    }

    public void moveToIndex(Station station, int index) {
        int fromIndex = 0;

        for (Module module : station) {
            if (module == this) {
                station.moveModule(fromIndex, index);
                return;
            }
            fromIndex++;
        }

        throw new IllegalArgumentException("Module not found in station");
    }
}
