package com.example.client.commands;

import com.example.common.Protocol.CommandType;

/** Authorizes an existing user for subsequent client requests. */
public final class Login extends AccountCommand {

    public Login() {
        super("login", "авторизоваться", CommandType.LOGIN);
    }
}
