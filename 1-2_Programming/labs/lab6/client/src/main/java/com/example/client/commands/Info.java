package com.example.client.commands;

import com.example.common.Protocol.CommandType;

public final class Info extends AbstractEmptyCommand {

    public Info() {
        super("info", "вывести информацию о коллекции", CommandType.INFO);
    }
}
