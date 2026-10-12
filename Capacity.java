public class Capacity {

    private static final int MIN_VALUE = 0;

    private int value;

    public Capacity(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Negative capacity");
        }
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        if (value < MIN_VALUE) {
            throw new IllegalArgumentException("Negative capacity set");
        }
        this.value = value;
    }

    public Capacity clone() {
        return new Capacity(this.value);
    }
}
