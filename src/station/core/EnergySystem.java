package station.core;

public class EnergySystem {
    private int available;

    public EnergySystem(int available) {
        restore(available);
    }

    public int getAvailable() {
        return available;
    }

    void restore(int amount) {
        if (amount < 0) throw new IllegalArgumentException("Отрицательная энергия");
        available = amount;
    }

    public void spend(int amount) {
        if (amount < 0 || amount > available) {
            throw new IllegalStateException("Недостаточно энергии станции");
        }
        available -= amount;
    }

    public void add(int amount) {
        if (amount < 0) throw new IllegalArgumentException("Отрицательная энергия");
        if (amount > Integer.MAX_VALUE - available) {
            throw new IllegalStateException("Превышен максимальный запас энергии станции");
        }
        available += amount;
    }
}
