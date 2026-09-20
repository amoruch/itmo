package commands;

import managers.*;
import models.*;
import utility.*;

/**
 * Команда 'remove_lower' - удаляет из коллекции все элементы, меньшие, чем
 * заданный.
 */
public class RemoveLower extends Command {

    private final Console console;
    private final CollectionManager collectionManager;

    public RemoveLower(Console console, CollectionManager collectionManager) {
        super("remove_lower {element}", "удалить из коллекции все элементы, меньшие, чем заданный");
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

            SpaceMarine comp = request.getSpaceMarine();

            console.println("Удаление элементов из коллекции по шаблону...");

            if (collectionManager.getCollection().isEmpty()) {
                return new ExecutionResponse(false, "Коллекция пуста!");
            }
            while (!collectionManager.getCollection().isEmpty()) {
                SpaceMarine tmp = collectionManager.pop();
                if (tmp.compareTo(comp) > 0) {
                    collectionManager.add(tmp);
                    break;
                }
                console.println("Element with id " + tmp.getId() + " was removed...");
            }
            if (!collectionManager.getCollection().isEmpty()) {
                collectionManager.update();
            }

            return new ExecutionResponse("Подходящие элементы коллекции удалены!");
        } catch (Exception e) {
            return new ExecutionResponse(false, "Что-то пошло не так...");
        }
    }
}
