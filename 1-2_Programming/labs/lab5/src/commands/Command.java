package commands;

import utility.*;

/**
 * Абстрактная команда с именем и описанием.
 */
public abstract class Command implements Describable, Executable {

    private final String name;
    private final String description;
    protected boolean requireData;

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
        this.requireData = false;
    }

    /**
     * @return Название и использование команды.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * @return Описание команды.
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * @return Нужен ли команде SpaceMarine
     */
    public boolean RequireData() {
        return requireData; // косяк для runner'а чтобы он понимал когда просить у юзера SpaceMarine
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Command command = (Command) obj;
        return name.equals(command.name) && description.equals(command.description);
    }

    @Override
    public int hashCode() {
        return name.hashCode() + description.hashCode();
    }

    @Override
    public String toString() {
        return "Command{" + "name='" + name + "\'"
                + ", description='" + description + "\'" + "}";
    }
}
