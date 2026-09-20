package com.example.client.commands;

import com.example.client.ClientRunner;
import com.example.common.Protocol.CommandType;
import com.example.common.Protocol.Id;
import com.example.common.Protocol.Payload;

public final class RemoveById extends AbstractRemoteCommand {

    public RemoveById() {
        super("remove_by_id", "удалить элемент по id", CommandType.REMOVE_BY_ID);
    }

    @Override
    Payload createPayload(String argument, ClientRunner runner) throws Exception {
        return new Id(readId(argument, runner.ask()));
    }
}
