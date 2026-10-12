import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Station implements Iterable<Module> {

    private List<Module> modules;
    private int maxModules;

    public Station(int maxModules) {
        if (maxModules < 0) {
            throw new IllegalArgumentException(
                "Maximum modules cannot be negative"
            );
        }

        this.modules = new ArrayList<>();
        this.maxModules = maxModules;
    }

    public Station(Station other) {
        this.modules = new ArrayList<>();
        this.maxModules = other.maxModules;

        for (Module module : other.modules) {
            this.modules.add(module.clone());
        }
    }

    @Override
    public Iterator<Module> iterator() {
        return new StationIterator();
    }

    private class StationIterator implements Iterator<Module> {

        private final Iterator<Module> modulesIterator = modules.iterator();

        @Override
        public boolean hasNext() {
            return modulesIterator.hasNext();
        }

        @Override
        public Module next() {
            return modulesIterator.next();
        }
    }

    public Iterator<Module> getIterator() {
        return iterator();
    }

    public void addModule(Module module) {
        if (modules.size() >= maxModules) {
            throw new IllegalStateException("Station is full");
        }
        for (Module m : modules) {
            if (m.getId() == module.getId()) {
                throw new IllegalStateException("Id must be unique");
            }
        }

        this.modules.add(module);
    }

    public void removeById(int id) {
        for (int i = 0; i < modules.size(); i++) {
            if (modules.get(i).getId() == id) {
                removeByIndex(i);
                return;
            }
        }
    }

    public void removeByIndex(int index) {
        if (index < 0 || index >= modules.size()) {
            throw new IndexOutOfBoundsException("Invalid module index");
        }

        this.modules.remove(index);
    }

    public Module findById(int id) {
        for (Module module : modules) {
            if (module.getId() == id) {
                return module;
            }
        }

        return null;
    }

    public int getIndexById(int id) {
        for (int i = 0; i < modules.size(); i++) {
            if (modules.get(i).getId() == id) {
                return i;
            }
        }

        return -1;
    }

    public void moveModule(int fromIndex, int toIndex) {
        if (fromIndex < 0 || fromIndex >= modules.size()) {
            throw new IndexOutOfBoundsException("Invalid source index");
        }

        if (toIndex < 0 || toIndex >= modules.size()) {
            throw new IndexOutOfBoundsException("Invalid destination index");
        }

        Module module = modules.remove(fromIndex);
        modules.add(toIndex, module);
    }

    public void copyFrom(Station other) {
        if (this == other) {
            return;
        }

        List<Module> copies = new ArrayList<>();

        for (Module module : other.modules) {
            copies.add(module.clone());
        }

        this.modules = copies;
        this.maxModules = other.maxModules;
    }

    public void transferModuleTo(int moduleId, Station target) {
        if (target == this) {
            throw new IllegalArgumentException(
                "Target station must differ from source"
            );
        }

        if (target.modules.size() == target.maxModules) {
            throw new IllegalStateException("Target station is full");
        }

        Module module = findById(moduleId);

        if (module == null) {
            throw new IllegalArgumentException("Module not found: " + moduleId);
        }

        modules.remove(module);
        target.modules.add(module);
    }

    public void transferFrom(Station other) {
        if (this == other) {
            return;
        }

        if (this.modules.size() + other.modules.size() > this.maxModules) {
            throw new IllegalStateException(
                "Not enough capacity to transfer modules"
            );
        }

        while (!other.modules.isEmpty()) {
            Module module = other.modules.get(0);
            other.transferModuleTo(module.getId(), this);
        }
    }

    public void sortByEfficiency() {
        List<Module> sortedModules = new ArrayList<>();

        Iterator<Module> iterator = getIterator();

        while (iterator.hasNext()) {
            sortedModules.add(iterator.next());
        }

        sortedModules.sort((m1, m2) ->
            Integer.compare(m1.getEfficiency(), m2.getEfficiency())
        );

        modules = sortedModules;
    }
}
