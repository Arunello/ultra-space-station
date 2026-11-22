package station.factory;

import station.modules.*;
import station.modules.Module;

public class AdvancedModuleFactory implements ModuleFactory {

    @Override
    public Module createResidentialModule(String state, Capacity capacity) {
        return new MedicalCabin(state, capacity);
    }

    @Override
    public Module createEnergyModule(String state, Capacity capacity) {
        return new ReactorModule(state, capacity);
    }

    @Override
    public Module createResearchModule(String state, Capacity capacity) {
        return new PhysicsLab(state, capacity);
    }
}
