package commands;

import utility.*;

/**
 * Команда 'execute_script' - считывает и исполняет скрипт из указанного файла.
 * В скрипте содержатся команды в таком же виде, в котором их вводит
 * пользователь в интерактивном режиме.
 */
public class ExecuteScript extends Command {

    private final Console console;

    public ExecuteScript(Console console) {
        super("execute_script file_name", "считать и исполнить скрипт из указанного файла");
        this.console = console;
    }

    /**
     * Выполняет команду
     *
     * @return Успешность выполнения команды и сообщение об успешности.
     */
    @Override
    public ExecutionResponse apply(Request request) {
        if (request.getArguments().length != 1) {
            return new ExecutionResponse(false, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
        }

        console.println("* Выполнение скрипта " + request.getArguments()[0] + "...");

        return new ExecutionResponse("Cкрипт '" + request.getArguments()[0] + "' выполнен!");
    }
}
