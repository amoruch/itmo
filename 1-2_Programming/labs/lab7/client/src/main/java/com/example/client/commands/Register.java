package com.example.client.commands;

import com.example.common.Protocol.CommandType;

/** Registers a new account and stores its credentials for subsequent requests. */
public final class Register extends AccountCommand {

    public Register() {
        super("register", "зарегистрировать пользователя", CommandType.REGISTER);
    }
}
