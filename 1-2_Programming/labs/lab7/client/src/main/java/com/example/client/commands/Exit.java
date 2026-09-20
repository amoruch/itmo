package com.example.client.commands;

import com.example.client.ClientRunner;

/**
 * Stops the current interactive input or script loop.
 */
public final class Exit extends AbstractCommand {

    public Exit() {
        super("exit", "завершить работу клиента");
    }

    @Override
    public ExecutionResult execute(String argument, ClientRunner runner) {
        if (!argument.isBlank()) {
            return ExecutionResult.failure("Usage: exit");
        }
        return ExecutionResult.exit("Завершение работы клиента.");
    }
}
