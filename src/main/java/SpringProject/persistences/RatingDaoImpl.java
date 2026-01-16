package SpringProject.persistences;

import SpringProject.entities.Rating;
import SpringProject.entities.Song;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;

@Slf4j
@Repository
public class RatingDaoImpl implements RatingDao {

    private final Connector connector;

    public RatingDaoImpl(Connector connector) {
        this.connector = connector;
    }

    public void closeConnection() {
        connector.freeConnection();
    }

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
        }
    }

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
        }

        return ratings;
    }

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
        }

        return null;
    }

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
        }

        return ratings;
    }

    @Override
    public Song getLowestRatedSong() {
        // If you don’t have SongDao yet, we return a Song with only the ID set (NOT null).
        Integer id = getSongIdByQuery("SELECT songID FROM rating GROUP BY songID ORDER BY AVG(userRating) ASC LIMIT 1");
        return (id == null) ? emptySong() : songWithId(id);
    }

    @Override
    public Song getMostPopularSong() {
        Integer id = getSongIdByQuery("SELECT songID FROM rating GROUP BY songID ORDER BY COUNT(*) DESC LIMIT 1");
        return (id == null) ? emptySong() : songWithId(id);
    }

    @Override
    public Song getTopRatedSong() {
        Integer id = getSongIdByQuery("SELECT songID FROM rating GROUP BY songID ORDER BY AVG(userRating) DESC LIMIT 1");
        return (id == null) ? emptySong() : songWithId(id);
    }


    private Rating mapRatingRow(ResultSet rs) throws SQLException {
        return Rating.builder()
                .username(rs.getString("username"))
                .songID(rs.getInt("songID"))
                .userRating(rs.getDouble("userRating"))
                .build();
    }

    private Integer getSongIdByQuery(String sql) {
        try {
            Connection conn = connector.getConnection();
            if (conn == null) return null;

            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                if (rs.next()) return rs.getInt("songID");
            }
        } catch (SQLException e) {
            log.error("getSongIdByQuery() failed: {}", e.getMessage());
        }
        return null;
    }

    // These avoid returning null for Song methods
    private Song emptySong() {
        return Song.builder().build();
    }

    private Song songWithId(int songId) {
        return Song.builder().id(songId).build();
    }
}
