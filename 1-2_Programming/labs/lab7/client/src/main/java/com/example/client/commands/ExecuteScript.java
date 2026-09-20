package com.example.client.commands;

import com.example.client.ClientRunner;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

/**
 * Runs a local command script.
 */
public final class ExecuteScript extends AbstractCommand {

    private final Set<Path> activeScripts = new HashSet<>();

    public ExecuteScript() {
        super("execute_script", "выполнить команды из файла");
    }

    @Override
    public ExecutionResult execute(String argument, ClientRunner runner) {
        if (argument.isBlank()) {
            return ExecutionResult.failure("Usage: execute_script file_name");
        }

        Path script;
        try {
            script = Path.of(argument).toAbsolutePath().normalize();
        } catch (InvalidPathException exception) {
            return ExecutionResult.failure("Invalid script path: " + exception.getMessage());
        }
        if (!activeScripts.add(script)) {
            return ExecutionResult.failure("Recursive script call blocked: " + script);
        }

        try {
            return runner.runScript(script);
        } finally {
            activeScripts.remove(script);
        }
    }
}
