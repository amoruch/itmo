package com.example.client.commands;

import com.example.client.utility.Ask;
import java.io.IOException;

abstract class AbstractCommand implements Command {

    private final String name;
    private final String description;

    AbstractCommand(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    int readId(String argument, Ask ask) throws IOException {
        if (!argument.isBlank()) {
            try {
                int id = Integer.parseInt(argument);
                if (id > 0) {
                    return id;
                }
            } catch (NumberFormatException ignored) {
            }
        }
        return ask.positiveInt("id (> 0): ");
    }
}
