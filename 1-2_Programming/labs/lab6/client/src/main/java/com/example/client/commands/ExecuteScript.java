package com.example.client.commands;

import com.example.client.managers.CommandManager;

/**
 * Runs a local command script.
 */
public final class ExecuteScript extends AbstractCommand {

    public ExecuteScript() {
        super("execute_script", "выполнить команды из файла");
    }

    @Override
    public void execute(String argument, CommandManager commandManager) {
        commandManager.executeScript(argument);
    }
}
