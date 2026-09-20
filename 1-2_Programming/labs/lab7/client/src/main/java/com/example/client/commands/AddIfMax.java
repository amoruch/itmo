package com.example.client.commands;

import com.example.client.ClientRunner;
import com.example.common.Protocol.CommandType;
import com.example.common.Protocol.Marine;
import com.example.common.Protocol.Payload;

public final class AddIfMax extends AbstractRemoteCommand {

    public AddIfMax() {
        super("add_if_max", "добавить элемент, если он больше максимального", CommandType.ADD_IF_MAX);
    }

    @Override
    Payload createPayload(String argument, ClientRunner runner) throws Exception {
        return new Marine(runner.ask().marine());
    }
}
