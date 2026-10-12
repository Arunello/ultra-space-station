public interface ModuleFactory {
    Module createResidentialModule(
        String name,
        int efficiency,
        String state,
        Durability durability,
        EnergyConsumption energyConsumption,
        Capacity capacity
    );

    Module createEnergyModule(
        String name,
        int efficiency,
        String state,
        Durability durability,
        EnergyConsumption energyConsumption,
        Capacity capacity
    );

    Module createResearchModule(
        String name,
        int efficiency,
        String state,
        Durability durability,
        EnergyConsumption energyConsumption,
        Capacity capacity
    );
}
