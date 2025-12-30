package SpringProject.services;

import lombok.extern.slf4j.Slf4j;

import SpringProject.entities.Playlists;
import SpringProject.entities.PlaylistsSongs;
import SpringProject.persistences.PlaylistDao;
import SpringProject.persistences.PlaylistSongDao;

import java.sql.SQLException;
import java.util.List;


/* @author [Maryam]*/

@Slf4j
public class PlaylistServices {
    private PlaylistDao playlistDao;
    private PlaylistSongDao playlistSongDao;

    public PlaylistServices(PlaylistDao playlistDao, PlaylistSongDao playlistSongDao) {
        this.playlistDao = playlistDao;
        this.playlistSongDao = playlistSongDao;
    }

    public void shutdownServices() {
        playlistDao.closeConnection();
        playlistSongDao.closeConnection();
    }

    /**
     * @param playlists The playlists to create
     * @return Created playlists with generated ID
     * @throws SQLException             if database error occurs
     * @throws IllegalArgumentException if playlist data is invalid
     */
    public Playlists createPlaylists(Playlists playlists) throws SQLException {
        if (playlists == null) {
            throw new IllegalArgumentException("Playlists cannot be null");
        }
        if (playlists.getUserId() <= 0) {
            throw new IllegalArgumentException("User iD must be provided");
        }
        if (playlists.getName() == null || playlists.getName().isBlank()) {
            throw new IllegalArgumentException("Playlist name must be provided");
        }
        log.info("Creating playlist '{}' for user {}", playlists.getName(), playlists.getUserId());
        return playlistDao.create(playlists);
    }

    /**
     * @return List of all playlists
     * @throws SQLException if database access error occurs
     */
    public List<Playlists> getAllPlaylists() throws SQLException {
        log.info("Retrieving all Playlists");
        return playlistDao.findAll();
    }

    /**
     * @param userId the  ID of the user
     * @return All playlists associated with that user
     * @throws SQLException if database access error occurs
     */
    public List<Playlists> getUserPlaylists(int userId) throws SQLException {
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID must be positive");
        }
        log.info("Retrieving all playlists for user '{}' ", userId);
        return playlistDao.findByUserId(userId);
    }

    /**
     * @param userId The userId assocated with this playlist
     * @return The playlists only visible to this particular ID
     * @throws SQLException if database access occurs
     */
    public List<Playlists> getVisiblePlaylists(int userId) throws SQLException {
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID must be positive");
        }
        log.info("Retrieving Playlists visible only to user '{}' ", userId);
        return playlistDao.getVisiblePlaylists(userId);
    }

    /**
     * @param playlists        The playlist with updated information
     * @param requestingUserId The ID of the user making the rewuest
     * @return true if the update was successful
     * @throws SQLException          if database error occurs
     * @throws IllegalStateException if user doesnt own the palylist
     */
    public boolean updatePlaylists(Playlists playlists, int requestingUserId) throws SQLException {
        if (playlists == null) {
            throw new IllegalArgumentException("Playlists cannot be null");
        }
        if (playlists.getId() <= 0) {
            throw new IllegalArgumentException("Playlist ID must be provided");
        }
        if (playlists.getUserId() != requestingUserId) {
            log.warn("User '{}' attempted to update playlists '{}' owned by user '{}' ", requestingUserId, playlists.getId(), playlists.getUserId());
            throw new IllegalStateException("You can only update your own playlist");
        }
        log.info("Updating playlist {} for user {}", playlists.getId(), requestingUserId);
        return playlistDao.update(playlists);
    }

    /**
     * @param playlistId       The ID of the playlist to delete
     * @param requestingUserId The ID of the user making the request
     * @return true if deletion successful
     * @throws SQLException          if database access error occurs
     * @throws IllegalStateException if user doesn't own the playlist
     */
    public boolean deletePlaylist(int playlistId, int requestingUserId) throws SQLException {
        if (playlistId <= 0) {
            throw new IllegalArgumentException("Playlist ID must be positive");
        }
        if (requestingUserId <= 0) {
            throw new IllegalArgumentException("Requesting user ID must be provided");
        }
        //Check ownership
        if (!userOwnsPlaylist(playlistId, requestingUserId)) {
            log.warn("User {} attempted to delete playlist {} they don't own",
                    requestingUserId, playlistId);
            throw new IllegalStateException("You can only delete your own playlists");
        }

        log.info("Deleting playlist {} for user {}", playlistId, requestingUserId);
        return playlistDao.delete(playlistId);
    }

    /**
     * Adds a song to a playlist
     * Validates ownership for private playlists
     *
     * @param playlistId       The ID of the playlist
     * @param songId           The ID of the song to add
     * @param requestingUserId The ID of the user making the request
     * @return Created PlaylistsSongs object
     * @throws SQLException          if database access error occurs
     * @throws IllegalStateException if user doesn't have permission
     */
    public PlaylistsSongs addSongToPlaylist(int playlistId, int songId, int requestingUserId) throws SQLException {
        if (playlistId <= 0) {
            throw new IllegalArgumentException("Playlist ID must be positive");
        }
        if (songId <= 0) {
            throw new IllegalArgumentException("Song ID must be positive");
        }
        if (requestingUserId <= 0) {
            throw new IllegalArgumentException("Requesting user ID must be provided");
        }

        // Get the playlist to check if it's private
        List<Playlists> allPlaylists = playlistDao.findAll();
        Playlists targetPlaylist = null;

        for (Playlists playlist : allPlaylists) {
            if (playlist.getId() == playlistId) {
                targetPlaylist = playlist;
                break;
            }
        }

        if (targetPlaylist == null) {
            throw new IllegalArgumentException("Playlist not found");
        }

        // If playlist is private, only owner can add songs
        if (!targetPlaylist.isPublic() && targetPlaylist.getUserId() != requestingUserId) {
            log.warn("User {} attempted to add song to private playlist {} owned by user {}",
                    requestingUserId, playlistId, targetPlaylist.getUserId());
            throw new IllegalStateException("You can only add songs to your own private playlists");
        }

        // Check if song already in playlist
        if (playlistSongDao.isSongInPlaylist(playlistId, songId)) {
            log.warn("Song {} is already in playlist {}", songId, playlistId);
            throw new IllegalStateException("Song is already in this playlist");
        }

        log.info("Adding song {} to playlist {} by user {}", songId, playlistId, requestingUserId);

        PlaylistsSongs playlistSong = new PlaylistsSongs();
        playlistSong.setPlaylistId(playlistId);
        playlistSong.setSongId(songId);

        return playlistSongDao.addSongToPlaylist(playlistSong);
    }

    /**
     * Gets all songs in a playlist
     *
     * @param playlistId The ID of the playlist
     * @return List of PlaylistsSongs objects
     * @throws SQLException if database access error occurs
     */
    public List<PlaylistsSongs> getPlaylistSongs(int playlistId) throws SQLException {
        if (playlistId <= 0) {
            throw new IllegalArgumentException("Playlist ID must be positive");
        }

        log.info("Retrieving songs for playlist {}", playlistId);
        return playlistSongDao.getSongsByPlaylistId(playlistId);
    }

    /**
     * Removes a song from a playlist
     * Validates ownership for private playlists
     *
     * @param playlistId       The ID of the playlist
     * @param songId           The ID of the song to remove
     * @param requestingUserId The ID of the user making the request
     * @return true if removal successful
     * @throws SQLException          if database access error occurs
     * @throws IllegalStateException if user doesn't have permission
     */
    public boolean removeSongFromPlaylist(int playlistId, int songId, int requestingUserId) throws SQLException {
        if (playlistId <= 0) {
            throw new IllegalArgumentException("Playlist ID must be positive");
        }
        if (songId <= 0) {
            throw new IllegalArgumentException("Song ID must be positive");
        }
        if (requestingUserId <= 0) {
            throw new IllegalArgumentException("Requesting user ID must be provided");
        }

        // Check ownership for private playlists
        if (!canModifyPlaylist(playlistId, requestingUserId)) {
            log.warn("User {} attempted to remove song from playlist {} without permission",
                    requestingUserId, playlistId);
            throw new IllegalStateException("You don't have permission to modify this playlist");
        }

        log.info("Removing song {} from playlist {} by user {}", songId, playlistId, requestingUserId);
        return playlistSongDao.removeSongFromPlaylist(playlistId, songId);
    }

    /**
     * Checks if a song is in a playlist
     *
     * @param playlistId The ID of the playlist
     * @param songId     The ID of the song
     * @return true if song is in playlist
     * @throws SQLException if database access error occurs
     */
    public boolean isSongInPlaylist(int playlistId, int songId) throws SQLException {
        if (playlistId <= 0 || songId <= 0) {
            return false;
        }
        log.info("Checking if song {} is present in a playlist", songId);
        return playlistSongDao.isSongInPlaylist(playlistId, songId);
    }

    /**
     * @param songId The ID of the song
     * @return List of playlist IDs containing the song
     * @throws SQLException             if database access error occurs
     * @throws IllegalArgumentException if songId is invalid
     */
    public List<Integer> getPlaylistsContainingSong(int songId) throws SQLException {
        if (songId <= 0) {
            throw new IllegalArgumentException("Song ID must be positive");
        }
        log.info("Getting all the playlists containing that particular song");
        return playlistSongDao.getPlaylistsContainingSong(songId);
    }

    /**
     * Deletes all songs from a playlist
     *
     * @param playlistId The ID of the playlist
     * @return The number of songs deleted
     * @throws SQLException             if database access error occurs
     * @throws IllegalArgumentException if playlistId is invalid
     */
    public int deleteAllSongsFromPlaylist(int playlistId) throws SQLException {
        if (playlistId <= 0) {
            throw new IllegalArgumentException("Playlist ID must be positive");
        }
        log.info("Deleting all songs relating to playlists {} ", playlistId);
        return playlistSongDao.deleteAllSongsFromPlaylist(playlistId);
    }


    /**
     * Checks if a user owns a specific playlist
     *
     * @param playlistId The ID of the playlist
     * @param userId     The ID of the user
     * @return true if user owns the playlist
     * @throws SQLException if database access error occurs
     */
    public boolean userOwnsPlaylist(int playlistId, int userId) throws SQLException {
        if (playlistId <= 0 || userId <= 0) {
            return false;
        }

        List<Playlists> userPlaylists = playlistDao.findByUserId(userId);

        for (Playlists playlist : userPlaylists) {
            if (playlist.getId() == playlistId) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if a user can modify a playlist (owner or public)
     *
     * @param playlistId The ID of the playlist
     * @param userId     The ID of the user
     * @return true if user can modify the playlist
     * @throws SQLException if database access error occurs
     */
    private boolean canModifyPlaylist(int playlistId, int userId) throws SQLException {
        List<Playlists> allPlaylists = playlistDao.findAll();

        for (Playlists playlist : allPlaylists) {
            if (playlist.getId() == playlistId) {
                // Can modify if owner OR if playlist is public
                return playlist.getUserId() == userId || playlist.isPublic();
            }
        }

        return false;
    }

}
