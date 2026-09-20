package com.example.client.commands;

import com.example.common.Protocol.CommandType;

public final class MaxByHealth extends AbstractEmptyCommand {

    public MaxByHealth() {
        super("max_by_health", "вывести элемент с максимальным health", CommandType.MAX_BY_HEALTH);
    }
}
