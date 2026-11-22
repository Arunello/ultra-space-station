package station.events;

public interface Observable {
    void subscribe(Observer observer);
    void unsubscribe(Observer observer);
    void notify(ModuleEvent event);
}
