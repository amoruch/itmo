package com.example.client.commands;

import com.example.common.Protocol.CommandType;

public final class RemoveFirst extends AbstractEmptyCommand {

    public RemoveFirst() {
        super("remove_first", "удалить первый элемент коллекции", CommandType.REMOVE_FIRST);
    }
}
