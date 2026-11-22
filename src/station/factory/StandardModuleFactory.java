package station.factory;

import station.modules.*;
import station.modules.Module;

public class StandardModuleFactory implements ModuleFactory {

    @Override
    public Module createResidentialModule(String state, Capacity capacity) {
        return new CrewCabin(state, capacity);
    }

    @Override
    public Module createEnergyModule(String state, Capacity capacity) {
        return new SolarModule(state, capacity);
    }

    @Override
    public Module createResearchModule(String state, Capacity capacity) {
        return new BiologyLab(state, capacity);
    }
}
