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
        public Playlists create(Playlists playlists) throws SQLException;
        public List<Playlists> findAll() throws SQLException;
        public List<Playlists> findByUserId(int userId) throws SQLException;
        public List<Playlists> getVisiblePlaylists(int userId) throws SQLException;
        public boolean update(Playlists playlists) throws SQLException;
        public boolean delete(int id) throws SQLException;
    }


