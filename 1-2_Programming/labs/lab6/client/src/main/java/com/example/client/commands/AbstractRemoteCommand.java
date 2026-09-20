package com.example.client.commands;

import com.example.client.managers.CommandManager;
import com.example.common.Protocol.CommandType;
import com.example.common.Protocol.Payload;
import com.example.common.Protocol.Request;

abstract class AbstractRemoteCommand extends AbstractCommand {

    private final CommandType type;

    AbstractRemoteCommand(String name, String description, CommandType type) {
        super(name, description);
        this.type = type;
    }

    @Override
    public final void execute(String argument, CommandManager commandManager) {
        try {
            commandManager.send(new Request(type, createPayload(argument, commandManager)));
        } catch (Exception exception) {
            commandManager.printError("Command error: " + exception.getMessage());
        }
    }

    abstract Payload createPayload(String argument, CommandManager commandManager) throws Exception;
}
