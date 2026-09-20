package commands;

import java.util.*;
import managers.*;
import models.*;
import utility.*;

/**
 * Команда 'print_descending' - выводит элементы коллекции в порядке убывания.
 */
public class PrintDescending extends Command {

    private final Console console;
    private final CollectionManager collectionManager;

    public PrintDescending(Console console, CollectionManager collectionManager) {
        super("print_descending", "вывести элементы коллекции в порядке убывания");
        this.console = console;
        this.collectionManager = collectionManager;
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

            console.println("* Вывод элементов коллекции в порядке убывания:");

            LinkedList<SpaceMarine> collection = collectionManager.getCollection();
            Collections.reverse(collection);
            for (SpaceMarine unit : collection) {
                console.println(unit);
            }

            return new ExecutionResponse("Коллекция успешно выведена!");
        } catch (Exception e) {
            return new ExecutionResponse(false, "Что-то пошло не так...");
        }
    }
}
