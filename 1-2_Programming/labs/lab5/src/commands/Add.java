package commands;

import managers.*;
import models.*;
import utility.*;

/**
 * Команда 'add' - добавляет новый элемент в коллекцию.
 */
public class Add extends Command {

    private final Console console;
    private final CollectionManager collectionManager;

    public Add(Console console, CollectionManager collectionManager) {
        super("add {element}", "добавить новый элемент в коллекцию");
        this.console = console;
        this.collectionManager = collectionManager;
        this.requireData = true;
    }

    /**
     * Выполняет команду
     *
     * @return Успешность выполнения команды и сообщение об успешности.
     */
    @Override
    public ExecutionResponse apply(Request request) {
        try {
            if (request.getArguments().length > 0) {
                return new ExecutionResponse(false, "Неправильное кол-во аргументов!\nИспользование: '" + getName() + "'");
            }

            SpaceMarine newbee = request.getSpaceMarine();

            console.println("* Добавление нового SpaceMarine...");

            if (newbee != null && newbee.validate()) {
                collectionManager.add(newbee);
                return new ExecutionResponse("SpaceMarine успешно добавлен!");
            } else {
                return new ExecutionResponse(false, "Поля SpaceMarine не валидны! SpaceMarine не создан!");
            }
        } catch (Exception e) {
            return new ExecutionResponse(false, "Что-то пошло не так...");
        }
    }
}
