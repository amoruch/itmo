package utility;

import models.SpaceMarine;

public class Request {

    private String command;
    private String[] arguments;
    private SpaceMarine spaceMarine;

    public Request(String command, String[] arguments) {
        this.command = command;
        this.arguments = arguments;
        this.spaceMarine = null;
    }

    public Request(String command, String[] arguments, SpaceMarine spaceMarine) {
        this(command, arguments);
        this.spaceMarine = spaceMarine;
    }

    public void setSpaceMarine(SpaceMarine spaceMarine) {
        this.spaceMarine = spaceMarine;
    }

    public SpaceMarine getSpaceMarine() {
        return spaceMarine;
    }

    public String[] getArguments() {
        return arguments;
    }

    public String getCommand() {
        return command;
    }
}
