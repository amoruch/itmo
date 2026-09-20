package com.example.client.commands;

import com.example.client.managers.CommandManager;

/**
 * One command available from the client console.
 */
public interface Command {

    String getName();

    String getDescription();

    void execute(String argument, CommandManager commandManager);
}
