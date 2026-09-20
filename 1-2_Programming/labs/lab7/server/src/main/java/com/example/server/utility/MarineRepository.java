package com.example.server.utility;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.common.Model.AstartesCategory;
import com.example.common.Model.Chapter;
import com.example.common.Model.Coordinates;
import com.example.common.Model.MeleeWeapon;
import com.example.common.Model.SpaceMarine;
import com.example.server.managers.DatabaseManager;

/**
 * JDBC operations for {@code lab7_space_marines}.
 */
public final class MarineRepository {

    private static final String LOAD_ALL = """
            SELECT id, name, coordinate_x, coordinate_y, creation_date, health, loyal,
                   category, melee_weapon, chapter_name, chapter_world
            FROM lab7_space_marines
            ORDER BY id
            """;
    private static final String INSERT = """
            INSERT INTO lab7_space_marines (
                name, coordinate_x, coordinate_y, health, loyal,
                category, melee_weapon, chapter_name, chapter_world, creator_id
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            RETURNING id, creation_date
            """;
    private static final String UPDATE = """
            UPDATE lab7_space_marines
            SET name = ?, coordinate_x = ?, coordinate_y = ?, health = ?, loyal = ?,
                category = ?, melee_weapon = ?, chapter_name = ?, chapter_world = ?
            WHERE id = ? AND creator_id = ?
            """;
    private static final String DELETE_ONE = """
            DELETE FROM lab7_space_marines
            WHERE id = ? AND creator_id = ?
            """;
    private static final String DELETE_OWNER = """
            DELETE FROM lab7_space_marines
            WHERE creator_id = ?
            RETURNING id
            """;
    private static final String DELETE_IDS = """
            DELETE FROM lab7_space_marines
            WHERE creator_id = ? AND id = ANY (?)
            RETURNING id
            """;

    private final DatabaseManager databaseManager;

    public MarineRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public List<SpaceMarine> loadAll() throws SQLException {
        try (Connection connection = databaseManager.openConnection(); PreparedStatement statement = connection.prepareStatement(LOAD_ALL); ResultSet result = statement.executeQuery()) {
            List<SpaceMarine> marines = new ArrayList<>();
            while (result.next()) {
                marines.add(readMarine(result));
            }
            return marines;
        }
    }

    public SpaceMarine insert(AuthenticatedUser user, SpaceMarine draft) throws SQLException {
        try (Connection connection = databaseManager.openConnection(); PreparedStatement statement = connection.prepareStatement(INSERT)) {
            bindMarine(statement, draft, 1);
            statement.setInt(10, user.id());
            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) {
                    throw new SQLException("The database did not return a generated SpaceMarine id");
                }
                OffsetDateTime creationDate = result.getObject("creation_date", OffsetDateTime.class);
                return draft.withServerFields(result.getInt("id"), creationDate.toZonedDateTime());
            }
        }
    }

    public boolean update(AuthenticatedUser user, int id, SpaceMarine draft) throws SQLException {
        try (Connection connection = databaseManager.openConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            bindMarine(statement, draft, 1);
            statement.setInt(10, id);
            statement.setInt(11, user.id());
            return statement.executeUpdate() == 1;
        }
    }

    public boolean delete(AuthenticatedUser user, int id) throws SQLException {
        try (Connection connection = databaseManager.openConnection(); PreparedStatement statement = connection.prepareStatement(DELETE_ONE)) {
            statement.setInt(1, id);
            statement.setInt(2, user.id());
            return statement.executeUpdate() == 1;
        }
    }

    public Set<Integer> deleteAllOwnedBy(AuthenticatedUser user) throws SQLException {
        try (Connection connection = databaseManager.openConnection(); PreparedStatement statement = connection.prepareStatement(DELETE_OWNER)) {
            statement.setInt(1, user.id());
            return readDeletedIds(statement);
        }
    }

    public Set<Integer> deleteOwnedIds(AuthenticatedUser user, Collection<Integer> ids)
            throws SQLException {
        if (ids.isEmpty()) {
            return Set.of();
        }
        try (Connection connection = databaseManager.openConnection(); PreparedStatement statement = connection.prepareStatement(DELETE_IDS)) {
            statement.setInt(1, user.id());
            Array idArray = connection.createArrayOf("INTEGER", ids.toArray(Integer[]::new));
            try {
                statement.setArray(2, idArray);
                return readDeletedIds(statement);
            } finally {
                idArray.free();
            }
        }
    }

    private static Set<Integer> readDeletedIds(PreparedStatement statement) throws SQLException {
        try (ResultSet result = statement.executeQuery()) {
            Set<Integer> ids = new HashSet<>();
            while (result.next()) {
                ids.add(result.getInt("id"));
            }
            return ids;
        }
    }

    private static void bindMarine(PreparedStatement statement, SpaceMarine marine, int index)
            throws SQLException {
        statement.setString(index, marine.name());
        statement.setLong(index + 1, marine.coordinates().x());
        statement.setLong(index + 2, marine.coordinates().y());
        statement.setDouble(index + 3, marine.health());
        statement.setBoolean(index + 4, marine.loyal());
        statement.setString(index + 5, marine.category().name());
        statement.setString(index + 6, marine.meleeWeapon().name());
        if (marine.chapter() == null) {
            statement.setNull(index + 7, Types.VARCHAR);
            statement.setNull(index + 8, Types.VARCHAR);
        } else {
            statement.setString(index + 7, marine.chapter().name());
            statement.setString(index + 8, marine.chapter().world());
        }
    }

    private static SpaceMarine readMarine(ResultSet result) throws SQLException {
        String chapterName = result.getString("chapter_name");
        Chapter chapter = chapterName == null ? null
                : new Chapter(chapterName, result.getString("chapter_world"));
        OffsetDateTime creationDate = result.getObject("creation_date", OffsetDateTime.class);
        return new SpaceMarine(
                result.getInt("id"),
                result.getString("name"),
                new Coordinates(result.getLong("coordinate_x"), result.getLong("coordinate_y")),
                creationDate.toZonedDateTime(),
                result.getDouble("health"),
                result.getBoolean("loyal"),
                AstartesCategory.valueOf(result.getString("category")),
                MeleeWeapon.valueOf(result.getString("melee_weapon")),
                chapter);
    }
}
