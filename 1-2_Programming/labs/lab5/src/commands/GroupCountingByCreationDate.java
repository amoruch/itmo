package commands;

import java.util.*;
import managers.*;
import models.*;
import utility.*;

/**
 * Команда 'group_counting_by_creation_date' - сгруппировывает элементы
 * коллекции по значению поля creationDate, выводит количество элементов в
 * каждой группе.
 */
public class GroupCountingByCreationDate extends Command {

    private final Console console;
    private final CollectionManager collectionManager;

    public GroupCountingByCreationDate(Console console, CollectionManager collectionManager) {
        super("group_counting_by_creation_date", "сгруппировать элементы коллекции по значению поля creationDate, вывести количество элементов в каждой группе");
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

            console.println("* Выполняется группировка SpaceMarine...");

            HashMap<String, Integer> group = new HashMap<>();
            LinkedList<SpaceMarine> collection = collectionManager.getCollection();
            for (SpaceMarine unit : collection) {
                String date = unit.getCreationDate().toLocalDate().toString();
                group.put(date, group.get(date) == null ? 1 : group.get(date) + 1);
            }

            console.println(group.toString());

            return new ExecutionResponse("Группировка SpaceMarine по creationDate выполнена!");
        } catch (Exception e) {
            return new ExecutionResponse(false, "Что-то пошло не так...");
        }
    }
}
