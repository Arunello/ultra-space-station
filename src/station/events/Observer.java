package station.events;

public interface Observer {
    void update(ModuleEvents.ModuleEvent event);
}
