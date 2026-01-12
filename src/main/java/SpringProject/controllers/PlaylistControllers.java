package SpringProject.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import SpringProject.entities.Playlists;
import SpringProject.entities.PlaylistsSongs;
import SpringProject.services.PlaylistServices;

import jakarta.validation.Valid;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/playlists")
public class PlaylistControllers {

    // Exception error codes
    private static final int DUPLICATE_KEY_ERROR_CODE = 1062;
    private static final int FOREIGN_KEY_CONSTRAINT_FAILS = 1452;

    private final PlaylistServices playlistService;

    public PlaylistControllers(PlaylistServices playlistService) {
        this.playlistService = playlistService;
    }

    /**
     * Get all playlists
     */
    @GetMapping(path = "/all", produces = "application/json")
    public List<Playlists> getAllPlaylists() {
        try {
            return playlistService.getAllPlaylists();
        } catch (SQLException e) {
            log.error("Playlist list could not be retrieved. Database error occurred: {}",
                    e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Database error"
            );
        }
    }

    /**
     * Get playlists by user ID
     */
    @GetMapping(path = "/user/{userId}", produces = "application/json")
    public List<Playlists> getUserPlaylists(@PathVariable int userId) {
        try {
            return playlistService.getUserPlaylists(userId);
        } catch (IllegalArgumentException e) {
            log.error("Invalid user ID: {}", e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage()
            );
        } catch (SQLException e) {
            log.error("User playlists could not be retrieved. Database error occurred: {}",
                    e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Database error"
            );
        }
    }

    /**
     * Get visible playlists for a user
     */
    @GetMapping(path = "/visible/{userId}", produces = "application/json")
    public List<Playlists> getVisiblePlaylists(@PathVariable int userId) {
        try {
            return playlistService.getVisiblePlaylists(userId);
        } catch (IllegalArgumentException e) {
            log.error("Invalid user ID: {}", e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage()
            );
        } catch (SQLException e) {
            log.error("Visible playlists could not be retrieved. Database error occurred: {}",
                    e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Database error"
            );
        }
    }

    /**
     * Create a new playlist
     */
    @PostMapping(path = "/add", produces = "application/json")
    public Playlists createPlaylist(@Valid @RequestBody Playlists playlist) {
        try {
            return playlistService.createPlaylists(playlist);
        } catch (IllegalArgumentException e) {
            log.error("Failed to create playlist. Validation error: {}", e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage()
            );
        } catch (SQLException e) {
            log.error("Failed to add playlist with name \"{}\". Database error occurred: {}",
                    playlist.getName(), e.getMessage());
            if (e.getErrorCode() == DUPLICATE_KEY_ERROR_CODE) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT, "Playlist already exists"
                );
            } else if (e.getErrorCode() == FOREIGN_KEY_CONSTRAINT_FAILS) {
                throw new ResponseStatusException(
                        HttpStatus.UNPROCESSABLE_ENTITY, "Foreign key constraint failed"
                );
            } else {
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR, "Database error occurred"
                );
            }
        }
    }

    /**
     * Update a playlist
     * Requires requestingUserId in request header
     */
    @PutMapping(path = "/{playlistId}", produces = "application/json")
    public boolean updatePlaylist(
            @PathVariable int playlistId,
            @Valid @RequestBody Playlists playlist,
            @RequestHeader("X-User-Id") int requestingUserId) {
        try {
            playlist.setId(playlistId);
            boolean updated = playlistService.updatePlaylists(playlist, requestingUserId);
            if (!updated) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Playlist not found"
                );
            }
            return true;
        } catch (IllegalArgumentException e) {
            log.error("Failed to update playlist. Validation error: {}", e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage()
            );
        } catch (IllegalStateException e) {
            log.error("Failed to update playlist. Authorization error: {}", e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, e.getMessage()
            );
        } catch (SQLException e) {
            log.error("Failed to update playlist with ID \"{}\". Database error occurred: {}",
                    playlistId, e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Database error occurred"
            );
        }
    }

    /**
     * Delete a playlist
     * Requires requestingUserId in request header
     */
    @DeleteMapping(path = "/{playlistId}", produces = "application/json")
    public boolean deletePlaylist(
            @PathVariable int playlistId,
            @RequestHeader("X-User-Id") int requestingUserId) {
        try {
            boolean deleted = playlistService.deletePlaylist(playlistId, requestingUserId);
            if (!deleted) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Playlist not found"
                );
            }
            return true;
        } catch (IllegalArgumentException e) {
            log.error("Failed to delete playlist. Validation error: {}", e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage()
            );
        } catch (IllegalStateException e) {
            log.error("Failed to delete playlist. Authorization error: {}", e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, e.getMessage()
            );
        } catch (SQLException e) {
            log.error("Failed to delete playlist with ID \"{}\". Database error occurred: {}",
                    playlistId, e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Database error occurred"
            );
        }
    }

    /**
     * Add a song to a playlist
     * Requires requestingUserId in request header
     */
    @PostMapping(path = "/{playlistId}/songs/{songId}", produces = "application/json")
    public PlaylistsSongs addSongToPlaylist(
            @PathVariable int playlistId,
            @PathVariable int songId,
            @RequestHeader("X-User-Id") int requestingUserId) {
        try {
            return playlistService.addSongToPlaylist(playlistId, songId, requestingUserId);
        } catch (IllegalArgumentException e) {
            log.error("Failed to add song to playlist. Validation error: {}", e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage()
            );
        } catch (IllegalStateException e) {
            log.error("Failed to add song to playlist. Authorization/State error: {}", e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, e.getMessage()
            );
        } catch (SQLException e) {
            log.error("Failed to add song {} to playlist {}. Database error occurred: {}",
                    songId, playlistId, e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Database error occurred"
            );
        }
    }

    /**
     * Get all songs in a playlist
     */
    @GetMapping(path = "/{playlistId}/songs", produces = "application/json")
    public List<PlaylistsSongs> getPlaylistSongs(@PathVariable int playlistId) {
        try {
            return playlistService.getPlaylistSongs(playlistId);
        } catch (IllegalArgumentException e) {
            log.error("Invalid playlist ID: {}", e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage()
            );
        } catch (SQLException e) {
            log.error("Playlist songs could not be retrieved. Database error occurred: {}",
                    e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Database error"
            );
        }
    }

    /**
     * Remove a song from a playlist
     * Requires requestingUserId in request header
     */
    @DeleteMapping(path = "/{playlistId}/songs/{songId}", produces = "application/json")
    public boolean removeSongFromPlaylist(
            @PathVariable int playlistId,
            @PathVariable int songId,
            @RequestHeader("X-User-Id") int requestingUserId) {
        try {
            return playlistService.removeSongFromPlaylist(playlistId, songId, requestingUserId);
        } catch (IllegalArgumentException e) {
            log.error("Failed to remove song. Validation error: {}", e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage()
            );
        } catch (IllegalStateException e) {
            log.error("Failed to remove song. Authorization error: {}", e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, e.getMessage()
            );
        } catch (SQLException e) {
            log.error("Failed to remove song {} from playlist {}. Database error occurred: {}",
                    songId, playlistId, e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Database error occurred"
            );
        }
    }

    /**
     * Check if a song is in a playlist
     */
    @GetMapping(path = "/{playlistId}/songs/{songId}/exists", produces = "application/json")
    public boolean isSongInPlaylist(
            @PathVariable int playlistId,
            @PathVariable int songId) {
        try {
            return playlistService.isSongInPlaylist(playlistId, songId);
        } catch (SQLException e) {
            log.error("Could not check if song is in playlist. Database error occurred: {}",
                    e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Database error"
            );
        }
    }

    /**
     * Get all playlists containing a specific song
     */
    @GetMapping(path = "/containing-song/{songId}", produces = "application/json")
    public List<Integer> getPlaylistsContainingSong(@PathVariable int songId) {
        try {
            return playlistService.getPlaylistsContainingSong(songId);
        } catch (IllegalArgumentException e) {
            log.error("Invalid song ID: {}", e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage()
            );
        } catch (SQLException e) {
            log.error("Could not retrieve playlists containing song. Database error occurred: {}",
                    e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Database error"
            );
        }
    }

    /**
     * Delete all songs from a playlist
     * Requires requestingUserId in request header
     */
    @DeleteMapping(path = "/{playlistId}/songs", produces = "application/json")
    public int deleteAllSongsFromPlaylist(
            @PathVariable int playlistId,
            @RequestHeader("X-User-Id") int requestingUserId) {
        try {
            // Verify ownership before deleting all songs
            if (!playlistService.userOwnsPlaylist(playlistId, requestingUserId)) {
                throw new IllegalStateException("You can only delete songs from your own playlists");
            }
            return playlistService.deleteAllSongsFromPlaylist(playlistId);
        } catch (IllegalArgumentException e) {
            log.error("Invalid playlist ID: {}", e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage()
            );
        } catch (IllegalStateException e) {
            log.error("Authorization error: {}", e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, e.getMessage()
            );
        } catch (SQLException e) {
            log.error("Could not delete songs from playlist. Database error occurred: {}",
                    e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Database error"
            );
        }
    }
}
