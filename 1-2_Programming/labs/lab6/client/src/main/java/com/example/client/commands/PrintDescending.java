package com.example.client.commands;

import com.example.common.Protocol.CommandType;

public final class PrintDescending extends AbstractEmptyCommand {

    public PrintDescending() {
        super("print_descending", "вывести элементы в убывающем порядке", CommandType.PRINT_DESCENDING);
    }
}
