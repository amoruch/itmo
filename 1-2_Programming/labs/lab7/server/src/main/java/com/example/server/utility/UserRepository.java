package com.example.server.utility;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HexFormat;
import java.util.Optional;

import com.example.common.Protocol.Credentials;
import com.example.server.managers.DatabaseManager;

/**
 * JDBC operations for accounts stored in {@code lab7_users}.
 */
public final class UserRepository {

    private static final String REGISTER = """
            INSERT INTO lab7_users (login, password_hash)
            VALUES (?, ?)
            ON CONFLICT (login) DO NOTHING
            RETURNING id
            """;
    private static final String AUTHENTICATE = """
            SELECT id
            FROM lab7_users
            WHERE login = ? AND password_hash = ?
            """;

    private final DatabaseManager databaseManager;

    public UserRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public Optional<AuthenticatedUser> register(Credentials credentials) throws SQLException {
        try (Connection connection = databaseManager.openConnection(); PreparedStatement statement = connection.prepareStatement(REGISTER)) {
            statement.setString(1, credentials.login());
            statement.setString(2, sha224(credentials.password()));
            try (ResultSet result = statement.executeQuery()) {
                return result.next()
                        ? Optional.of(new AuthenticatedUser(result.getInt("id"), credentials.login()))
                        : Optional.empty();
            }
        }
    }

    public Optional<AuthenticatedUser> authenticate(Credentials credentials) throws SQLException {
        try (Connection connection = databaseManager.openConnection(); PreparedStatement statement = connection.prepareStatement(AUTHENTICATE)) {
            statement.setString(1, credentials.login());
            statement.setString(2, sha224(credentials.password()));
            try (ResultSet result = statement.executeQuery()) {
                return result.next()
                        ? Optional.of(new AuthenticatedUser(result.getInt("id"), credentials.login()))
                        : Optional.empty();
            }
        }
    }

    private static String sha224(String password) {
        try {
            return HexFormat.of().formatHex(
                    MessageDigest.getInstance("SHA-224").digest(password.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-224 is unavailable in this Java runtime", exception);
        }
    }
}
