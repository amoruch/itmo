package com.example.client.commands;

import com.example.client.managers.CommandManager;
import com.example.common.Protocol.CommandType;
import com.example.common.Protocol.Empty;
import com.example.common.Protocol.Payload;

abstract class AbstractEmptyCommand extends AbstractRemoteCommand {

    AbstractEmptyCommand(String name, String description, CommandType type) {
        super(name, description, type);
    }

    @Override
    Payload createPayload(String argument, CommandManager commandManager) {
        return new Empty();
    }
}
