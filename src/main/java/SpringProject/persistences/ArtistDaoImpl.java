package SpringProject.persistences;

import SpringProject.entities.Album;
import SpringProject.entities.Artist;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ArtistDaoImpl implements ArtistDao {

    private final Connector connector;

    public ArtistDaoImpl(Connector connector) {
        this.connector = connector;
    }

    @Override
    public List<Artist> getAllArtists() {
        return findAll();
    }

    @Override
    public Artist getArtistById(int id) {
        return findById(id).orElse(null);
    }

    @Override
    public List<Album> getAlbumsByArtist(int artistId) {
        List<Album> albums = new ArrayList<>();
        String sql = "SELECT * FROM albums WHERE artistId = ?";

        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, artistId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                albums.add(mapRowToAlbum(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return albums;
    }

    @Override
    public List<Artist> findAll() {
        List<Artist> artists = new ArrayList<>();
        String sql = "SELECT * FROM artists";

        try (Connection conn = connector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                artists.add(mapRowToArtist(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return artists;
    }

    @Override
    public Optional<Artist> findById(int id) {
        String sql = "SELECT * FROM artists WHERE id = ?";

        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(mapRowToArtist(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    private Artist mapRowToArtist(ResultSet rs) throws SQLException {
        return Artist.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .bio(rs.getString("bio"))
                .country(rs.getString("country"))
                .formedYear(rs.getInt("formedYear"))
                .build();
    }

    private Album mapRowToAlbum(ResultSet rs) throws SQLException {
        return Album.builder()
                .id(rs.getInt("id"))
                .title(rs.getString("title"))
                .artistId(rs.getInt("artistId"))
                .genreId(rs.getInt("genreId"))
                .releaseDate(rs.getDate("releaseDate").toLocalDate())
                .build();
    }
}
