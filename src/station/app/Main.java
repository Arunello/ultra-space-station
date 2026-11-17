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
    private static Module reactor;
    private static Module physics;
    private static Module biology;

    public static void main(String[] args) {
        CommandCenter center = new CommandCenter(6, 1000, 300, 50);

        scenario1(center);
        scenario2(center);
        scenario3(center);
        scenario4(center);
        scenario5(center);
    }

    private static void scenario1(CommandCenter center) {
        title(
            "Сценарий 1: командный центр, модули, EarthOrbit, передача энергии"
        );

        center.setEnvironment(new EarthOrbit());

        cabin = center.createModule(
            standardFactory,
            "residential",
            "Crew Cabin",
            75,
            new Durability(100),
            new EnergyConsumption(10),
            new Capacity(32)
        );
        solar = center.createModule(
            standardFactory,
            "energy",
            "Solar Module",
            85,
            new Durability(100),
            new EnergyConsumption(0),
            new Capacity(0)
        );
        reactor = center.createModule(
            advancedFactory,
            "energy",
            "Reactor",
            95,
            new Durability(100),
            new EnergyConsumption(0),
            new Capacity(0)
        );
        physics = center.createModule(
            advancedFactory,
            "research",
            "Physics Lab",
            88,
            new Durability(100),
            new EnergyConsumption(60),
            new Capacity(4)
        );
        biology = center.createModule(
            standardFactory,
            "research",
            "Biology Lab",
            80,
            new Durability(100),
            new EnergyConsumption(40),
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

        System.out.println(
            "Biology Lab в аварии: Proxy не пропустит воздействие."
        );
        biology.setState("авария");
        center.setEnvironment(new DeepSpace());
        biology.setState("включён");
        System.out.println(
            "Коэффициент энергопотребления Physics Lab: x" +
                physics.getEnergyModifier()
        );

        System.out.println(
            "\nПовторное применение того же окружения берётся из кеша:"
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
        printAll(center);

        System.out.println("\nВозврат на EarthOrbit и повторное включение:");
        center.setEnvironment(new EarthOrbit());
        for (Module module : center.getModules()) {
            module.activate();
        }
        printAll(center);
    }

    private static void scenario3(CommandCenter center) {
        title("Сценарий 3: солнечная буря и метеоритный поток");

        SolarStorm storm = new SolarStorm(1.5);
        for (Module module : center.getModules()) {
            center.interactWith(storm, module);
        }
        printAll(center);

        System.out.println(
            "\nМетеоритный поток бьёт по ослабленной Biology Lab:"
        );
        biology.getDurability().setValue(8);
        center.interactWith(new MeteorStorm(), biology);
        printAll(center);
    }

    private static void scenario4(CommandCenter center) {
        title("Сценарий 4: грузовой контейнер");

        System.out.println("До: " + center.getStatistics());
        center.interactWith(new CargoContainer(), cabin);
        System.out.println("После: " + center.getStatistics());
    }

    private static void scenario5(CommandCenter center) {
        title("Сценарий 5: космическая аномалия");

        SpaceAnomaly anomaly = new SpaceAnomaly();
        for (int i = 0; i < 6; i++) {
            Module target = center
                .getModules()
                .get(i % center.getModuleCount());
            center.interactWith(anomaly, target);
        }
        printAll(center);
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
