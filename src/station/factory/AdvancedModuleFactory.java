package station.factory;

import station.modules.*;
import station.modules.Module;

public class AdvancedModuleFactory implements ModuleFactory {

    public Module createResidentialModule(
        String name,
        int efficiency,
        String state,
        Durability durability,
        EnergyConsumption energyConsumption,
        Capacity capacity
    ) {
        return new MedicalCabin(
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
        return new ReactorModule(
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
        return new PhysicsLab(
            name,
            efficiency,
            state,
            durability,
            energyConsumption,
            capacity
        );
    }
}
