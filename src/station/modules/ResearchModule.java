package station.modules;

public abstract class ResearchModule extends Module {

    private static final double STANDBY_ENERGY_FRACTION = 0.10;

    private boolean activationBlocked;

    public boolean isActivationBlocked() {
        return activationBlocked;
    }

    public String describeResult(int points) {
        if (points == 0) return "исследование завершено без пригодных данных";
        return describeFindings(points >= 70);
    }

    protected abstract String describeFindings(boolean discovery);

    @Override
    public int getShiftEnergyConsumption() {
        return (int) Math.round(
            getEnergyConsumption().getValue() * STANDBY_ENERGY_FRACTION * getEnergyModifier()
        );
    }

    public int startResearch() {
        if (activationBlocked) throw new IllegalStateException("В поле астероидов исследования заблокированы");
        if (!("включён".equals(getState()) || "выключен".equals(getState()))) {
            throw new IllegalStateException("Исследование недоступно. Состояние лаборатории: " + getState());
        }
        if (getMediator() == null) throw new IllegalStateException("Лаборатория вне станции");
        if (!getMediator().tryConsumeEnergy(this, getEffectiveEnergyConsumption())) {
            throw new IllegalStateException("Недостаточно энергии для исследования");
        }
        activate();
        return (int) Math.round(getWorkingEfficiency() / getEnergyModifier());
    }

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
