package com.example.client.commands;

import com.example.common.Protocol.CommandType;

public final class GroupCountingByCreationDate extends AbstractEmptyCommand {

    public GroupCountingByCreationDate() {
        super("group_counting_by_creation_date", "сгруппировать элементы по дате создания", CommandType.GROUP_COUNTING_BY_CREATION_DATE);
    }
}
