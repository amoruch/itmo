package com.example.client.commands;

import com.example.client.ClientRunner;

/**
 * Displays commands available from the client console without contacting the
 * server.
 */
public final class Help extends AbstractCommand {

    public Help() {
        super("help", "вывести справку по доступным командам");
    }

    @Override
    public ExecutionResult execute(String argument, ClientRunner runner) {
        String help = runner.commands().entrySet().stream()
                .map(entry -> entry.getKey() + " : " + entry.getValue().getDescription())
                .collect(java.util.stream.Collectors.joining(System.lineSeparator()));
        return ExecutionResult.success(help);
    }
}
