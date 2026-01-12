package SpringProject.persistences;

import lombok.extern.slf4j.Slf4j;
import SpringProject.entities.Playlists;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of GenreDao interface
 * Handles all database operations for genre management
 *
 * @author [Maryam]
 */
@Repository
@Slf4j
public class PlaylistDaoImpl implements PlaylistDao {

    private Connector connector;

    public PlaylistDaoImpl(Connector connector) {
        this.connector = connector;
    }

    @Override
    public void closeConnection() {
        connector.freeConnection();
    }

    /**
     * Creates a new playlist in the database
     *
     * @param playlists The playlist object containing playlist details (name, description,user_id)
     * @return The created playlist object with the generated ID, or null if creation failed
     * @throws SQLException if a database access error occurs, the SQL statement fails,
     */
    @Override
    public Playlists create(Playlists playlists) throws SQLException {
        if (playlists == null) {
            throw new IllegalArgumentException("Cannot create a null playlist.");
        }

        Connection conn = connector.getConnection();
        if (conn == null) {
            throw new SQLException("create(): Could not establish connection to database.");
        }

        String sql = "INSERT INTO playlists (name, description, userId, isPublic) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, playlists.getName());
            ps.setString(2, playlists.getDescription());
            ps.setInt(3, playlists.getUserId());
            ps.setBoolean(4, playlists.getIsPublic());

            int rowsInserted = ps.executeUpdate();
            if (rowsInserted == 0) {
                return null;
            }

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    playlists.setId(keys.getInt(1));
                }
            }

        } catch (SQLException e) {
            log.error("create() - SQL statement failed. \nException: {}", e.getMessage());
            throw e;
        }

        return playlists;
    }

    /**
     * Retrieves all playlists from the database
     * This includes both public and private playlists from all users
     *
     * @return List of all Playlists objects in the database, empty list if none exist
     * @throws SQLException if a database access error occurs or the SQL query fails
     */
    @Override
    public List<Playlists> findAll() throws SQLException {
        Connection conn = connector.getConnection();
        if (conn == null) {
            throw new SQLException("findAll(): Could not establish connection to database.");
        }

        ArrayList<Playlists> playlists = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM playlists")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    playlists.add(mapPlaylistRow(rs));
                }
            } catch (SQLException e) {
                log.error("findAll(): Issue running query or processing result set. \nException: {}", e.getMessage());
                throw e;
            }
        } catch (SQLException e) {
            log.error("findAll() - SQL query could not be prepared. \nException: {}", e.getMessage());
            throw e;
        }

        return playlists;
    }

    /**
     * Retrieves all playlists created by a specific user
     * Returns only playlists where the user is the owner
     *
     * @param userId The ID of the user whose playlists to retrieve
     * @return List of Playlists objects owned by the specified user, empty list if none found
     * @throws SQLException if a database access error occurs or the SQL query fails
     */
    @Override
    public List<Playlists> findByUserId(int userId) throws SQLException {
        Connection conn = connector.getConnection();
        if (conn == null) {
            throw new SQLException("findByUserId(): Could not establish connection.");
        }

        ArrayList<Playlists> playlists = new ArrayList<>();

        try (PreparedStatement ps =
                     conn.prepareStatement("SELECT * FROM playlists WHERE userId = ?")) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    playlists.add(mapPlaylistRow(rs));
                }
            } catch (SQLException e) {
                log.error("findByUserId(): Issue running query or processing result set. \nException: {}", e.getMessage());
                throw e;
            }

        } catch (SQLException e) {
            log.error("findByUserId() - Could not prepare SQL statement. \nException: {}", e.getMessage());
            throw e;
        }

        return playlists;
    }

    /**
     * Retrieves all playlists visible to a specific user
     * This includes:
     * - All playlists owned by the user (both public and private)
     * - All public playlists from other users
     *
     * @param userId The ID of the user for whom to retrieve visible playlists
     * @return List of Playlists objects visible to the user, empty list if none found
     * @throws SQLException if a database access error occurs or the SQL query fails
     */
    @Override
    public List<Playlists> getVisiblePlaylists(int userId) throws SQLException {
        Connection conn = connector.getConnection();
        if (conn == null) {
            throw new SQLException("getVisiblePlaylists(): Could not establish connection.");
        }

        ArrayList<Playlists> playlists = new ArrayList<>();

        String sql = """
                SELECT * FROM playlists
                WHERE userId = ?
                   OR isPublic = TRUE
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    playlists.add(mapPlaylistRow(rs));
                }
            } catch (SQLException e) {
                log.error("getVisiblePlaylists(): Issue processing resultset. \nException: {}", e.getMessage());
                throw e;
            }

        } catch (SQLException e) {
            log.error("getVisiblePlaylists() - SQL could not be prepared. \nException: {}", e.getMessage());
            throw e;
        }

        return playlists;
    }

    /**
     * Updates an existing playlist's information
     * Can update name, description, and public/private status
     * The playlist ID must be set in the playlists object
     *
     * @param playlists The playlist object containing updated information with a valid ID
     * @return true if the playlist was successfully updated, false if playlist not found or update failed
     * @throws SQLException if a database access error occurs or the SQL update statement fails
     */
    @Override
    public boolean update(Playlists playlists) throws SQLException {
        if (playlists == null || playlists.getId() <= 0) {
            throw new IllegalArgumentException("update(): playlist must not be null and must have a valid ID.");
        }

        Connection conn = connector.getConnection();
        if (conn == null) {
            throw new SQLException("update(): Could not establish connection.");
        }

        int rowsUpdated = 0;

        String sql = """
                UPDATE playlists
                SET name = ?, description = ?, isPublic = ?
                WHERE id = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, playlists.getName());
            ps.setString(2, playlists.getDescription());
            ps.setBoolean(3, playlists.getIsPublic());
            ps.setInt(4, playlists.getId());

            rowsUpdated = ps.executeUpdate();

        } catch (SQLException e) {
            log.error("update() - SQL failed. \nException: {}", e.getMessage());
            throw e;
        }

        return rowsUpdated == 1;
    }

    /**
     * Deletes a playlist from the database
     * This will also delete all associated playlist_songs entries due to CASCADE constraint
     *
     * @param id The ID of the playlist to delete
     * @return true if the playlist was successfully deleted, false if playlist not found or deletion failed
     * @throws SQLException if a database access error occurs or the SQL delete statement fails
     */
    @Override
    public boolean delete(int id) throws SQLException {
        Connection conn = connector.getConnection();
        if (conn == null) {
            throw new SQLException("delete(): Could not establish connection.");
        }

        int rowsDeleted = 0;

        try (PreparedStatement ps =
                     conn.prepareStatement("DELETE FROM playlists WHERE id = ?")) {

            ps.setInt(1, id);
            rowsDeleted = ps.executeUpdate();

        } catch (SQLException e) {
            log.error("delete() - SQL statement failed. \nException: {}", e.getMessage());
            throw e;
        }

        return rowsDeleted == 1;
    }

    private static Playlists mapPlaylistRow(ResultSet rs) throws SQLException {
        return Playlists.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .userId(rs.getInt("userId"))
                .isPublic(rs.getBoolean("isPublic"))
                .createdAt(rs.getTimestamp("createdDate"))
                .build();
    }
}
