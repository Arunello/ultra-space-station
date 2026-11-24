package station.objects.strategy;

import java.util.Random;
import station.core.CommandCenter;
import station.logging.LoggingProxy;
import station.modules.Module;

public class SpaceAnomalyStrategy implements ExternalObjectStrategy {

    private static final int DOUBLE_ENERGY = 1;
    private static final int EFFECT_COUNT = 4;
    private static final double EFFICIENCY_FACTOR = 0.5;
    private final Random random = new Random();

    public String apply(CommandCenter commandCenter, Module module) {
        int effect = random.nextInt(EFFECT_COUNT);

        if (effect != DOUBLE_ENERGY && random.nextInt(100) < commandCenter.getProtectionPercent()) {
            String message = "Защита станции нейтрализовала аномалию у модуля «" + module.getName() + "».";
            LoggingProxy.log(LoggingProxy.Source.OBJECT, message);
            return message;
        }

        String result = switch (effect) {
            case 0 -> {
                module.deactivate();
                yield "Отключение модуля «" + module.getName() + "». Состояние: " + module.getState();
            }
            case DOUBLE_ENERGY -> {
                commandCenter.addEnergy(commandCenter.getEnergy());
                yield "Запас энергии удвоен до " + commandCenter.getEnergy();
            }
            case 2 -> {
                int index = random.nextInt(commandCenter.getModuleCount());
                commandCenter.teleportModule(module, index);
                yield "Телепортация модуля «" + module.getName() + "» на позицию " + index;
            }
            default -> {
                module.multiplyEfficiency(EFFICIENCY_FACTOR);
                yield "Эффективность модуля «" + module.getName() + "» снижена до " + module.getEfficiency() + "%";
            }
        };
        String message = "Аномалия. " + result + ".";
        LoggingProxy.log(LoggingProxy.Source.OBJECT, message);
        return message;
    }
}
