package station.control;

import java.util.ArrayDeque;
import java.util.Deque;
import station.core.CommandCenter;

public class CommandHistory {
    private record Entry(Command command, CommandCenter.Snapshot before) {}

    private final CommandCenter center;
    private final Deque<Entry> entries = new ArrayDeque<>();

    public CommandHistory(CommandCenter center) {
        this.center = center;
    }

    public String execute(Command command) {
        if (!command.changesStation()) return command.execute();
        var before = center.snapshot();
        String result = command.execute();
        if (!before.equals(center.snapshot())) entries.push(new Entry(command, before));
        return result;
    }

    public Command getLastCommand() {
        return entries.isEmpty() ? null : entries.peek().command();
    }

    public String undo() {
        if (entries.isEmpty()) return "История пуста";
        center.restore(entries.pop().before());
        return "Последняя команда отменена";
    }
}
