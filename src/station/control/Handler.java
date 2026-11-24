package station.control;

import java.util.List;
import java.util.function.Function;

public class Handler {
    private final String keyword;
    private final String usage;
    private final String description;
    private final int minArgs;
    private final int maxArgs;
    private final Function<List<String>, String> action;
    private Handler next;

    private Handler(String usage, String description, int minArgs, int maxArgs, Function<List<String>, String> action) {
        this.keyword = usage.split(" ")[0];
        this.usage = usage;
        this.description = description;
        this.minArgs = minArgs;
        this.maxArgs = maxArgs;
        this.action = action;
    }

    private Handler setNext(Handler handler) {
        next = handler;
        return handler;
    }

    public String handle(List<String> words) {
        if (!keyword.equals(words.get(0))) {
            if (next != null) return next.handle(words);
            throw new IllegalArgumentException("Неизвестная команда. Введите help");
        }
        int count = words.size() - 1;
        if (count < minArgs || count > maxArgs) throw syntaxError();
        try {
            return action.apply(words.subList(1, words.size()));
        } catch (NumberFormatException e) {
            throw syntaxError();
        }
    }

    private IllegalArgumentException syntaxError() {
        return new IllegalArgumentException(usage + " — " + description);
    }

    public String help() {
        String line = usage + " — " + description;
        return next == null ? line : line + "\n" + next.help();
    }

    static Handler createChain(StationFacade f) {
        Handler first = new Handler("activate <номер>", "Включить модуль.", 1, 1, a -> f.activateModule(number(a, 0)));
        first.setNext(new Handler("deactivate <номер>", "Выключить модуль.", 1, 1, a -> f.deactivateModule(number(a, 0))))
            .setNext(new Handler("move <номер> <новая позиция>", "Переставить модуль в списке. Укажите два номера.", 2, 2, a -> f.moveModule(number(a, 0), number(a, 1))))
            .setNext(new Handler("repair <номер>", "Потратить энергию станции на восстановление прочности.", 1, 1, a -> f.repairModule(number(a, 0))))
            .setNext(new Handler("damage <номер>", "Нанести 10 единиц урона.", 1, 1, a -> f.damageModule(number(a, 0))))
            .setNext(new Handler("energy <номер> <процент>", "Передать модулю указанный процент текущего запаса энергии станции (0–100).", 2, 2, a -> f.distributeEnergy(number(a, 0), number(a, 1))))
            .setNext(new Handler("research <номер>", "Выполнить исследование в лаборатории.", 1, 1, a -> f.startResearch(number(a, 0))))
            .setNext(new Handler("build <тип> \"имя\"", "построить модуль. Типы: crew, medical, solar, reactor, biology, physics.", 2, 2, a -> f.buildModule(a.get(0), a.get(1))))
            .setNext(new Handler("step", "Провести смену и показать выработку энергии и расход ресурсов.", 0, 0, a -> f.step()))
            .setNext(new Handler("env <orbit|deep|asteroids>", "Сменить окружение станции.", 1, 1,
                a -> f.changeEnvironment(a.get(0))))
            .setNext(new Handler("event <тип> [номер]", "solar, meteor, anomaly — воздействие на модуль; cargo — груз для станции.", 1, 2,
                a -> f.triggerEvent(a.get(0), a.size() == 2 ? number(a, 1) : null)))
            .setNext(new Handler("status [номер]", "Показать станцию и список модулей. С номером — подробности о модуле.", 0, 1, a -> f.getStatus(a.isEmpty() ? null : number(a, 0))))
            .setNext(new Handler("help", "Показать команды.", 0, 0, a -> f.help()))
            .setNext(new Handler("undo", "Отменить последнее изменение станции.", 0, 0, a -> f.undo()))
            .setNext(new Handler("exit", "Завершить работу.", 0, 0, a -> f.exit()));
        return first;
    }

    private static int number(List<String> arguments, int index) {
        return Integer.parseInt(arguments.get(index));
    }
}
