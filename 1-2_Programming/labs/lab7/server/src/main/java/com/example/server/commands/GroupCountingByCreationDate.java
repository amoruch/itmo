package com.example.server.commands;

import com.example.common.Protocol.CommandType;
import com.example.common.Protocol.Request;
import com.example.common.Protocol.Response;
import com.example.server.managers.CollectionManager;
import com.example.server.utility.AuthenticatedUser;

/**
 * Groups elements by their creation date.
 */
public final class GroupCountingByCreationDate extends Command {

    public GroupCountingByCreationDate(CollectionManager collectionManager) {
        super(CommandType.GROUP_COUNTING_BY_CREATION_DATE, "group_counting_by_creation_date",
                "сгруппировать элементы по creationDate", collectionManager);
    }

    @Override
    public Response apply(Request request, AuthenticatedUser user) {
        return ok(collectionManager.countByCreationDate().isEmpty()
                ? "Коллекция пуста!" : collectionManager.countByCreationDate().toString());
    }
}
