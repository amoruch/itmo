package com.example.client.commands;

import com.example.common.Protocol.CommandType;

public final class Show extends AbstractEmptyCommand {

    public Show() {
        super("show", "вывести все элементы коллекции", CommandType.SHOW);
    }
}
