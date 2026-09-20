package com.example.client.commands;

import com.example.client.managers.CommandManager;

/**
 * Stops the current interactive input or script loop.
 */
public final class Exit extends AbstractCommand {

    public Exit() {
        super("exit", "завершить работу клиента");
    }

    @Override
    public void execute(String argument, CommandManager commandManager) {
        commandManager.stopCurrentLoop();
    }
}
