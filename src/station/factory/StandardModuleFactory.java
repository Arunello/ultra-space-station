package station.factory;

import station.modules.*;
import station.modules.Module;

public class StandardModuleFactory implements ModuleFactory {

    public Module createResidentialModule(
        String name,
        int efficiency,
        String state,
        Durability durability,
        EnergyConsumption energyConsumption,
        Capacity capacity
    ) {
        return new CrewCabin(
            name,
            efficiency,
            state,
            durability,
            energyConsumption,
            capacity
        );
    }

    public Module createEnergyModule(
        String name,
        int efficiency,
        String state,
        Durability durability,
        EnergyConsumption energyConsumption,
        Capacity capacity
    ) {
        return new SolarModule(
            name,
            efficiency,
            state,
            durability,
            energyConsumption,
            capacity
        );
    }

    public Module createResearchModule(
        String name,
        int efficiency,
        String state,
        Durability durability,
        EnergyConsumption energyConsumption,
        Capacity capacity
    ) {
        return new BiologyLab(
            name,
            efficiency,
            state,
            durability,
            energyConsumption,
            capacity
        );
    }
}
