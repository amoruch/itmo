package com.example.server.utility;

/** An account successfully identified by the database for the current request. */
public record AuthenticatedUser(int id, String login) {

    public AuthenticatedUser {
        if (id <= 0 || login == null || login.isBlank()) {
            throw new IllegalArgumentException("Invalid authenticated user");
        }
    }
}
