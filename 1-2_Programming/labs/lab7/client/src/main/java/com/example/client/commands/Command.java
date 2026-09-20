package com.example.client.commands;

import com.example.client.ClientRunner;

/**
 * One command available from the client console.
 */
public interface Command {

    String getName();

    String getDescription();

    ExecutionResult execute(String argument, ClientRunner runner);
}
