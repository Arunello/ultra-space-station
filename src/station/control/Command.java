package station.control;

import station.core.StationMediator;

public abstract class Command {
    protected final StationMediator mediator;

    protected Command(StationMediator mediator) {
        this.mediator = mediator;
    }

    public abstract String execute();

    public boolean changesStation() {
        return true;
    }
}

final class ActivateModuleCommand extends Command {
    private final int index;

    ActivateModuleCommand(StationMediator mediator, int index) {
        super(mediator);
        this.index = index;
    }

    @Override
    public String execute() {
        return mediator.activate(index);
    }
}

final class DeactivateModuleCommand extends Command {
    private final int index;

    DeactivateModuleCommand(StationMediator mediator, int index) {
        super(mediator);
        this.index = index;
    }

    @Override
    public String execute() {
        return mediator.deactivate(index);
    }
}

final class MoveModuleCommand extends Command {
    private final int from;
    private final int to;

    MoveModuleCommand(StationMediator mediator, int from, int to) {
        super(mediator);
        this.from = from;
        this.to = to;
    }

    @Override
    public String execute() {
        return mediator.move(from, to);
    }
}

final class RepairModuleCommand extends Command {
    private final int index;

    RepairModuleCommand(StationMediator mediator, int index) {
        super(mediator);
        this.index = index;
    }

    @Override
    public String execute() {
        return mediator.repair(index);
    }
}

final class DamageModuleCommand extends Command {
    private final int index;

    DamageModuleCommand(StationMediator mediator, int index) {
        super(mediator);
        this.index = index;
    }

    @Override
    public String execute() {
        return mediator.damage(index);
    }
}

final class DistributeEnergyCommand extends Command {
    private final int index;
    private final int percent;

    DistributeEnergyCommand(StationMediator mediator, int index, int percent) {
        super(mediator);
        this.index = index;
        this.percent = percent;
    }

    @Override
    public String execute() {
        return mediator.distribute(index, percent);
    }
}

final class StartResearchCommand extends Command {
    private final int index;

    StartResearchCommand(StationMediator mediator, int index) {
        super(mediator);
        this.index = index;
    }

    @Override
    public String execute() {
        return mediator.research(index);
    }
}

final class BuildModuleCommand extends Command {
    private final String type;
    private final String name;

    BuildModuleCommand(StationMediator mediator, String type, String name) {
        super(mediator);
        this.type = type;
        this.name = name;
    }

    @Override
    public String execute() {
        return mediator.build(type, name);
    }
}

final class StatusCommand extends Command {
    private final Integer index;

    StatusCommand(StationMediator mediator, Integer index) {
        super(mediator);
        this.index = index;
    }

    @Override
    public String execute() {
        return mediator.status(index);
    }

    @Override
    public boolean changesStation() {
        return false;
    }
}

final class ChangeEnvironmentCommand extends Command {
    private final String key;

    ChangeEnvironmentCommand(StationMediator mediator, String key) {
        super(mediator);
        this.key = key;
    }

    @Override
    public String execute() {
        return mediator.changeEnvironment(key);
    }
}

final class EventCommand extends Command {
    private final String type;
    private final Integer index;

    EventCommand(StationMediator mediator, String type, Integer index) {
        super(mediator);
        this.type = type;
        this.index = index;
    }

    @Override
    public String execute() {
        return mediator.triggerEvent(type, index);
    }
}

final class StepCommand extends Command {
    StepCommand(StationMediator mediator) {
        super(mediator);
    }

    @Override
    public String execute() {
        return mediator.step();
    }
}
