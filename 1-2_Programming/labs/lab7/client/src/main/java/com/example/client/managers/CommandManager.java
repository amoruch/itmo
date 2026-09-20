package com.example.client.managers;

import com.example.client.commands.*;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Registers and locates commands available from the client console.
 */
public final class CommandManager {

    private final Map<String, Command> commands = new LinkedHashMap<>();

    public CommandManager() {
        registerCommands();
    }

    private void registerCommands() {
        register(new Register());
        register(new Login());
        register(new Help());
        register(new Info());
        register(new Show());
        register(new Add());
        register(new Update());
        register(new RemoveById());
        register(new Clear());
        register(new RemoveFirst());
        register(new AddIfMax());
        register(new RemoveLower());
        register(new MaxByHealth());
        register(new GroupCountingByCreationDate());
        register(new PrintDescending());
        register(new History());
        register(new ExecuteScript());
        register(new Exit());
    }

    private void register(Command command) {
        commands.put(command.getName(), command);
    }

    public void register(String name, Command command) {
        commands.put(name, command);
    }

    public Map<String, Command> getCommands() {
        return Collections.unmodifiableMap(new LinkedHashMap<>(commands));
    }
}
