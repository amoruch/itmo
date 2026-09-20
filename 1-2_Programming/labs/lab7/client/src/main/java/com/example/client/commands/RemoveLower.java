package com.example.client.commands;

import com.example.client.ClientRunner;
import com.example.common.Protocol.CommandType;
import com.example.common.Protocol.Marine;
import com.example.common.Protocol.Payload;

public final class RemoveLower extends AbstractRemoteCommand {

    public RemoveLower() {
        super("remove_lower", "удалить элементы, меньшие заданного", CommandType.REMOVE_LOWER);
    }

    @Override
    Payload createPayload(String argument, ClientRunner runner) throws Exception {
        return new Marine(runner.ask().marine());
    }
}
