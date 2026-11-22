package station.app;

import station.core.*;
import station.environment.*;
import station.factory.*;
import station.modules.*;
import station.modules.Module;
import station.objects.*;

public class Main {

    private static final ModuleFactory standardFactory =
        new StandardModuleFactory();
    private static final ModuleFactory advancedFactory =
        new AdvancedModuleFactory();

    private static Module cabin;
    private static Module solar;
    private static Module physics;

    public static void main(String[] args) {
        CommandCenter center = new CommandCenter(6, 1000, 300, 50);

        scenario1(center);
        scenario2(center);
        scenario3(center);
        scenario4(center);
    }

    private static void scenario1(CommandCenter center) {
        title(
            "Сценарий 1: командный центр, модули, EarthOrbit, передача энергии"
        );

        center.setEnvironment(new EarthOrbit());

        cabin = center.createModule(
            standardFactory,
            Module.Category.RESIDENTIAL,
            new Capacity(32)
        );
        solar = center.createModule(
            standardFactory,
            Module.Category.ENERGY,
            new Capacity(0)
        );
        center.createModule(
            advancedFactory,
            Module.Category.ENERGY,
            new Capacity(0)
        );
        physics = center.createModule(
            advancedFactory,
            Module.Category.RESEARCH,
            new Capacity(4)
        );
        center.createModule(
            standardFactory,
            Module.Category.RESEARCH,
            new Capacity(2)
        );

        for (Module module : center.getModules()) {
            module.activate();
        }
        printAll(center);

        System.out.println("\nПередача энергии между модулями:");
        center.chargeModule(solar, 50);
        solar.transferEnergyTo(physics, 30);
        System.out.println(
            "Накоплено: Solar=" +
                solar.getStoredEnergy() +
                ", Physics=" +
                physics.getStoredEnergy()
        );
    }

    private static void scenario2(CommandCenter center) {
        title("Сценарий 2: DeepSpace, AsteroidField, Proxy и Observer");

        center.setEnvironment(new DeepSpace());
        System.out.println(
            "Коэффициент энергопотребления Physics Lab: x" +
                physics.getEnergyModifier()
        );

        System.out.println(
            "\nПовторное воздействие DeepSpace:"
        );
        center.getCurrentEnvironment().applyEffect(physics);

        System.out.println(
            "\nAsteroidField: исследовательские модули блокируются:"
        );
        center.setEnvironment(new AsteroidField());
        System.out.println(
            "Кеш Proxy: урон Solar Module = " +
                center.getCurrentEnvironment().getCachedDamage(solar.getId())
        );
        physics.activate();
        System.out.println("После попытки включения: " + physics.getState());

        System.out.println("\nВозврат на EarthOrbit и повторное включение:");
        center.setEnvironment(new EarthOrbit());
        physics.activate();
        System.out.println("После включения на орбите: " + physics.getState());
        printAll(center);
    }

    private static void scenario3(CommandCenter center) {
        title("Сценарий 3: солнечная буря");
        center.interactWith(new SolarStorm(1.5), solar);
        printModule(solar);
        System.out.println(center.getStatistics());
    }

    private static void scenario4(CommandCenter center) {
        title("Сценарий 4: грузовой контейнер");

        System.out.println("До: " + center.getStatistics());
        center.interactWith(new CargoContainer(), cabin);
        System.out.println("После: " + center.getStatistics());
    }

    private static void title(String text) {
        System.out.println(
            "\n==================== " + text + " ===================="
        );
    }

    private static void printAll(CommandCenter center) {
        System.out.println(center.getStatistics());
        for (Module module : center.getModules()) {
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
                " | energyConsumption: " +
                module.getEffectiveEnergyConsumption()
        );
    }
}
