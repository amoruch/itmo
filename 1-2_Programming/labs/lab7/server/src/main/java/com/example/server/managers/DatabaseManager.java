package com.example.server.managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Creates short-lived JDBC connections for the server repositories.
 */
public final class DatabaseManager {

    private static final String DEFAULT_URL = "jdbc:postgresql://localhost:5432/lab7";
    private static final String DEFAULT_USER = "postgres";

    private final String url;
    private final String user;
    private final String password;

    private DatabaseManager(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public static DatabaseManager fromEnvironment() {
        return new DatabaseManager(
                setting("LAB7_DB_URL", DEFAULT_URL),
                setting("LAB7_DB_USER", DEFAULT_USER),
                setting("LAB7_DB_PASSWORD", ""));
    }

    public Connection openConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public void verifyConnection() throws SQLException {
        try (Connection connection = openConnection(); PreparedStatement statement = connection.prepareStatement("SELECT 1")) {
            statement.executeQuery();
        }
    }

    private static String setting(String name, String defaultValue) {
        String property = System.getProperty(name);
        if (property != null && !property.isBlank()) {
            return property;
        }
        String environment = System.getenv(name);
        return environment == null || environment.isBlank() ? defaultValue : environment;
    }
}
