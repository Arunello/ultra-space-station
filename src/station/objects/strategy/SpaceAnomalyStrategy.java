package station.objects.strategy;

import java.util.Random;
import station.core.CommandCenter;
import station.logging.LoggingProxy;
import station.modules.Module;

public class SpaceAnomalyStrategy implements ExternalObjectStrategy {

    private static final int EFFECT_COUNT = 4;
    private static final double EFFICIENCY_FACTOR = 0.5;
    private final Random random = new Random();

    public void apply(CommandCenter commandCenter, Module module) {
        int effect = random.nextInt(EFFECT_COUNT);

        switch (effect) {
            case 0 -> {
                module.deactivate();
                log("модуль " + module.getName() + " отключён");
            }
            case 1 -> {
                commandCenter.addEnergy(commandCenter.getEnergy());
                log("энергия станции удвоена");
            }
            case 2 -> {
                int index = random.nextInt(commandCenter.getModuleCount());
                commandCenter.teleportModule(module, index);
                log(
                    "модуль " + module.getName() + " телепортирован на " + index
                );
            }
            case 3 -> {
                module.multiplyEfficiency(EFFICIENCY_FACTOR);
                log("эффективность " + module.getName() + " уменьшена вдвое");
            }
        }
    }

    private void log(String message) {
        LoggingProxy.log(LoggingProxy.Source.OBJECT, "Аномалия: " + message);
    }
}
