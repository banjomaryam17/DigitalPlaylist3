package SpringProject.persistences;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;
import SpringProject.entities.Genre;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of GenreDao interface
 * Handles all database operations for genre management
 *
 * @author [Maryam]
 */
@Slf4j
public class GenreImpl implements GenreDao {

    private Connector connector;

    public GenreImpl(Connector connector) {
        this.connector = connector;
    }

    /**
     * Closes the database connection
     */
    public void closeConnection() {
        connector.freeConnection();
    }

    /**
     * Creates a new genre in the database
     *
     * @param genre The genre object containing genre details (name, description)
     * @return The created Genre object with the generated ID, or null if creation failed
     * @throws SQLException if a database access error occurs, the SQL statement fails,
     */
    @Override
    public Genre create(Genre genre) throws SQLException {
        if (genre == null) {
            throw new IllegalArgumentException("Cannot add a null Genre to database");
        }

        Connection conn = connector.getConnection();
        if (conn == null) {
            throw new SQLException("create(): Could not establish connection to database.");
        }

        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO genres (name, description) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, genre.getName());
            ps.setString(2, genre.getDescription());

            int addedRows = ps.executeUpdate();

            if (addedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        genre.setId(generatedKeys.getInt(1));
                        log.info("Genre created with ID: {}", genre.getId());
                        return genre;
                    }
                }
            }

            return null;

        } catch (SQLException e) {
            log.error("create() - The SQL query could not be prepared. \nException: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Retrieves a genre by its unique ID
     *
     * @param id The ID of the genre to retrieve
     * @return The Genre object if found, null if no genre exists with the specified ID
     * @throws SQLException if a database access error occurs or the SQL query fails
     */
    @Override
    public Genre findById(int id) throws SQLException {
        Connection conn = connector.getConnection();
        if (conn == null) {
            throw new SQLException("findById(): Could not establish connection to database.");
        }

        Genre genre = null;
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM genres WHERE id = ?")) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    genre = mapGenreRow(rs);
                }
            } catch (SQLException e) {
                log.error("findById(): An issue occurred when running the query or processing the resultset. " +
                        "\nException: {}", e.getMessage());
                throw e;
            }
        } catch (SQLException e) {
            log.error("findById() - The SQL query could not be prepared. \nException: {}", e.getMessage());
            throw e;
        }

        return genre;
    }

    /**
     * Retrieves all genres from the database
     *
     * @return List of all Genre objects in the database, empty list if no genres exist
     * @throws SQLException if a database access error occurs or the SQL query fails
     */
    @Override
    public List<Genre> findAll() throws SQLException {
        Connection conn = connector.getConnection();
        if (conn == null) {
            throw new SQLException("findAll(): Could not establish connection to database.");
        }

        ArrayList<Genre> genres = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM genres ORDER BY name")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Genre genre = mapGenreRow(rs);
                    genres.add(genre);
                }
            } catch (SQLException e) {
                log.error("findAll(): An issue occurred when running the query or processing the resultset. " +
                        "\nException: {}", e.getMessage());
                throw e;
            }
        } catch (SQLException e) {
            log.error("findAll() - The SQL query could not be prepared. \nException: {}", e.getMessage());
            throw e;
        }

        return genres;
    }

    /**
     * Updates an existing genre's information
     * Can update genre name and description
     * The genre ID must be set in the genre object
     *
     * @param genre The genre object containing updated information with a valid ID
     * @return true if the genre was successfully updated, false if genre not found or update failed
     * @throws SQLException if a database access error occurs, the SQL update statement fails,
     * or UNIQUE constraint is violated (duplicate genre name)
     */
    @Override
    public boolean update(Genre genre) throws SQLException {
        if (genre == null) {
            throw new IllegalArgumentException("Cannot update a null Genre");
        }

        Connection conn = connector.getConnection();
        if (conn == null) {
            throw new SQLException("update(): Could not establish connection to database.");
        }

        int updatedRows = 0;
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE genres SET name = ?, description = ? WHERE id = ?")) {

            ps.setString(1, genre.getName());
            ps.setString(2, genre.getDescription());
            ps.setInt(3, genre.getId());

            updatedRows = ps.executeUpdate();

        } catch (SQLException e) {
            log.error("update() - The SQL query could not be prepared. \nException: {}", e.getMessage());
            throw e;
        }

        return updatedRows == 1;
    }

    /**
     * Deletes a genre from the database
     * Note: This operation may fail if the genre is referenced by existing songs or albums
     * due to FOREIGN KEY constraints (RESTRICT behavior)
     *
     * @param id The ID of the genre to delete
     * @return true if the genre was successfully deleted, false if genre not found or deletion failed
     * @throws SQLException if a database access error occurs, the SQL delete statement fails,
     *  or foreign key constraint prevents deletion (genre is in use)
     */
    @Override
    public boolean delete(int id) throws SQLException {
        Connection conn = connector.getConnection();
        if (conn == null) {
            throw new SQLException("delete(): Could not establish connection to database.");
        }

        int deletedRows = 0;
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM genres WHERE id = ?")) {
            ps.setInt(1, id);
            deletedRows = ps.executeUpdate();

        } catch (SQLException e) {
            // Check if it's a foreign key constraint error
            if (e.getMessage().contains("foreign key constraint")) {
                log.error("delete() - Cannot delete genre {} - it is referenced by songs or albums", id);
            } else {
                log.error("delete() - The SQL query could not be prepared. \nException: {}", e.getMessage());
            }
            throw e;
        }

        return deletedRows == 1;
    }

    /**
     * Helper method to map a ResultSet row to a Genre object
     *
     * @param rs The ResultSet positioned at a valid row
     * @return Genre object populated with data from the current ResultSet row
     * @throws SQLException if a database access error occurs or column not found
     */
    private static Genre mapGenreRow(ResultSet rs) throws SQLException {
        Genre genre = new Genre();
        genre.setId(rs.getInt("id"));
        genre.setName(rs.getString("name"));
        genre.setDescription(rs.getString("description"));
        return genre;
    }
}