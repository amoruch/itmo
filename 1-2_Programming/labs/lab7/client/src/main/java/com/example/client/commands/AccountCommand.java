package com.example.client.commands;

import com.example.client.ClientRunner;
import com.example.common.Protocol.CommandType;
import com.example.common.Protocol.Credentials;
import com.example.common.Protocol.Empty;
import com.example.common.Protocol.Request;

/** Shared input and request logic for registration and authorization. */
abstract class AccountCommand extends AbstractCommand {

    private final CommandType type;

    AccountCommand(String name, String description, CommandType type) {
        super(name, description);
        this.type = type;
    }

    @Override
    public final ExecutionResult execute(String argument, ClientRunner runner) {
        try {
            String[] values = argument.trim().split("\\s+", 2);
            String login;
            String password;
            if (argument.isBlank()) {
                login = runner.ask().line("login: ");
                password = runner.ask().line("password: ");
            } else if (values.length == 2) {
                login = values[0];
                password = values[1];
            } else {
                return ExecutionResult.failure("Usage: " + getName() + " login password");
            }
            Credentials credentials = new Credentials(login, password);
            ExecutionResult result = runner.send(new Request(type, new Empty(), credentials));
            if (result.status() == ExecutionStatus.SUCCESS) {
                runner.authorize(credentials);
            }
            return result;
        } catch (IllegalArgumentException exception) {
            return ExecutionResult.failure("Логин и пароль не должны быть пустыми.");
        } catch (Exception exception) {
            return ExecutionResult.failure("Ошибка авторизации: " + exception.getMessage());
        }
    }
}
