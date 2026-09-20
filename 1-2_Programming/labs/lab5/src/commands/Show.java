package commands;

import managers.*;
import utility.*;

/**
 * Команда 'show' - выводит все элементы коллекции
 */
public class Show extends Command {

    private final Console console;
    private final CollectionManager collectionManager;

    public Show(Console console, CollectionManager collectionManager) {
        super("show", "вывести все элементы коллекции");
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

            console.println("* Вывод всех элементов коллекции:");
            console.println(collectionManager);

            return new ExecutionResponse("Коллекция успешно выведена!");
        } catch (Exception e) {
            return new ExecutionResponse(false, "Что-то пошло не так... Коллекция не выведена...");
        }
    }
}
