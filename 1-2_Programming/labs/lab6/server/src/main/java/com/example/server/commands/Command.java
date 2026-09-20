package com.example.server.commands;

import com.example.common.Protocol.CommandType;
import com.example.common.Protocol.Request;
import com.example.common.Protocol.Response;
import com.example.server.managers.CollectionManager;

/**
 * Base class for server commands, following the structure of lab 5.
 */
public abstract class Command {

    private final CommandType type;
    private final String name;
    private final String description;
    protected final CollectionManager collectionManager;

    protected Command(
            CommandType type, String name, String description, CollectionManager collectionManager) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.collectionManager = collectionManager;
    }

    public CommandType type() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public abstract Response apply(Request request);

    protected Response ok(String message) {
        return new Response(true, message);
    }

    protected Response fail(String message) {
        return new Response(false, message);
    }
}
