package SpringProject.persistences;

import lombok.extern.slf4j.Slf4j;
import SpringProject.entities.PlaylistsSongs;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PlaylistSongImpl implements PlaylistSongDao {

    private Connector connector;

    public PlaylistSongImpl(Connector connector) {
        this.connector = connector;
    }

    /**
     * Add a song to a playlist
     *
     * @param playlistSong The playlist-song relationship to create
     * @return The created PlaylistSongDto with generated ID, or null if failed
     * @throws SQLException if database error occurs
     */
    @Override
    public PlaylistsSongs addSongToPlaylist(PlaylistsSongs playlistSong) throws SQLException {
        if (playlistSong == null) {
            throw new IllegalArgumentException("Cannot add a null playlist-song entry.");
        }

        Connection conn = connector.getConnection();
        if (conn == null) {
            throw new SQLException("addSongToPlaylist(): Could not establish connection to database.");
        }

        String sql = "INSERT INTO playlistSongs (playlistId, songId) VALUES (?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, playlistSong.getPlaylistId());
            ps.setInt(2, playlistSong.getSongId());

            int rowsInserted = ps.executeUpdate();
            if (rowsInserted == 0) {
                return null;
            }

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    playlistSong.setId(keys.getInt(1));
                }
            }

        } catch (SQLException e) {
            log.error("addSongToPlaylist() - SQL failed. \nException: {}", e.getMessage());
            throw e;
        }

        return playlistSong;
    }

    /**
     * Get all songs in a specific playlist
     *
     * @param id The ID of the playlist
     * @return List of PlaylistSongDto objects, empty list if none found
     * @throws SQLException if database error occurs
     */
    @Override
    public List<PlaylistsSongs> getSongsByPlaylistId(int id) throws SQLException {
        Connection conn = connector.getConnection();
        if (conn == null) {
            throw new SQLException("getSongsByPlaylistId(): Could not establish connection.");
        }

        ArrayList<PlaylistsSongs> songs = new ArrayList<>();

        String sql = "SELECT * FROM playlistSongs WHERE playlistId = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    songs.add(mapPlaylistSongRow(rs));
                }
            } catch (SQLException e) {
                log.error("getSongsByPlaylistId(): Issue processing resultset. \nException: {}", e.getMessage());
                throw e;
            }

        } catch (SQLException e) {
            log.error("getSongsByPlaylistId() - Could not prepare SQL statement. \nException: {}", e.getMessage());
            throw e;
        }

        return songs;
    }

    /**
     * Remove a song from a playlist
     *
     * @param id The ID of the playlist
     * @param songId The ID of the song to remove
     * @return true if deleted successfully, false otherwise
     * @throws SQLException if database error occurs
     */
    @Override
    public boolean removeSongFromPlaylist(int id, int songId) throws SQLException {
        Connection conn = connector.getConnection();
        if (conn == null) {
            throw new SQLException("removeSongFromPlaylist(): Could not establish connection.");
        }

        int rowsDeleted = 0;

        String sql = "DELETE FROM playlistSongs WHERE playlistId = ? AND songId = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, songId);

            rowsDeleted = ps.executeUpdate();

        } catch (SQLException e) {
            log.error("removeSongFromPlaylist() - SQL failed. \nException: {}", e.getMessage());
            throw e;
        }

        return rowsDeleted == 1;
    }

    /**
     *
     * @param id the ID of the playlist to check
     * @param songId the ID of the song to look for in the playlist
     * @return true if the song exists in the playlist, false otherwise
     * @throws SQLException if a database access error occurs or the SQL query fails
     */

    @Override
    public boolean isSongInPlaylist(int id, int songId) throws SQLException {
        Connection conn = connector.getConnection();
        if (conn == null) {
            throw new SQLException("isSongInPlaylist(): Could not establish connection.");
        }

        String sql = "SELECT COUNT(*) AS count FROM playlistSongs WHERE playlistId = ? AND songId = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, songId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count") > 0;
                }
            } catch (SQLException e) {
                log.error("isSongInPlaylist(): Issue processing resultset. \nException: {}", e.getMessage());
                throw e;
            }

        } catch (SQLException e) {
            log.error("isSongInPlaylist() - SQL could not be prepared. \nException: {}", e.getMessage());
            throw e;
        }

        return false;
    }

    /**
     * Get all playlists that contain a specific song
     *
     * @param songId The ID of the song
     * @return List of playlist IDs containing the song
     * @throws SQLException if database error occurs
     */
    @Override
    public List<Integer> getPlaylistsContainingSong(int songId) throws SQLException {
        Connection conn = connector.getConnection();
        if (conn == null) {
            throw new SQLException("getPlaylistsContainingSong(): Could not establish connection.");
        }

        ArrayList<Integer> playlistIds = new ArrayList<>();

        String sql = "SELECT playlistId FROM playlistSongs WHERE songId = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, songId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    playlistIds.add(rs.getInt("playlist_id"));
                }
            } catch (SQLException e) {
                log.error("getPlaylistsContainingSong(): Error processing resultset. \nException: {}", e.getMessage());
                throw e;
            }

        } catch (SQLException e) {
            log.error("getPlaylistsContainingSong() - SQL could not be prepared. \nException: {}", e.getMessage());
            throw e;
        }

        return playlistIds;
    }

    /**
     * Delete all songs from a playlist
     *
     * @param id The ID of the playlist
     * @return Number of songs deleted
     * @throws SQLException if database error occurs
     */
    @Override
    public int deleteAllSongsFromPlaylist(int id) throws SQLException {
        Connection conn = connector.getConnection();
        if (conn == null) {
            throw new SQLException("deleteAllSongsFromPlaylist(): Could not establish connection.");
        }

        int deletedRows = 0;

        String sql = "DELETE FROM playlistSongs WHERE playlistId = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            deletedRows = ps.executeUpdate();

        } catch (SQLException e) {
            log.error("deleteAllSongsFromPlaylist() - SQL failed. \nException: {}", e.getMessage());
            throw e;
        }

        return deletedRows;
    }


    private static PlaylistsSongs mapPlaylistSongRow(ResultSet rs) throws SQLException {
        return PlaylistsSongs.builder()
                .id(rs.getInt("id"))
                .playlistId(rs.getInt("playlist_id"))
                .songId(rs.getInt("song_id"))
                .build();
    }
}
