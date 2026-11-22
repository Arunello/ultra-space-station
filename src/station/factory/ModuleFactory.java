package station.factory;

import station.modules.*;
import station.modules.Module;

public interface ModuleFactory {
    Module createResidentialModule(String state, Capacity capacity);

    Module createEnergyModule(String state, Capacity capacity);

    Module createResearchModule(String state, Capacity capacity);

    default Module createModule(
        Module.Category category,
        String state,
        Capacity capacity
    ) {
        return switch (category) {
            case RESIDENTIAL -> createResidentialModule(state, capacity);
            case ENERGY -> createEnergyModule(state, capacity);
            case RESEARCH -> createResearchModule(state, capacity);
        };
    }
}
