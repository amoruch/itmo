package com.example.server.managers;

import com.example.common.Protocol.Request;
import com.example.common.Protocol.Response;
import com.example.common.Protocol.Text;
import com.example.common.Protocol.CommandType;
import com.example.server.commands.Command;
import com.example.server.utility.AuthenticatedUser;
import com.example.server.utility.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Locates and executes commands received from UDP clients.
 */
public final class RequestHandler {

    private static final Logger LOGGER = LogManager.getLogger(RequestHandler.class);
    private final CommandManager commandManager;
    private final UserRepository userRepository;

    public RequestHandler(CommandManager commandManager, UserRepository userRepository) {
        this.commandManager = commandManager;
        this.userRepository = userRepository;
    }

    public Response handle(Request request) {
        if (request.command() == CommandType.REGISTER) {
            return register(request);
        }
        if (request.command() == CommandType.LOGIN) {
            return login(request);
        }
        if (request.credentials() == null) {
            return new Response(false, new Text(
                    "Требуется авторизация. Выполните login или register."));
        }

        AuthenticatedUser user;
        try {
            user = userRepository.authenticate(request.credentials()).orElse(null);
        } catch (Exception exception) {
            LOGGER.error("Could not authenticate user {}", request.credentials().login(), exception);
            return new Response(false, new Text("Ошибка авторизации: " + exception.getMessage()));
        }
        if (user == null) {
            return new Response(false, new Text("Неверный логин или пароль."));
        }

        Command command = commandManager.getCommand(request.command());
        if (command == null) {
            LOGGER.warn("Received unknown command {}", request.command());
            return new Response(false, new Text("Команда не найдена. Наберите help для справки."));
        }
        commandManager.addToHistory(command.getName());
        LOGGER.info("Executing command {}", command.getName());
        try {
            Response response = command.apply(request, user);
            LOGGER.info("Command {} finished with status {}", command.getName(), response.ok());
            return response;
        } catch (ClassCastException exception) {
            LOGGER.warn("Invalid payload for command {}", command.getName(), exception);
            return new Response(false, new Text(
                    "Неверные данные для команды " + command.getName() + "."));
        } catch (Exception exception) {
            LOGGER.error("Command {} failed", command.getName(), exception);
            return new Response(false, new Text(
                    "Ошибка выполнения команды: " + exception.getMessage()));
        }
    }

    private Response register(Request request) {
        if (request.credentials() == null) {
            return new Response(false, new Text("Для регистрации укажите логин и пароль."));
        }
        try {
            AuthenticatedUser user = userRepository.register(request.credentials()).orElse(null);
            return user == null
                    ? new Response(false, new Text("Пользователь с таким логином уже существует."))
                    : new Response(true, new Text("Регистрация успешна. Вы авторизованы как "
                            + user.login() + "."));
        } catch (Exception exception) {
            LOGGER.error("Could not register user {}", request.credentials().login(), exception);
            return new Response(false, new Text("Ошибка регистрации: " + exception.getMessage()));
        }
    }

    private Response login(Request request) {
        if (request.credentials() == null) {
            return new Response(false, new Text("Для входа укажите логин и пароль."));
        }
        try {
            AuthenticatedUser user = userRepository.authenticate(request.credentials()).orElse(null);
            return user == null
                    ? new Response(false, new Text("Неверный логин или пароль."))
                    : new Response(true, new Text("Авторизация успешна. Вы вошли как "
                            + user.login() + "."));
        } catch (Exception exception) {
            LOGGER.error("Could not authenticate user {}", request.credentials().login(), exception);
            return new Response(false, new Text("Ошибка авторизации: " + exception.getMessage()));
        }
    }
}
