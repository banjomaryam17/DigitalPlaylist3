package SpringProject.persistences;

import java.sql.*;
import java.util.ArrayList;

import lombok.extern.slf4j.Slf4j;
import SpringProject.entities.Rating;
import SpringProject.entities.Song;

/**
 * Database implementation of {@link RatingDao}.
 * <p>
 * This class uses JDBC to read/write {@link Rating} records in the {@code rating} table.
 * It requires a {@link Connector} to provide a database connection.
 * </p>
 */
@Slf4j
public class RatingDaoImpl implements RatingDao {

    private final Connector connector;

    public RatingDaoImpl(Connector connector) {
        this.connector = connector;
    }


    /**
     * Inserts a new rating into the database.
     * <p>
     * If a rating already exists for the same username, songID key, the existing
     * record is updated instead.
     * </p>
     *
     * @param rating the rating to insert/update
     * @return number of rows affected usually 1
     * @throws SQLException             if a database error occurs
     * @throws IllegalArgumentException if {@code rating} is null
     */
    @Override
    public int addRating(Rating rating) throws SQLException {

        if (rating == null) {
            throw new IllegalArgumentException("addRating(): rating cannot be null");
        }

        Connection conn = connector.getConnection();
        if (conn == null) {
            throw new SQLException("addRating(): Could not connect to database");
        }

        String sql = """
                INSERT INTO rating (username, songID, userRating)
                VALUES (?, ?, ?)
                ON DUPLICATE KEY UPDATE userRating = VALUES(userRating)
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, rating.getUsername());
            ps.setInt(2, rating.getSongID());
            ps.setDouble(3, rating.getUserRating());
            return ps.executeUpdate();

        } catch (SQLException e) {
            log.error("addRating() failed: {}", e.getMessage());
            throw e;
        }
    }


    /**
     * Retrieves every rating from the database
     *
     * @return list of all ratings empty list if none found
     * @throws SQLException if a database error occurs
     */
    @Override
    public ArrayList<Rating> getAllRatings() throws SQLException {

        Connection conn = connector.getConnection();
        if (conn == null) {
            throw new SQLException("getAllRatings(): Could not connect to database");
        }

        ArrayList<Rating> ratings = new ArrayList<>();
        String sql = "SELECT username, songID, userRating FROM rating";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ratings.add(mapRatingRow(rs));
            }

        } catch (SQLException e) {
            log.error("getAllRatings() failed: {}", e.getMessage());
            throw e;
        }

        return ratings;
    }

    /**
     * Finds a rating using the composite key username, songID
     *
     * @param username the username to search for
     * @param songID   the song id to search for
     * @return the matching rating, or {@code null} if not found
     * @throws SQLException if a database error occurs
     */
    @Override
    public Rating findRatingByUsernameAndSongID(String username, int songID) throws SQLException {

        Connection conn = connector.getConnection();
        if (conn == null) {
            throw new SQLException("findRatingByUsernameAndSongID(): Could not connect to database");
        }

        String sql = "SELECT username, songID, userRating FROM rating WHERE username = ? AND songID = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setInt(2, songID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRatingRow(rs);
                }
            }

        } catch (SQLException e) {
            log.error("findRatingByUsernameAndSongID() failed: {}", e.getMessage());
            throw e;
        }

        return null;
    }

    /**
     * Retrieves all ratings submitted by a specific user
     *
     * @param username the username to search for
     * @return list of ratings for that user empty list if none found
     * @throws SQLException if a database error occurs
     */
    @Override
    public ArrayList<Rating> getUserRatingFromUsername(String username) throws SQLException {

        Connection conn = connector.getConnection();
        if (conn == null) {
            throw new SQLException("getUserRatingFromUsername(): Could not connect to database");
        }

        ArrayList<Rating> ratings = new ArrayList<>();
        String sql = "SELECT username, songID, userRating FROM rating WHERE username = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ratings.add(mapRatingRow(rs));
                }
            }

        } catch (SQLException e) {
            log.error("getUserRatingFromUsername() failed: {}", e.getMessage());
            throw e;
        }

        return ratings;
    }


    /**
     * Converts the current result set row into a {@link Rating} object
     *
     * @param rs the result set positioned at a row
     * @return a populated {@link Rating}
     * @throws SQLException if column access fails
     */
    private static Rating mapRatingRow(ResultSet rs) throws SQLException {
        return Rating.builder()
                .username(rs.getString("username"))
                .songID(rs.getInt("songID"))
                .userRating(rs.getDouble("userRating"))
                .build();
    }

}

