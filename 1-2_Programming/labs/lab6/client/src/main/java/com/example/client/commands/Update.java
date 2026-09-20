package com.example.client.commands;

import com.example.client.managers.CommandManager;
import com.example.common.Protocol.CommandType;
import com.example.common.Protocol.Payload;

public final class Update extends AbstractRemoteCommand {

    public Update() {
        super("update", "обновить элемент по id", CommandType.UPDATE);
    }

    @Override
    Payload createPayload(String argument, CommandManager commandManager) throws Exception {
        return new com.example.common.Protocol.Update(readId(argument, commandManager.ask()), commandManager.ask().marine());
    }
}
