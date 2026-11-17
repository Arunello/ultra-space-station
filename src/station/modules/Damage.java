package station.modules;

public record Damage(int amount, String type) {

    public Damage {
        if (amount < 0) {
            throw new IllegalArgumentException("Negative damage");
        }
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Damage type cannot be empty");
        }
    }
}
