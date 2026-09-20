package com.example.client.commands;

import com.example.common.Protocol.CommandType;

public final class History extends AbstractEmptyCommand {

    public History() {
        super("history", "вывести историю серверных команд", CommandType.HISTORY);
    }
}
