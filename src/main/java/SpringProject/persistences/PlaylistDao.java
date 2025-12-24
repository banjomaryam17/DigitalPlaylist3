package SpringProject.persistences;
import SpringProject.entities.Playlists;

import java.sql.SQLException;
import java.util.List;
    /**
     * Data Access Object interface for Playlist operations
     * Handles CRUD operations for playlists in the music library
     *
     */
    public interface PlaylistDao {
        void closeConnection();

        /**
         * Creates a new playlist in the database
         *
         * @param playlists The playlist object containing playlist details (name, description, user_id, is_public)
         * @return The created Playlists object with the generated ID, or null if creation failed
         * @throws SQLException if a database access error occurs or the SQL statement fails
         */
        public Playlists create(Playlists playlists) throws SQLException;

        /**
         * Retrieves all playlists from the database
         * This includes both public and private playlists from all users
         *
         * @return List of all Playlists objects in the database, empty list if none exist
         * @throws SQLException if a database access error occurs or the SQL query fails
         */
        public List<Playlists> findAll() throws SQLException;

        /**
         * Retrieves all playlists created by a specific user
         * Returns only playlists where the user is the owner
         *
         * @param userId The ID of the user whose playlists to retrieve
         * @return List of Playlists objects owned by the specified user, empty list if none found
         * @throws SQLException if a database access error occurs or the SQL query fails
         */
        public List<Playlists> findByUserId(int userId) throws SQLException;

        /**
         * Retrieves all playlists visible to a specific user
         * This includes:
         * - All playlists owned by the user (both public and private)
         * - All public playlists from other users
         *
         * @param userId The ID of the user for whom to retrieve visible playlists
         * @return List of Playlists objects visible to the user, empty list if none found
         * @throws SQLException if a database access error occurs or the SQL query fails
         */
        public List<Playlists> getVisiblePlaylists(int userId) throws SQLException;

        /**
         * Updates an existing playlist's information
         * Can update name, description, and public/private status
         * The playlist ID must be set in the playlists object
         *
         * @param playlists The playlist object containing updated information with a valid ID
         * @return true if the playlist was successfully updated, false if playlist not found or update failed
         * @throws SQLException if a database access error occurs or the SQL update statement fails
         */
        public boolean update(Playlists playlists) throws SQLException;

        /**
         * Deletes a playlist from the database
         * This will also delete all associated playlist_songs entries due to CASCADE constraint
         *
         * @param id The ID of the playlist to delete
         * @return true if the playlist was successfully deleted, false if playlist not found or deletion failed
         * @throws SQLException if a database access error occurs or the SQL delete statement fails
         */
        public boolean delete(int id) throws SQLException;
    }


