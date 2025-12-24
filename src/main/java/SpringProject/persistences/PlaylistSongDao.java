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

    /**
     * Add a song to a playlist
     *
     * @param playlistSong The playlist-song relationship to create
     * @return The created PlaylistSongDto with generated ID, or null if failed
     * @throws SQLException if database error occurs
     */
    PlaylistsSongs addSongToPlaylist(PlaylistsSongs playlistSong) throws SQLException;

    /**
     * Get all songs in a specific playlist
     *
     * @param id The ID of the playlist
     * @return List of PlaylistSongDto objects, empty list if none found
     * @throws SQLException if database error occurs
     */
    List<PlaylistsSongs> getSongsByPlaylistId(int id) throws SQLException;

    /**
     * Remove a song from a playlist
     *
     * @param id The ID of the playlist
     * @param songId The ID of the song to remove
     * @return true if deleted successfully, false otherwise
     * @throws SQLException if database error occurs
     */
    boolean removeSongFromPlaylist(int id, int songId) throws SQLException;

    /**
     * Check if a song already exists in a playlist
     *
     * @param id The ID of the playlist
     * @param songId The ID of the song
     * @return true if song is in playlist, false otherwise
     * @throws SQLException if database error occurs
     */
    boolean isSongInPlaylist(int id, int songId) throws SQLException;

    /**
     * Get all playlists that contain a specific song
     *
     * @param songId The ID of the song
     * @return List of playlist IDs containing the song
     * @throws SQLException if database error occurs
     */
    List<Integer> getPlaylistsContainingSong(int songId) throws SQLException;

    /**
     * Delete all songs from a playlist
     *
     * @param id The ID of the playlist
     * @return Number of songs deleted
     * @throws SQLException if database error occurs
     */
    int deleteAllSongsFromPlaylist(int id) throws SQLException;
}
