package station.modules;

public abstract class ResearchModule extends Module {

    private boolean activationBlocked;

    public void setActivationBlocked(boolean blocked) {
        activationBlocked = blocked;
        if (blocked && "включён".equals(getState())) {
            deactivate();
        }
    }

    @Override
    public void activate() {
        if (!activationBlocked) {
            super.activate();
        }
    }

    protected ResearchModule(
        int efficiency,
        String state,
        Durability durability,
        EnergyConsumption energyConsumption,
        Capacity capacity
    ) {
        super(efficiency, state, durability, energyConsumption, capacity);
    }

    protected ResearchModule(ResearchModule other) {
        super(other);
        this.activationBlocked = other.activationBlocked;
    }

    @Override
    public final Module.Category getCategory() {
        return Module.Category.RESEARCH;
    }
}
