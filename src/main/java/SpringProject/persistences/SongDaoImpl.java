package SpringProject.persistences;

import SpringProject.entities.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SongDaoImpl implements SongDao {

    private final Connector connector;

    public SongDaoImpl(Connector connector) {
        this.connector = connector;
    }

    @Override
    public List<Song> findAll() {
        List<Song> songs = new ArrayList<>();
        String sql = "SELECT id, title, artistId, albumId, genreId, durationSeconds, releaseYear FROM songs";

        try (Connection conn = connector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Song song = new Song();
                song.setId(rs.getInt("id"));
                song.setTitle(rs.getString("title"));
                song.setArtistId(rs.getInt("artistId"));
                song.setAlbumId(rs.getInt("albumId"));
                song.setGenreId(rs.getInt("genreId"));
                song.setDurationSeconds(rs.getDouble("durationSeconds"));
                song.setReleaseYear(rs.getInt("releaseYear"));
                songs.add(song);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // You can also log with @Slf4j
        }

        return songs;
    }

    @Override
    public List<Song> getSongsByTitle(String title) {
        List<Song> songs = new ArrayList<>();
        String query = "SELECT * FROM songs WHERE title LIKE ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, "%" + title + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                songs.add(mapRowToSong(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return songs;
    }

    @Override
    public List<Song> getSongsByArtist(String artistName) {
        List<Song> songs = new ArrayList<>();
        String query = """
            SELECT s.* 
            FROM songs s 
            JOIN artists a ON s.artistId = a.id 
            WHERE a.name LIKE ?
        """;

        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, "%" + artistName + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                songs.add(mapRowToSong(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return songs;
    }

    @Override
    public List<Song> getSongsByAlbum(String albumTitle) {
        List<Song> songs = new ArrayList<>();
        String query = """
            SELECT s.* 
            FROM songs s 
            JOIN albums al ON s.albumId = al.id 
            WHERE al.title LIKE ?
        """;

        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, "%" + albumTitle + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                songs.add(mapRowToSong(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return songs;
    }

    @Override
    public List<Song> getSongsByGenre(String genreName) {
        List<Song> songs = new ArrayList<>();
        String query = """
            SELECT s.* 
            FROM songs s 
            JOIN genres g ON s.genreId = g.id 
            WHERE g.name LIKE ?
        """;

        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, "%" + genreName + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                songs.add(mapRowToSong(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return songs;
    }

    public Song getSongById(int id) {
        Song song = null;
        String query = "SELECT * FROM songs WHERE id = ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                song = mapRowToSong(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return song;
    }

    private Song mapRowToSong(ResultSet rs) throws SQLException {
        return Song.builder()
                .id(rs.getInt("id"))
                .title(rs.getString("title"))
                .artistId(rs.getInt("artistId"))
                .albumId(rs.getInt("albumId"))
                .genreId(rs.getInt("genreId"))
                .durationSeconds(rs.getDouble("durationSeconds"))
                .releaseYear(rs.getInt("releaseYear"))
                .build();
    }
}
