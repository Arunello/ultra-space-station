public class EnergyConsumption {

    private static final int MIN_VALUE = 0;

    private int value;

    public EnergyConsumption(int value) {
        if (value < MIN_VALUE) {
            throw new IllegalArgumentException("Negative energyConsumption");
        }
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        if (value < MIN_VALUE) {
            throw new IllegalArgumentException(
                "Negative energyConsumption set"
            );
        }
        this.value = value;
    }

    public void decrease(int amount) {
        if (amount < MIN_VALUE || this.value < amount) {
            throw new IllegalArgumentException(
                "Negative energyConsumption decrease"
            );
        }
        this.value -= amount;
    }

    public void increase(int amount) {
        if (amount < MIN_VALUE) {
            throw new IllegalArgumentException(
                "Negative energyConsumption increase"
            );
        }
        this.value += amount;
    }

    public EnergyConsumption clone() {
        return new EnergyConsumption(this.value);
    }
}
