package com.example.client.commands;

import com.example.client.managers.CommandManager;
import com.example.common.Protocol.CommandType;
import com.example.common.Protocol.Marine;
import com.example.common.Protocol.Payload;

public final class Add extends AbstractRemoteCommand {

    public Add() {
        super("add", "добавить новый элемент в коллекцию", CommandType.ADD);
    }

    @Override
    Payload createPayload(String argument, CommandManager commandManager) throws Exception {
        return new Marine(commandManager.ask().marine());
    }
}
