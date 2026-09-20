package com.example.client.commands;

import com.example.client.ClientRunner;
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
    public final ExecutionResult execute(String argument, ClientRunner runner) {
        try {
            return runner.send(new Request(type, createPayload(argument, runner)));
        } catch (Exception exception) {
            return ExecutionResult.failure("Command error: " + exception.getMessage());
        }
    }

    abstract Payload createPayload(String argument, ClientRunner runner) throws Exception;
}
