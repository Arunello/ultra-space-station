package station.events;

import java.util.*;

public class ModuleEvents {

    public static final String ACTIVATED = "Activated";
    public static final String DEACTIVATED = "Deactivated";
    public static final String DAMAGED = "Damaged";
    public static final String DESTROYED = "Destroyed";
    public static final String ENERGY_CONSUMED = "EnergyConsumed";

    private final List<Observer> observers = new ArrayList<>();

    public void subscribe(Observer observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void unsubscribe(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers(ModuleEvent event) {
        for (Observer observer : new ArrayList<>(observers)) {
            observer.update(event);
        }
    }
}
