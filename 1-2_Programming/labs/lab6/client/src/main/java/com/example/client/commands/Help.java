package com.example.client.commands;

import com.example.client.managers.CommandManager;

/**
 * Displays commands available from the client console without contacting the
 * server.
 */
public final class Help extends AbstractCommand {

    public Help() {
        super("help", "вывести справку по доступным командам");
    }

    @Override
    public void execute(String argument, CommandManager commandManager) {
        commandManager.getCommands().forEach((name, command)
                -> commandManager.println(name + " : " + command.getDescription()));
    }
}
