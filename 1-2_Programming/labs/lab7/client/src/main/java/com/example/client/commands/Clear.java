package com.example.client.commands;

import com.example.common.Protocol.CommandType;

public final class Clear extends AbstractEmptyCommand {

    public Clear() {
        super("clear", "очистить коллекцию", CommandType.CLEAR);
    }
}
