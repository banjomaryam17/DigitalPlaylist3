package SpringProject.persistences;

import SpringProject.entities.Album;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AlbumDaoImpl implements AlbumDao {

    private final Connector connector;

    public AlbumDaoImpl(Connector connector) {
        this.connector = connector;
    }

    @Override
    public List<Album> getAlbumsByArtistId(int artistId) {
        List<Album> albums = new ArrayList<>();
        String query = "SELECT * FROM albums WHERE artistId = ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, artistId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Album album = Album.builder()
                        .id(rs.getInt("id"))
                        .title(rs.getString("title"))
                        .artistId(rs.getInt("artistId"))
                        .genreId(rs.getInt("genreId"))
                        .releaseDate(rs.getDate("releaseDate") != null ?
                                rs.getDate("releaseDate").toLocalDate() : null)
                        .build();
                albums.add(album);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return albums;
    }

    @Override
    public Album getAlbumById(int id) {
        Album album = null;
        String query = "SELECT * FROM albums WHERE id = ?";
        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                album = Album.builder()
                        .id(rs.getInt("id"))
                        .title(rs.getString("title"))
                        .artistId(rs.getInt("artistId"))
                        .genreId(rs.getInt("genreId"))
                        .releaseDate(rs.getDate("releaseDate") != null ?
                                rs.getDate("releaseDate").toLocalDate() : null)
                        .build();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return album;
    }
}
