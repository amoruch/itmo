package commands;

import java.util.*;
import managers.*;
import models.*;
import utility.*;

/**
 * Команда 'add_if_max' - добавляет новый элемент в коллекцию, если его значение
 * превышает значение наибольшего элемента этой коллекции.
 */
public class AddIfMax extends Command {

    private final Console console;
    private final CollectionManager collectionManager;

    public AddIfMax(Console console, CollectionManager collectionManager) {
        super("add_if_max {element}", "добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции");
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

            LinkedList<SpaceMarine> collection = collectionManager.getCollection();
            if (!collection.isEmpty()) {
                SpaceMarine maxUnit = Collections.max(collection);
                if (maxUnit.compareTo(newbee) > 0) {
                    return new ExecutionResponse(false, "SpaceMarine не добавлен: его значение меньше максимального элемента коллекции.");
                }
            }

            collectionManager.add(newbee);
            collectionManager.update();

            return new ExecutionResponse("SpaceMarine успешно добавлен!");
        } catch (Exception e) {
            return new ExecutionResponse(false, "Что-то пошло не так...");
        }
    }
}
