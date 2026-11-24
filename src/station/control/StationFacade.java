package station.control;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import station.core.CommandCenter;
import station.core.StationMediator;
import station.logging.LoggingProxy;

public class StationFacade {
    private final CommandHistory history;
    private final StationMediator mediator;
    private final Handler first;
    private boolean running = true;

    public StationFacade(CommandCenter center) {
        mediator = center.getMediator();
        history = new CommandHistory(center);
        first = Handler.createChain(this);
    }

    public boolean isRunning() {
        return running;
    }

    public String exit() {
        running = false;
        return "Работа завершена";
    }

    public String undo() {
        return history.undo();
    }

    public String help() {
        return "Номера модулей начинаются с 0. Введите status, чтобы увидеть список.\n" + first.help();
    }

    public String executeCommand(Command command) {
        return history.execute(command);
    }

    public String processInput(String input) {
        if (input.isBlank()) return "";
        LoggingProxy.log(LoggingProxy.Source.USER, "> " + input);
        String result;
        try {
            result = first.handle(tokenize(input.strip()));
        } catch (IllegalArgumentException | IllegalStateException e) {
            result = "Ошибка: " + e.getMessage();
        }
        LoggingProxy.log(LoggingProxy.Source.USER, result);
        return result;
    }

    private static final Pattern TOKEN = Pattern.compile("\\G\\s*(?:\"([^\"]*)\"|([^\\s\"]+))(?=\\s|$)");

    private List<String> tokenize(String input) {
        var tokens = new ArrayList<String>();
        var matcher = TOKEN.matcher(input);
        int end = 0;
        while (matcher.find()) {
            tokens.add(matcher.group(1) == null ? matcher.group(2) : matcher.group(1));
            end = matcher.end();
        }
        if (end != input.length()) throw new IllegalArgumentException("Проверьте кавычки и пробелы");
        return tokens;
    }

    public String activateModule(int index) {
        return executeCommand(new ActivateModuleCommand(mediator, index));
    }

    public String deactivateModule(int index) {
        return executeCommand(new DeactivateModuleCommand(mediator, index));
    }

    public String moveModule(int from, int to) {
        return executeCommand(new MoveModuleCommand(mediator, from, to));
    }

    public String repairModule(int index) {
        return executeCommand(new RepairModuleCommand(mediator, index));
    }

    public String damageModule(int index) {
        return executeCommand(new DamageModuleCommand(mediator, index));
    }

    public String distributeEnergy(int index, int percent) {
        return executeCommand(new DistributeEnergyCommand(mediator, index, percent));
    }

    public String startResearch(int index) {
        return executeCommand(new StartResearchCommand(mediator, index));
    }

    public String buildModule(String type, String name) {
        return executeCommand(new BuildModuleCommand(mediator, type, name));
    }

    public String step() {
        return executeCommand(new StepCommand(mediator));
    }

    public String changeEnvironment(String key) {
        return executeCommand(new ChangeEnvironmentCommand(mediator, key));
    }

    public String triggerEvent(String type, Integer index) {
        return executeCommand(new EventCommand(mediator, type, index));
    }

    public String getStatus(Integer index) {
        return executeCommand(new StatusCommand(mediator, index));
    }
}
