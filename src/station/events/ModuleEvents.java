package station.events;

import java.util.*;

public class ModuleEvents {

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
            observer.onModuleEvent(event);
        }
    }
}
