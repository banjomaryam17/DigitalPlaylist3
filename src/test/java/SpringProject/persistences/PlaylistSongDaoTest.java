package SpringProject.persistences;

import SpringProject.entities.PlaylistsSongs;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author [Maryam]
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PlaylistSongDaoTest {

    private static Connector connector;
    private static PlaylistSongDao playlistSongDao;
    private static Integer testPlaylistSongId;
    private static final int TEST_PLAYLIST_ID = 1;
    private static final int TEST_SONG_ID = 1;
    private static final int TEST_SONG_ID_2 = 2;

    /**
     * Set up test database connection before all tests
     */
    @BeforeAll
    static void setupDatabase() {
        connector = new MySqlConnector("test_mac_database.properties");
        playlistSongDao = new PlaylistSongImpl(connector);
        System.out.println(" PlaylistSongDao Test ");
    }

    /**
     * Clean up after each test
     */
    @AfterEach
    void cleanupTestData() {
        System.out.println("Test completed");
    }

    /**
     * Close connection after all tests
     */
    @AfterAll
    static void closeConnection() {
        if (playlistSongDao != null) {
            playlistSongDao.closeConnection();
        }
        System.out.println(" PlaylistSongDao Test Completed ");
    }
    /**
     * Test adding a song to a playlist
     * successfully create the relationship and return object with ID
     */
    @Test
    @Order(1)
    void testAddSongToPlaylist_Success() throws SQLException {
        System.out.println("Test: Add Song to Playlist");

        PlaylistsSongs playlistSong = PlaylistsSongs.builder()
                .playlistId(TEST_PLAYLIST_ID)
                .songId(TEST_SONG_ID)
                .positionInPlaylist(1)
                .build();
        PlaylistsSongs created = playlistSongDao.addSongToPlaylist(playlistSong);

        assertNotNull(created, "Created playlist-song relationship should not be null");
        assertNotNull(created.getId(), "Should have generated ID");
        assertEquals(TEST_PLAYLIST_ID, created.getPlaylistId(), "Playlist ID should match");
        assertEquals(TEST_SONG_ID, created.getSongId(), "Song ID should match");
        // Save ID for other tests
        testPlaylistSongId = created.getId();

        System.out.println(" Song added to playlist with ID: " + created.getId());
    }

    /**
     * Test adding duplicate song to same playlist
     * Should throw SQLException due to UNIQUE constraint
     */
    @Test
    @Order(2)
    void testAddSongToPlaylist_Duplicate() {
        System.out.println("Test: Add Song to Playlist - Duplicate");
        //Same song and playlist as test 1
        PlaylistsSongs duplicate = PlaylistsSongs.builder()
                .playlistId(TEST_PLAYLIST_ID)
                .songId(TEST_SONG_ID)
                .positionInPlaylist(2)
                .build();

        assertThrows(SQLException.class, () -> {
            playlistSongDao.addSongToPlaylist(duplicate);
        }, "Should throw SQLException for duplicate playlist-song combination");

        System.out.println(" Correctly rejected duplicate song in playlist");
    }

    /**
     * Test adding multiple different songs to same playlist
     * Should successfully add multiple songs
     */
    @Test
    @Order(3)
    void testAddSongToPlaylist_MultipleSongs() throws SQLException {
        System.out.println("Test: Add Song to Playlist - Multiple Songs");

        PlaylistsSongs song2 = PlaylistsSongs.builder()
                .playlistId(TEST_PLAYLIST_ID)
                .songId(TEST_SONG_ID_2)
                .positionInPlaylist(2)
                .build();

        PlaylistsSongs created = playlistSongDao.addSongToPlaylist(song2);

        assertNotNull(created);
        assertEquals(TEST_SONG_ID_2, created.getSongId());

        System.out.println("Successfully added multiple songs to playlist");
    }

    /**
     * Test adding with invalid playlist ID
     * Should throw SQLException due to foreign key constraint
     */
    @Test
    @Order(4)
    void testAddSongToPlaylist_InvalidPlaylistId() {
        System.out.println("Test: Add Song to Playlist - Invalid Playlist ID");

        PlaylistsSongs invalid = PlaylistsSongs.builder()
                .playlistId(99999)
                .songId(TEST_SONG_ID)
                .positionInPlaylist(1)
                .build();

        assertThrows(SQLException.class, () -> {
            playlistSongDao.addSongToPlaylist(invalid);
        }, "Should throw SQLException for invalid playlist ID");

        System.out.println("Correctly rejected invalid playlist ID");
    }

    /**
     * Test adding with invalid song ID
     * Should throw SQLException due to foreign key constraint
     */
    @Test
    @Order(5)
    void testAddSongToPlaylist_InvalidSongId() {
        System.out.println("Test: Add Song to Playlist - Invalid Song ID");

        PlaylistsSongs invalid = PlaylistsSongs.builder()
                .playlistId(TEST_PLAYLIST_ID)
                .songId(99999)
                .positionInPlaylist(1)
                .build();

        assertThrows(SQLException.class, () -> {
            playlistSongDao.addSongToPlaylist(invalid);
        }, "Should throw SQLException for invalid song ID");

        System.out.println("Correctly rejected invalid song ID");
    }

    /**
     * Test getting all songs in a playlist
     * Should return list of songs in the playlist
     */
    @Test
    @Order(6)
    void testGetSongsByPlaylistId_Success() throws SQLException {
        System.out.println("Test: Get Songs by Playlist ID - Success");

        List<PlaylistsSongs> songs = playlistSongDao.getSongsByPlaylistId(TEST_PLAYLIST_ID);

        assertNotNull(songs, "Songs list should not be null");
        assertTrue(songs.size() >= 2, "Should have at least 2 songs from previous tests");

        // Verify all songs belong to the correct playlist
        for (PlaylistsSongs song : songs) {
            assertEquals(TEST_PLAYLIST_ID, song.getPlaylistId(),
                    "All songs should belong to playlist " + TEST_PLAYLIST_ID);
        }

        System.out.println("Retrieved " + songs.size() + " songs from playlist");
    }

    /**
     * Test getting songs from empty playlist
     * Should return empty list
     */
    @Test
    @Order(7)
    void testGetSongsByPlaylistId_EmptyPlaylist() throws SQLException {
        System.out.println("Test: Get Songs by Playlist ID - Empty Playlist");
            // a playlist that has no songs
        List<PlaylistsSongs> songs = playlistSongDao.getSongsByPlaylistId(99);

        assertNotNull(songs, "Should return a list (not null)");
        assertTrue(songs.isEmpty(), "Should return empty list for playlist with no songs");

        System.out.println("Correctly returned empty list for empty playlist");
    }

    /**
     * Test getting songs with invalid playlist ID
     * Should return empty list
     */
    @Test
    @Order(8)
    void testGetSongsByPlaylistId_InvalidId() throws SQLException {
        System.out.println("Test: Get Songs by Playlist ID - Invalid ID");
        List<PlaylistsSongs> songs = playlistSongDao.getSongsByPlaylistId(-1);
        assertNotNull(songs, "Should return a list (not null)");
        assertTrue(songs.isEmpty(), "Should return empty list for invalid ID");

        System.out.println("Correctly handled invalid playlist ID");
    }

    /**
     * Test checking if a song exists in a playlist
     * Should return true for existing song
     */
    @Test
    @Order(9)
    void testIsSongInPlaylist_Exists() throws SQLException {
        System.out.println("Test: Is Song in Playlist - Exists");
        boolean exists = playlistSongDao.isSongInPlaylist(TEST_PLAYLIST_ID, TEST_SONG_ID);

        assertTrue(exists, "Song should exist in playlist");
        System.out.println("Correctly identified song exists in playlist");
    }

    /**
     * Test checking if a song doesn't exist in a playlist
     * Should return false
     */
    @Test
    @Order(10)
    void testIsSongInPlaylist_NotExists() throws SQLException {
        System.out.println("Test: Is Song in Playlist - Not Exists");
        boolean exists = playlistSongDao.isSongInPlaylist(TEST_PLAYLIST_ID, 999);
        assertFalse(exists, "Song should not exist in playlist");
        System.out.println("Correctly identified song doesn't exist in playlist");
    }

    /**
     * Test checking with invalid IDs
     * Should return false
     */
    @Test
    @Order(11)
    void testIsSongInPlaylist_InvalidIds() throws SQLException {
        System.out.println("Test: Is Song in Playlist - Invalid IDs");
        assertFalse(playlistSongDao.isSongInPlaylist(-1, TEST_SONG_ID),
                "Should return false for invalid playlist ID");
        assertFalse(playlistSongDao.isSongInPlaylist(TEST_PLAYLIST_ID, -1),
                "Should return false for invalid song ID");
        assertFalse(playlistSongDao.isSongInPlaylist(0, 0),
                "Should return false for zero IDs");

        System.out.println("Correctly handled invalid IDs");
    }

    /**
     * Test getting all playlists that contain a specific song
     * Should return list of playlist IDs
     */
    @Test
    @Order(12)
    void testGetPlaylistsContainingSong_Success() throws SQLException {
        System.out.println("Test: Get Playlists Containing Song - Success");
        List<Integer> playlistIds = playlistSongDao.getPlaylistsContainingSong(TEST_SONG_ID);

        assertNotNull(playlistIds, "Playlist IDs list should not be null");
        assertTrue(playlistIds.size() > 0, "Should find at least one playlist containing the song");
        assertTrue(playlistIds.contains(TEST_PLAYLIST_ID),
                "Should include the test playlist");

        System.out.println("Found " + playlistIds.size() + " playlists containing song " + TEST_SONG_ID);
    }

    /**
     * Test getting playlists for a song that's not in any playlist
     * Should return empty list
     */
    @Test
    @Order(13)
    void testGetPlaylistsContainingSong_NoPlaylists() throws SQLException {
        System.out.println("Test: Get Playlists Containing Song - No Playlists");

        List<Integer> playlistIds = playlistSongDao.getPlaylistsContainingSong(999);

        assertNotNull(playlistIds, "Should return a list (not null)");
        assertTrue(playlistIds.isEmpty(), "Should return empty list for song not in any playlist");

        System.out.println(" Correctly returned empty list for song not in playlists");
    }

    /**
     * Test removing a song from a playlist
     * Should successfully remove and return true
     */
    @Test
    @Order(14)
    void testRemoveSongFromPlaylist_Success() throws SQLException {
        System.out.println("Test: Remove Song from Playlist - Success");
        boolean removed = playlistSongDao.removeSongFromPlaylist(TEST_PLAYLIST_ID, TEST_SONG_ID_2);

        assertTrue(removed, "Remove should return true");

        //To verify if it's removed
        boolean stillExists = playlistSongDao.isSongInPlaylist(TEST_PLAYLIST_ID, TEST_SONG_ID_2);
        assertFalse(stillExists, "Song should no longer be in playlist");

        System.out.println("Successfully removed song from playlist");
    }

    /**
     * Test removing a song that's not in the playlist
     * Should return false
     */
    @Test
    @Order(15)
    void testRemoveSongFromPlaylist_NotExists() throws SQLException {
        System.out.println("Test: Remove Song from Playlist - Not Exists");

        boolean removed = playlistSongDao.removeSongFromPlaylist(TEST_PLAYLIST_ID, 999);

        assertFalse(removed, "Remove should return false for non-existent song");

        System.out.println("Correctly returned false for non-existent song");
    }

    /**
     * Test removing with invalid IDs
     * Should return false
     */
    @Test
    @Order(16)
    void testRemoveSongFromPlaylist_InvalidIds() throws SQLException {
        System.out.println("Test: Remove Song from Playlist - Invalid IDs");

        assertFalse(playlistSongDao.removeSongFromPlaylist(-1, TEST_SONG_ID),
                "Should return false for invalid playlist ID");
        assertFalse(playlistSongDao.removeSongFromPlaylist(TEST_PLAYLIST_ID, -1),
                "Should return false for invalid song ID");

        System.out.println("Correctly handled invalid IDs");
    }

    /**
     * Test deleting all songs from a playlist
     * Should return count of deleted songs
     */
    @Test
    @Order(17)
    void testDeleteAllSongsFromPlaylist_Success() throws SQLException {
        System.out.println("Test: Delete All Songs from Playlist - Success");

        // Add a few songs first
        PlaylistsSongs song3 = PlaylistsSongs.builder()
                .playlistId(TEST_PLAYLIST_ID)
                .songId(3)
                .positionInPlaylist(3)
                .build();
        playlistSongDao.addSongToPlaylist(song3);

        // Act
        int deletedCount = playlistSongDao.deleteAllSongsFromPlaylist(TEST_PLAYLIST_ID);

        // Assert
        assertTrue(deletedCount > 0, "Should delete at least one song");

        // Verify all songs are deleted
        List<PlaylistsSongs> remainingSongs = playlistSongDao.getSongsByPlaylistId(TEST_PLAYLIST_ID);
        assertTrue(remainingSongs.isEmpty(), "Playlist should have no songs");

        System.out.println("Deleted " + deletedCount + " songs from playlist");
    }

    /**
     * Test deleting from empty playlist
     * Should return 0
     */
    @Test
    @Order(18)
    void testDeleteAllSongsFromPlaylist_EmptyPlaylist() throws SQLException {
        System.out.println("Test: Delete All Songs from Playlist - Empty Playlist");

        int deletedCount = playlistSongDao.deleteAllSongsFromPlaylist(TEST_PLAYLIST_ID);

        assertEquals(0, deletedCount, "Should return 0 for empty playlist");

        System.out.println("Correctly returned 0 for empty playlist");
    }

    /**
     * Test deleting from non-existent playlist
     * Should return 0
     */
    @Test
    @Order(19)
    void testDeleteAllSongsFromPlaylist_NonExistent() throws SQLException {
        System.out.println("Test: Delete All Songs from Playlist - Non-Existent");

        int deletedCount = playlistSongDao.deleteAllSongsFromPlaylist(99999);
        assertEquals(0, deletedCount, "Should return 0 for non-existent playlist");

        System.out.println("Correctly returned 0 for non-existent playlist");
    }

    /**
     * Test complete workflow: Add, Check, Get, Remove
     */
    @Test
    @Order(20)
    void testCompleteWorkflow() throws SQLException {
        System.out.println("Test: Complete Workflow");

        // ADD
        PlaylistsSongs added = playlistSongDao.addSongToPlaylist(
                PlaylistsSongs.builder()
                        .playlistId(TEST_PLAYLIST_ID)
                        .songId(TEST_SONG_ID)
                        .positionInPlaylist(1)
                        .build()
        );
        assertNotNull(added);
        System.out.println("  ✓ Added song to playlist");

        // CHECK
        boolean exists = playlistSongDao.isSongInPlaylist(TEST_PLAYLIST_ID, TEST_SONG_ID);
        assertTrue(exists);
        System.out.println("  ✓ Confirmed song exists in playlist");

        // GET
        List<PlaylistsSongs> songs = playlistSongDao.getSongsByPlaylistId(TEST_PLAYLIST_ID);
        assertTrue(songs.size() > 0);
        System.out.println("  ✓ Retrieved songs from playlist");

        // REMOVE
        boolean removed = playlistSongDao.removeSongFromPlaylist(TEST_PLAYLIST_ID, TEST_SONG_ID);
        assertTrue(removed);

        boolean stillExists = playlistSongDao.isSongInPlaylist(TEST_PLAYLIST_ID, TEST_SONG_ID);
        assertFalse(stillExists);
        System.out.println(" Removed song from playlist");

        System.out.println(" Complete workflow successful");
    }
}