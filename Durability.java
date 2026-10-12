public class Durability {

    private static final int MAX_VALUE = 100;
    private static final int MIN_VALUE = 0;

    private int value;

    public Durability(int value) {
        if (value < MIN_VALUE || value > MAX_VALUE) {
            throw new IllegalArgumentException(
                "Durability is between MIN_VALUE and MAX_VALUE"
            );
        }
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        if (value < MIN_VALUE || value > MAX_VALUE) {
            throw new IllegalArgumentException(
                "Durability must be between MIN_VALUE and MAX_VALUE"
            );
        }
        this.value = value;
    }

    public void decrease(int amount) {
        if (amount < MIN_VALUE || this.value < amount) {
            throw new IllegalArgumentException("Negative durability decrease");
        }
        this.value -= amount;
    }

    public void repair(int amount) {
        if (amount < MIN_VALUE || this.value + amount > MAX_VALUE) {
            throw new IllegalArgumentException(
                "Negative durability repair amount"
            );
        }
        this.value += amount;
    }

    public Durability clone() {
        return new Durability(this.value);
    }
}
