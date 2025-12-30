package SpringProject.persistences;
import SpringProject.entities.PlaylistsSongs;

import java.sql.SQLException;
import java.util.List;



/**
 * Data Access Object interface for PlaylistSong operations
 * Handles adding and retrieving songs in playlists
 *
 */
public interface PlaylistSongDao {
    void closeConnection();
    PlaylistsSongs addSongToPlaylist(PlaylistsSongs playlistSong) throws SQLException;
    List<PlaylistsSongs> getSongsByPlaylistId(int id) throws SQLException;
    boolean removeSongFromPlaylist(int id, int songId) throws SQLException;
    boolean isSongInPlaylist(int id, int songId) throws SQLException;
    List<Integer> getPlaylistsContainingSong(int songId) throws SQLException;
    int deleteAllSongsFromPlaylist(int id) throws SQLException;
}
