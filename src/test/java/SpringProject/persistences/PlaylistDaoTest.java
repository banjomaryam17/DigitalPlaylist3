package SpringProject.persistences;

import SpringProject.persistences.Connector;
import SpringProject.persistences.MySqlConnector;
import SpringProject.entities.Playlists;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for PlaylistDao
 * Tests all CRUD operations against the test database
 *
 * @author [Maryam]
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PlaylistDaoTest {

    private static Connector connector;
    private static PlaylistDao playlistDao;
    private static Integer testPlaylistId;
    private static final int TEST_USER_ID = 1;

    /**
     * Set up test database connection before all tests
     */
    @BeforeAll
    static void setupDatabase() {
        connector = new MySqlConnector("test_database.properties");
        playlistDao = new PlaylistDaoImpl(connector);
        System.out.println("=== PlaylistDao Test Suite Started ===");
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
        if (playlistDao != null) {
            playlistDao.closeConnection();
        }
        System.out.println("=== PlaylistDao Test Suite Completed ===");
    }

    // ========== CREATE TESTS ==========

    /**
     * Test creating a valid playlist
     * Should successfully create and return playlist with generated ID
     */
    @Test
    @Order(1)
    void testCreatePlaylist_Success() throws SQLException {
        System.out.println("Test: Create Playlist - Success");

        Playlists playlist = Playlists.builder()
                .userId(TEST_USER_ID)
                .name("Test Playlist " + System.currentTimeMillis())
                .description("A test playlist for unit testing")
                .isPublic(true)
                .build();

        Playlists created = playlistDao.create(playlist);

        assertNotNull(created, "Created playlist should not be null");
        assertTrue(created.getId() > 0, "Created playlist should have a generated ID");
        assertEquals(playlist.getName(), created.getName(), "Playlist name should match");
        assertEquals(playlist.getDescription(), created.getDescription(), "Description should match");
        assertEquals(playlist.getUserId(), created.getUserId(), "User ID should match");
        assertEquals(playlist.getIsPublic(), created.getIsPublic(), "isPublic should match");

        // ID for other tests
        testPlaylistId = created.getId();

        System.out.println("Playlist created with ID: " + created.getId());
    }

    /**
     * Test creating a playlist with null object
     * Should throw IllegalArgumentException
     */
    @Test
    @Order(2)
    void testCreatePlaylist_NullPlaylist() {
        System.out.println("Test: Create Playlist - Null Playlist");

        assertThrows(IllegalArgumentException.class, () -> {
            playlistDao.create(null);
        }, "Should throw IllegalArgumentException for null playlist");

        System.out.println(" Correctly rejected null playlist");
    }

    /**
     * Test creating a private playlist
     * Should successfully create with isPublic = false
     */
    @Test
    @Order(3)
    void testCreatePlaylist_PrivatePlaylist() throws SQLException {
        System.out.println("Test: Create Playlist - Private Playlist");

        Playlists playlist = Playlists.builder()
                .userId(TEST_USER_ID)
                .name("Private Playlist " + System.currentTimeMillis())
                .description("This is a private playlist")
                .isPublic(false)
                .build();

        Playlists created = playlistDao.create(playlist);

        assertNotNull(created, "Created playlist should not be null");
        assertFalse(created.getIsPublic(), "Playlist should be private");

        System.out.println("Private playlist created successfully");
    }

    /**
     * Test retrieving all playlists
     * Should return a list (empty or contain playlists)
     */
    @Test
    @Order(4)
    void testFindAll_ReturnsPlaylists() throws SQLException {
        System.out.println("Test: Find All - Returns Playlists");
        List<Playlists> playlists = playlistDao.findAll();

        assertNotNull(playlists, "Playlists list should not be null");
        assertTrue(playlists.size() >= 0, "Should return a valid list");
        assertTrue(playlists.size() > 0, "Should have at least the test playlists we created");

        System.out.println(" Retrieved " + playlists.size() + " playlists");

        //first few playlists for verification
        playlists.stream().limit(3).forEach(p ->
                System.out.println("  - " + p.getName() + " (User: " + p.getUserId() + ")")
        );
    }

    /**
     * Test finding playlists by user ID
     * Should return only playlists belonging to that user
     */
    @Test
    @Order(5)
    void testFindByUserId_ExistingUser() throws SQLException {
        System.out.println("Test: Find By User ID - Existing User");
        List<Playlists> userPlaylists = playlistDao.findByUserId(TEST_USER_ID);

        assertNotNull(userPlaylists, "User playlists should not be null");
        assertTrue(userPlaylists.size() > 0, "User should have at least one playlist");

        // Verify all playlists belong to the user
        for (Playlists playlist : userPlaylists) {
            assertEquals(TEST_USER_ID, playlist.getUserId(),
                    "All playlists should belong to user " + TEST_USER_ID);
        }

        System.out.println(" Found " + userPlaylists.size() + " playlists for user " + TEST_USER_ID);
    }

    /**
     * Test finding playlists for non-existent user
     * Should return empty list
     */
    @Test
    @Order(6)
    void testFindByUserId_NonExistentUser() throws SQLException {
        System.out.println("Test: Find By User ID - Non-Existent User");

        List<Playlists> playlists = playlistDao.findByUserId(99999);

        assertNotNull(playlists, "Should return a list (not null)");
        assertTrue(playlists.isEmpty(), "Should return empty list for non-existent user");

        System.out.println(" Correctly returned empty list for non-existent user");
    }

    /**
     * Test getting visible playlists for a user
     * Should return user's own playlists + all public playlists
     */
    @Test
    @Order(7)
    void testGetVisiblePlaylists_Success() throws SQLException {
        System.out.println("Test: Get Visible Playlists - Success");

        List<Playlists> visiblePlaylists = playlistDao.getVisiblePlaylists(TEST_USER_ID);

        assertNotNull(visiblePlaylists, "Visible playlists should not be null");
        assertTrue(visiblePlaylists.size() > 0, "Should have visible playlists");

        // Verify playlists are either owned by user or public
        for (Playlists playlist : visiblePlaylists) {
            boolean isOwnedOrPublic = playlist.getUserId().equals(TEST_USER_ID)
                    || playlist.getIsPublic();
            assertTrue(isOwnedOrPublic,
                    "Playlist '" + playlist.getName() + "' should be either owned by user or public");
        }

        System.out.println(" Found " + visiblePlaylists.size() + " visible playlists");
    }

    /**
     * Test visible playlists includes user's private playlists
     * Should include private playlists owned by the user
     */
    @Test
    @Order(8)
    void testGetVisiblePlaylists_IncludesOwnPrivate() throws SQLException {
        System.out.println("Test: Get Visible Playlists - Includes Own Private");

        //  Create a private playlist
        Playlists privatePlaylist = Playlists.builder()
                .userId(TEST_USER_ID)
                .name("Private Test " + System.currentTimeMillis())
                .description("My private playlist")
                .isPublic(false)
                .build();
        Playlists created = playlistDao.create(privatePlaylist);
        List<Playlists> visiblePlaylists = playlistDao.getVisiblePlaylists(TEST_USER_ID);

        boolean foundPrivate = visiblePlaylists.stream()
                .anyMatch(p -> p.getId().equals(created.getId()));

        assertTrue(foundPrivate, "Should include user's own private playlist");

        System.out.println("Correctly includes user's private playlists");
    }

    /**
     * Test updating an existing playlist
     * Should successfully update name, description, and isPublic
     */
    @Test
    @Order(9)
    void testUpdate_ExistingPlaylist() throws SQLException {
        System.out.println("Test: Update - Existing Playlist");

        // Arrange - Use the playlist created in first test
        Playlists updated = Playlists.builder()
                .id(testPlaylistId)
                .userId(TEST_USER_ID)
                .name("Updated Test Playlist")
                .description("Updated description")
                .isPublic(false)
                .build();

        boolean result = playlistDao.update(updated);

        assertTrue(result, "Update should return true");

        List<Playlists> playlists = playlistDao.findByUserId(TEST_USER_ID);
        Playlists found = playlists.stream()
                .filter(p -> p.getId().equals(testPlaylistId))
                .findFirst()
                .orElse(null);

        assertNotNull(found, "Updated playlist should be found");
        assertEquals("Updated Test Playlist", found.getName(), "Name should be updated");
        assertEquals("Updated description", found.getDescription(), "Description should be updated");
        assertFalse(found.getIsPublic(), "isPublic should be updated");

        System.out.println("Successfully updated playlist");
    }

    /**
     * Test updating a non-existent playlist
     * Should return false
     */
    @Test
    @Order(10)
    void testUpdate_NonExistentPlaylist() throws SQLException {
        System.out.println("Test: Update - Non-Existent Playlist");

        Playlists playlist = Playlists.builder()
                .id(99999)
                .userId(TEST_USER_ID)
                .name("Non-existent")
                .description("This playlist doesn't exist")
                .isPublic(true)
                .build();
        boolean updated = playlistDao.update(playlist);

        assertFalse(updated, "Update should return false for non-existent playlist");

        System.out.println("Correctly returned false for non-existent playlist");
    }

    /**
     * Test updating playlist with null object
     * Should throw IllegalArgumentException
     */
    @Test
    @Order(11)
    void testUpdate_NullPlaylist() {
        System.out.println("Test: Update - Null Playlist");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            playlistDao.update(null);
        }, "Should throw IllegalArgumentException for null playlist");

        System.out.println(" Correctly rejected null playlist");
    }

    /**
     * Test updating playlist with invalid ID (0 or negative)
     * Should throw IllegalArgumentException
     */
    @Test
    @Order(12)
    void testUpdate_InvalidId() {
        System.out.println("Test: Update - Invalid ID");

        // Arrange
        Playlists invalidPlaylist = Playlists.builder()
                .id(0)
                .userId(TEST_USER_ID)
                .name("Invalid")
                .description("Invalid ID")
                .isPublic(true)
                .build();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            playlistDao.update(invalidPlaylist);
        }, "Should throw IllegalArgumentException for invalid ID");

        System.out.println(" Correctly rejected invalid ID");
    }


    /**
     * Test deleting an existing playlist
     * Should successfully delete and return true
     */
    @Test
    @Order(13)
    void testDelete_ExistingPlaylist() throws SQLException {
        System.out.println("Test: Delete - Existing Playlist");

        // Arrange - Create a playlist to delete
        Playlists playlist = Playlists.builder()
                .userId(TEST_USER_ID)
                .name("Delete Test " + System.currentTimeMillis())
                .description("Will be deleted")
                .isPublic(true)
                .build();
        Playlists created = playlistDao.create(playlist);

        // Act
        boolean deleted = playlistDao.delete(created.getId());

        // Assert
        assertTrue(deleted, "Delete should return true");

        // Verify it's deleted
        List<Playlists> playlists = playlistDao.findByUserId(TEST_USER_ID);
        boolean exists = playlists.stream()
                .anyMatch(p -> p.getId().equals(created.getId()));

        assertFalse(exists, "Playlist should no longer exist");

        System.out.println(" Successfully deleted playlist");
    }

    /**
     * Test deleting a non-existent playlist
     * Should return false
     */
    @Test
    @Order(14)
    void testDelete_NonExistentPlaylist() throws SQLException {
        System.out.println("Test: Delete - Non-Existent Playlist");
        boolean deleted = playlistDao.delete(99999);
        assertFalse(deleted, "Delete should return false for non-existent playlist");

        System.out.println(" Correctly returned false for non-existent playlist");
    }

    /**
     * Test deleting with invalid ID
     * Should return false
     */
    @Test
    @Order(15)
    void testDelete_InvalidId() throws SQLException {
        System.out.println("Test: Delete - Invalid ID");
        boolean deleted = playlistDao.delete(-1);
        assertFalse(deleted, "Delete should return false for invalid ID");

        System.out.println("Correctly handled invalid ID");
    }

    /**
     * Test complete CRUD cycle
     * Create → Read → Update → Delete
     */
    @Test
    @Order(16)
    void testCompleteCRUDCycle() throws SQLException {
        System.out.println("Test: Complete CRUD Cycle");

        // CREATE
        Playlists playlist = Playlists.builder()
                .userId(TEST_USER_ID)
                .name("CRUD Test " + System.currentTimeMillis())
                .description("Testing full CRUD cycle")
                .isPublic(true)
                .build();
        Playlists created = playlistDao.create(playlist);
        assertNotNull(created);
        System.out.println("  ✓ Created playlist ID: " + created.getId());

        // READ
        List<Playlists> playlists = playlistDao.findByUserId(TEST_USER_ID);
        Playlists found = playlists.stream()
                .filter(p -> p.getId().equals(created.getId()))
                .findFirst()
                .orElse(null);
        assertNotNull(found);
        assertEquals(created.getName(), found.getName());
        System.out.println("  ✓ Found playlist: " + found.getName());

        // UPDATE
        found.setDescription("Updated description");
        found.setIsPublic(false);
        boolean updated = playlistDao.update(found);
        assertTrue(updated);

        List<Playlists> afterUpdate = playlistDao.findByUserId(TEST_USER_ID);
        Playlists updatedPlaylist = afterUpdate.stream()
                .filter(p -> p.getId().equals(found.getId()))
                .findFirst()
                .orElse(null);
        assertNotNull(updatedPlaylist);
        assertEquals("Updated description", updatedPlaylist.getDescription());
        assertFalse(updatedPlaylist.getIsPublic());
        System.out.println("  ✓ Updated playlist");

        // DELETE
        boolean deleted = playlistDao.delete(found.getId());
        assertTrue(deleted);

        List<Playlists> afterDelete = playlistDao.findByUserId(TEST_USER_ID);
        boolean exists = afterDelete.stream()
                .anyMatch(p -> p.getId().equals(found.getId()));
        assertFalse(exists);
        System.out.println("  ✓ Deleted playlist");

        System.out.println(" Complete CRUD cycle successful");
    }

    /**
     * Test creating multiple playlists for same user
     * Should successfully create multiple playlists
     */
    @Test
    @Order(17)
    void testMultiplePlaylists_SameUser() throws SQLException {
        System.out.println("Test: Multiple Playlists - Same User");

        //  Create 3 playlists
        Playlists p1 = playlistDao.create(Playlists.builder()
                .userId(TEST_USER_ID)
                .name("Multi Test 1 " + System.currentTimeMillis())
                .description("First playlist")
                .isPublic(true)
                .build());

        Playlists p2 = playlistDao.create(Playlists.builder()
                .userId(TEST_USER_ID)
                .name("Multi Test 2 " + System.currentTimeMillis())
                .description("Second playlist")
                .isPublic(false)
                .build());

        Playlists p3 = playlistDao.create(Playlists.builder()
                .userId(TEST_USER_ID)
                .name("Multi Test 3 " + System.currentTimeMillis())
                .description("Third playlist")
                .isPublic(true)
                .build());

        assertNotNull(p1);
        assertNotNull(p2);
        assertNotNull(p3);

        List<Playlists> userPlaylists = playlistDao.findByUserId(TEST_USER_ID);
        assertTrue(userPlaylists.size() >= 3, "User should have at least 3 playlists");

        System.out.println("Successfully created multiple playlists for same user");
    }
}