import java.util.Iterator;

public class Main {

    public static void main(String[] args) {
        Station station = new Station(10);

        ModuleFactory standardFactory = new StandardModuleFactory();
        ModuleFactory advancedFactory = new AdvancedModuleFactory();

        Module cabin = standardFactory.createResidentialModule(
            "Crew Cabin",
            75,
            "выключен",
            new Durability(90),
            new EnergyConsumption(10),
            new Capacity(32)
        );

        Module solar = standardFactory.createEnergyModule(
            "Solar Module",
            85,
            "выключен",
            new Durability(100),
            new EnergyConsumption(0),
            new Capacity(0)
        );

        Module physicsLab = advancedFactory.createResearchModule(
            "Physics Lab",
            88,
            "выключен",
            new Durability(95),
            new EnergyConsumption(188),
            new Capacity(4)
        );

        station.addModule(cabin);
        station.addModule(solar);
        station.addModule(physicsLab);

        System.out.println("Сценарий 1");

        for (Module module : station) {
            printModule(module);
        }

        Station copiedStation = new Station(station);

        Iterator<Module> copyIterator = copiedStation.iterator();

        System.out.println("\nСценарий 2");

        System.out.println("Копия:");
        for (Module module : copiedStation) {
            printModule(module);
        }

        if (copyIterator.hasNext()) {
            Module moduleToRemove = copyIterator.next();
            int removedId = moduleToRemove.getId();

            copiedStation.removeById(removedId);

            System.out.println("\nУдалённый модуль:");
            System.out.println(
                "ID: " +
                    moduleToRemove.getId() +
                    " | type: " +
                    moduleToRemove.getType() +
                    " | name: " +
                    moduleToRemove.getName()
            );
        }

        for (Module module : copiedStation) {
            switch (module.getType()) {
                case "PhysicsLab":
                    module.activate();
                    break;
                case "SolarModule":
                    module.getDurability().setValue(1);
                    break;
            }
        }

        Module biologyLab = standardFactory.createResearchModule(
            "Biology Lab",
            100,
            "авария",
            new Durability(50),
            new EnergyConsumption(1000),
            new Capacity(2)
        );

        copiedStation.addModule(biologyLab);

        System.out.println("\nОригинальная станция после изменения копии:");
        for (Module module : station) {
            printModule(module);
        }

        System.out.println("\nИзменённая копия:");
        for (Module module : copiedStation) {
            printModule(module);
        }
        System.out.println("\nСценарий 3");

        System.out.println("Перемещаем копию в оригиналную станцию:\n");

        station.transferFrom(copiedStation);

        for (Module module : station) {
            printModule(module);
        }

        for (Module module : copiedStation) {
            printModule(module);
        }
        System.out.println("\nСценарий 4");

        System.out.println("\nАктивация модулей:");

        for (Module module : station) {
            if (module.getState().equals("выключен")) {
                module.activate();
                System.out.println(
                    "Модуль " +
                        module.getName() +
                        " | ID: " +
                        module.getId() +
                        " | state: " +
                        module.getState()
                );
            }
        }

        System.out.println("\nДеактивация модулей:");

        for (Module module : station) {
            if (module.getState().equals("включён")) {
                module.deactivate();
                System.out.println(
                    "Модуль " +
                        module.getName() +
                        " | ID: " +
                        module.getId() +
                        " | state: " +
                        module.getState()
                );
            }
        }

        System.out.println("\nСмена состояния модуля:");

        Module moduleToChange = station.findById(physicsLab.getId());

        if (moduleToChange != null) {
            moduleToChange.setState("ремонт");

            System.out.println(
                "ID: " +
                    moduleToChange.getId() +
                    " | name: " +
                    moduleToChange.getName() +
                    " | state: " +
                    moduleToChange.getState()
            );
        }

        System.out.println("\nПеремещение модуля внутри станции:");

        Module moduleToMove = station.findById(solar.getId());

        station.moveModule(
            station.getIndexById(moduleToMove.getId()),
            station.getIndexById(cabin.getId())
        );

        System.out.println(
            "Модуль " +
                moduleToMove.getName() +
                " перемещён на позицию " +
                (station.getIndexById(moduleToMove.getId()) + 1)
        );

        System.out.println("\nСтанция после изменений:");

        for (Module module : station) {
            printModule(module);
        }

        System.out.println("\nСценарий 5");

        System.out.println("\nУдаление модуля по ID:");

        Module moduleById = station.findById(cabin.getId());

        if (moduleById != null) {
            int removedId = moduleById.getId();
            station.removeById(removedId);

            System.out.println("Удалён модуль с ID: " + removedId);
        }

        System.out.println("\nСтанция после удаления по ID:");

        for (Module module : station) {
            printModule(module);
        }

        System.out.println("\nУдаление модуля по индексу:");

        if (station.iterator().hasNext()) {
            Module moduleByIndex = station.iterator().next();
            int removedId = moduleByIndex.getId();

            station.removeByIndex(0);

            System.out.println(
                "Удалён модуль с ID: " + removedId + " | index: 0"
            );
        }

        System.out.println("\nСтанция после удаления по индексу:");

        for (Module module : station) {
            printModule(module);
        }

        System.out.println(
            "\nПроверка ограничения максимального количества модулей:"
        );

        Station limitedStation = new Station(1);

        limitedStation.addModule(
            standardFactory.createResidentialModule(
                "Test Cabin",
                70,
                "выключен",
                new Durability(80),
                new EnergyConsumption(10),
                new Capacity(10)
            )
        );

        System.out.println("Добавлен первый модуль.");

        try {
            limitedStation.addModule(
                standardFactory.createEnergyModule(
                    "Test Solar",
                    80,
                    "выключен",
                    new Durability(100),
                    new EnergyConsumption(0),
                    new Capacity(0)
                )
            );
        } catch (IllegalStateException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }

        System.out.println("\nСодержимое станции с ограничением:");

        for (Module module : limitedStation) {
            printModule(module);
        }
    }

    private static void printModule(Module module) {
        System.out.println(
            "ID: " +
                module.getId() +
                " | type: " +
                module.getType() +
                " | name: " +
                module.getName() +
                " | efficiency: " +
                module.getEfficiency() +
                " | state: " +
                module.getState() +
                " | durability: " +
                module.getDurability().getValue() +
                " | capacity: " +
                module.getCapacity().getValue() +
                " | energyConsumption: " +
                module.getEnergyConsumption().getValue()
        );
    }
}
