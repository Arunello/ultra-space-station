package station.core;

public record ResourcePackage(int energy, int oxygen, int safetyLevel) {

    public ResourcePackage {
        if (energy < 0 || oxygen < 0 || safetyLevel < 0) {
            throw new IllegalArgumentException("Negative resource amount");
        }
    }
}
