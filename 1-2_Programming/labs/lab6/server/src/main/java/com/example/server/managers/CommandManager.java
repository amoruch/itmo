package com.example.server.managers;

import com.example.common.Protocol.CommandType;
import com.example.server.commands.Command;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Registers server commands and stores the last executed client commands.
 */
public final class CommandManager {

    private static final int HISTORY_LIMIT = 12;

    private final Map<String, Command> commands = new LinkedHashMap<>();
    private final Map<CommandType, Command> remoteCommands = new EnumMap<>(CommandType.class);
    private final Deque<String> history = new ArrayDeque<>();

    public void register(String name, Command command) {
        commands.put(name, command);
        if (command.type() != null) {
            remoteCommands.put(command.type(), command);
        }
    }

    public Command getCommand(String name) {
        return commands.get(name);
    }

    public Command getCommand(CommandType type) {
        return remoteCommands.get(type);
    }

    public Map<String, Command> getCommands() {
        return commands;
    }

    public Deque<String> getCommandHistory() {
        return history;
    }

    public void addToHistory(String command) {
        history.addLast(command);
        while (history.size() > HISTORY_LIMIT) {
            history.removeFirst();
        }
    }
}
