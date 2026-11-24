package station.core;

import station.environment.*;
import station.events.ModuleEvent;
import station.events.Observer;
import station.factory.*;
import station.logging.LoggingProxy;
import station.modules.*;
import station.modules.Module;
import station.objects.*;

public class StationMediator implements Observer {

    private final CommandCenter center;

    StationMediator(CommandCenter center) {
        this.center = center;
    }

    @Override
    public void onModuleEvent(ModuleEvent event) {
        center.onModuleEvent(event);
    }

    public void transferEnergy(Module source, Module target, int amount) {
        if (source.getMediator() != this || target.getMediator() != this) {
            throw new IllegalArgumentException(
                "Модули должны находиться на одной станции"
            );
        }
        source.consumeCharge(amount);
        target.chargeEnergy(amount);
        LoggingProxy.log(
            LoggingProxy.Source.MODULE,
            source.getName() +
                " передал " +
                amount +
                " энергии модулю " +
                target.getName()
        );
    }

    public boolean tryConsumeEnergy(Module consumer, int cost) {
        if (consumer.getMediator() != this || cost < 0) {
            throw new IllegalArgumentException("Некорректный запрос энергии");
        }
        if (cost == 0) return true;

        var reserves = center
            .getModules()
            .stream()
            .filter(
                module -> module != consumer && module instanceof EnergyModule
            )
            .filter(module -> "включён".equals(module.getState()))
            .toList();
        long available = (long) center.getEnergy() + consumer.getStoredEnergy();
        for (Module reserve : reserves) {
            available += reserve.getStoredEnergy();
        }
        if (available < cost) return false;

        int ownCharge = Math.min(cost, consumer.getStoredEnergy());
        consumer.consumeCharge(ownCharge);
        int remaining = cost - ownCharge;
        for (Module reserve : reserves) {
            if (remaining == 0) break;
            int amount = Math.min(remaining, reserve.getStoredEnergy());
            if (amount > 0) {
                transferEnergy(reserve, consumer, amount);
                consumer.consumeCharge(amount);
                remaining -= amount;
            }
        }
        center.spendEnergy(remaining);
        consumer.reportEnergyConsumed(cost);
        return true;
    }

    public String activate(int index) {
        Module module = center.getModuleByIndex(index);
        if ("включён".equals(module.getState())) return (
            "Модуль «" + module.getName() + "» уже включён."
        );
        if (module instanceof ResearchModule lab && lab.isActivationBlocked()) {
            return "Лабораторию нельзя включить в поле астероидов.";
        }
        module.activate();
        return module.getName() + " — " + module.getState();
    }

    public String deactivate(int index) {
        Module module = center.getModuleByIndex(index);
        module.deactivate();
        return module.getName() + " — " + module.getState();
    }

    public String move(int from, int to) {
        Module module = center.getModuleByIndex(from);
        center.getModuleByIndex(to);
        center.teleportModule(module, to);
        return module.getName() + " на позиции " + to;
    }

    public String repair(int index) {
        Module module = center.getModuleByIndex(index);
        int missing =
            module.getTypeInfo().getMaxHealth() -
            module.getDurability().getValue();
        if (missing == 0) return "Ремонт не требуется.";
        center.spendEnergy(missing);
        module.getDurability().repair(missing);
        return "Восстановлено прочности: " + missing;
    }

    public String damage(int index) {
        Module module = center.getModuleByIndex(index);
        module.subtractDurability(new Damage(10, "PHYSICAL"));
        return (
            module.getName() +
            ", прочность: " +
            module.getDurability().getValue()
        );
    }

    public String distribute(int index, int percent) {
        if (percent < 0 || percent > 100) throw new IllegalArgumentException(
            "Процент от 0 до 100"
        );
        int amount = (int) (((long) center.getEnergy() * percent) / 100);
        center.chargeModule(center.getModuleByIndex(index), amount);
        return "Передано энергии: " + amount;
    }

    public String research(int index) {
        if (!(center.getModuleByIndex(index) instanceof ResearchModule lab)) {
            throw new IllegalArgumentException(
                "Выберите исследовательский модуль"
            );
        }
        int points = lab.startResearch();
        return "%s «%s»: %s. Научные баллы: %d.".formatted(
            typeName(lab),
            lab.getName(),
            lab.describeResult(points),
            points
        );
    }

    public String changeEnvironment(String key) {
        Environment next = switch (key) {
            case "orbit" -> new EarthOrbit();
            case "deep" -> new DeepSpace();
            case "asteroids" -> new AsteroidField();
            default -> throw new IllegalArgumentException(
                "Допустимые окружения: orbit, deep, asteroids."
            );
        };
        var current = center.getCurrentEnvironment();
        if (current != null && current.getName().equals(next.getName())) {
            return "Станция уже здесь: " + next.getName() + ".";
        }
        var modules = center.getModules();
        center.setEnvironment(next);
        var report = new StringBuilder(
            "Новое окружение: " + next.getName() + "."
        );
        appendEnvironmentReport(report, modules);
        return report.toString();
    }

    private void appendEnvironmentReport(
        StringBuilder report,
        java.util.List<Module> modules
    ) {
        if (center.getCurrentEnvironment() == null) return;
        for (Module module : modules) {
            int damage = center
                .getCurrentEnvironment()
                .getCachedDamage(module.getId());
            if (damage > 0) {
                report
                    .append("\n«")
                    .append(module.getName())
                    .append("»: урон ")
                    .append(damage);
                if (module.getMediator() == null) report.append(
                    ", модуль уничтожен"
                );
                report.append('.');
            }
            if (
                module.getMediator() != null &&
                module instanceof ResearchModule lab &&
                lab.isActivationBlocked()
            ) {
                report
                    .append("\n«")
                    .append(lab.getName())
                    .append("»: исследования заблокированы.");
            }
        }
    }

    public String step() {
        var modules = center.getModules();
        center.advanceEnvironment();
        var report = new StringBuilder(
            "Смена " + center.getCompletedShifts() + " завершена."
        );
        appendEnvironmentReport(report, modules);
        var survivingModules = center.getModules();
        int energyBefore = center.getEnergy();
        for (Module module : survivingModules) {
            if (
                module instanceof EnergyModule generator &&
                "включён".equals(generator.getState())
            ) {
                int amount = Math.min(
                    generator.getOutputPerShift(),
                    Integer.MAX_VALUE - center.getEnergy()
                );
                if (amount > 0) center.addEnergy(amount);
            }
        }
        int generated = center.getEnergy() - energyBefore;

        int oxygenBefore = center.getOxygen();
        long consumed = 0;
        for (Module module : survivingModules) {
            if (!"включён".equals(module.getState())) continue;
            int oxygenCost =
                module instanceof ResidentialModule
                    ? module.getCapacity().getValue()
                    : 0;
            int energyCost = module.getShiftEnergyConsumption();
            if (oxygenCost > center.getOxygen()) {
                turnOffForShortage(report, module, "кислорода");
            } else if (!tryConsumeEnergy(module, energyCost)) {
                turnOffForShortage(report, module, "энергии");
            } else {
                center.spendOxygen(oxygenCost);
                consumed += energyCost;
            }
        }
        return report
            .append("\nВыработано энергии: ")
            .append(generated)
            .append(". Израсходовано энергии: ")
            .append(consumed)
            .append(". Общий запас: ")
            .append(center.getEnergy())
            .append(".\nИзрасходовано кислорода: ")
            .append(oxygenBefore - center.getOxygen())
            .append('.')
            .toString();
    }

    private void turnOffForShortage(
        StringBuilder report,
        Module module,
        String resource
    ) {
        module.deactivate();
        report
            .append("\n«")
            .append(module.getName())
            .append("» отключён: недостаточно ")
            .append(resource)
            .append('.');
    }

    public String triggerEvent(String type, Integer index) {
        if ("cargo".equals(type)) {
            if (index != null) throw new IllegalArgumentException(
                "Для груза введите event cargo."
            );
            return new CargoContainer().deliver(center);
        }
        ExternalObject object = switch (type) {
            case "solar" -> new SolarStorm();
            case "meteor" -> new MeteorStorm();
            case "anomaly" -> new SpaceAnomaly();
            default -> throw new IllegalArgumentException(
                "Допустимые события: solar, meteor, anomaly, cargo."
            );
        };
        if (index == null) throw new IllegalArgumentException(
            "Укажите номер модуля: event " + type + " <номер>."
        );
        return center.interactWith(object, center.getModuleByIndex(index));
    }

    public String build(String type, String name) {
        ModuleFactory factory = switch (type) {
            case "crew", "solar", "biology" -> new StandardModuleFactory();
            case "medical", "reactor", "physics" -> new AdvancedModuleFactory();
            default -> throw new IllegalArgumentException(
                "Неизвестный тип «" +
                    type +
                    "». Доступны crew, medical, solar, reactor, biology и physics."
            );
        };
        Module.Category category = switch (type) {
            case "crew", "medical" -> Module.Category.RESIDENTIAL;
            case "solar", "reactor" -> Module.Category.ENERGY;
            default -> Module.Category.RESEARCH;
        };
        int capacity = switch (category) {
            case RESIDENTIAL -> 32;
            case ENERGY -> 0;
            case RESEARCH -> type.equals("physics") ? 4 : 2;
        };
        Module module = center.createModule(
            factory,
            category,
            new Capacity(capacity),
            name
        );
        return "Строительство завершено. %s «%s», номер %d. Потрачено %d энергии, осталось %d.".formatted(
            typeName(module),
            module.getName(),
            center.getModuleCount() - 1,
            module.getTypeInfo().getCost(),
            center.getEnergy()
        );
    }

    private String typeName(Module module) {
        return switch (module.getType()) {
            case CREW_CABIN -> "Жилой отсек";
            case MEDICAL_CABIN -> "Медотсек";
            case SOLAR_MODULE -> "Солнечная панель";
            case REACTOR_MODULE -> "Реактор";
            case BIOLOGY_LAB -> "Биолаборатория";
            case PHYSICS_LAB -> "Физическая лаборатория";
        };
    }

    public String status(Integer index) {
        if (index != null) {
            Module module = center.getModuleByIndex(index);
            String details =
                "ID: %d · Рабочая эффективность: %d%% (собственная %d%%) · Заряд: %d".formatted(
                    module.getId(),
                    module.getWorkingEfficiency(),
                    module.getEfficiency(),
                    module.getStoredEnergy()
                );
            String energy;
            if (module instanceof EnergyModule generator) {
                energy = "Выработка за смену: " + generator.getOutputPerShift();
            } else {
                energy =
                    "Расход энергии за смену: " +
                    module.getShiftEnergyConsumption();
                if (module instanceof ResearchModule) {
                    energy +=
                        " · Стоимость эксперимента: " +
                        module.getEffectiveEnergyConsumption();
                }
                if (module instanceof ResidentialModule) {
                    energy +=
                        " · Расход кислорода за смену: " +
                        module.getCapacity().getValue();
                }
            }
            return summary(index, module) + "\n" + details + "\n" + energy;
        }
        var environment = center.getCurrentEnvironment();
        StringBuilder result = new StringBuilder(
            "%s · Энергия %d · Кислород %d · Безопасность %d (защита %d%%) · Завершено смен: %d\nМодули: %d из %d".formatted(
                environment == null
                    ? "Окружение не задано"
                    : environment.getName(),
                center.getEnergy(),
                center.getOxygen(),
                center.getSafetyLevel(),
                center.getProtectionPercent(),
                center.getCompletedShifts(),
                center.getModuleCount(),
                center.getMaxModules()
            )
        );
        for (int i = 0; i < center.getModuleCount(); i++) {
            result.append('\n').append(summary(i, center.getModuleByIndex(i)));
        }
        return result.toString();
    }

    private String summary(int index, Module m) {
        return "%d  %s «%s» · состояние: %s · прочность %d/%d".formatted(
            index,
            typeName(m),
            m.getName(),
            m.getState(),
            m.getDurability().getValue(),
            m.getTypeInfo().getMaxHealth()
        );
    }
}
