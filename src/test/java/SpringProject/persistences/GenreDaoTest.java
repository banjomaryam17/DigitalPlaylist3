package SpringProject.persistences;
import SpringProject.entities.Genre;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GenreDaoTest {

    private static Connector connector;
    private static GenreDao genreDao;

    /**
     * Set up database connection before all test
     */
    @BeforeAll
    static  void setupDatabase(){
        connector = new MySqlConnector("test_database.properties");
        genreDao = new GenreImpl(connector);
        System.out.println("GenreDAO Test");
    }

    /**
     * Clean up after each test
     */
    @AfterEach
    void cleanupTestData(){
        System.out.println("Test completed");
    }

    @AfterAll
    static void closeConnection(){
        if(genreDao != null){
            genreDao.closeConnection();
        }
        System.out.println("GenreDao Test Completed");
    }

    /**
     * Test creating a valid genre
     * Should successfully create and return genre with generated ID
     */
    @Test
    @Order(1)
    void testCreateGenreSuccess() throws SQLException{
        System.out.println("TEST: Create Genre Successfully");

        Genre genre = Genre.builder()
                .name("Test Genre " +System.currentTimeMillis())
                .description("A test genre for unit testing")
                .build();
        Genre created = genreDao.create(genre);
        assertNotNull(created, "Created genre should not null");
        assertNotNull(created.getId(), "Created genre should have a generated ID");
        assertTrue(created.getId() > 0, "Genre ID should be positive");
        assertEquals(genre.getName(), created.getName(), "Genre name should match");
        assertEquals(genre.getDescription(), created.getDescription(), "Description should match");

        System.out.println("Genre created with ID: "+ created.getId());
    }

    /**
     * Test creating genre with null object
     * Should throw IllegalArgumentException
     */
    @Test
    @Order(2)
    void testCreateGenreNull(){
        System.out.println("TEST: Create Null Genre");

        assertThrows(IllegalArgumentException.class, () -> {
            genreDao.create(null);
        }, "Should throw IllegalArgumentException for null genre");

        System.out.println("Correctly rejected null genre");
    }


    /**
     * Test creating a genre with duplicate name
     * @throws SQLException due to UNIQUE constriants
     */
    @Test
    @Order(3)
    void testCreateGenreDuplicate() throws SQLException{
        System.out.println("TEST: Create Duplicate Genre");
        String uniqueName = "Duplicate Test " + System.currentTimeMillis();
        Genre genre1 = Genre.builder()
                .name(uniqueName)
                .description("First genre")
                .build();
        genreDao.create(genre1);
        Genre genre2 = Genre.builder()
                .name(uniqueName)
                .description("Second genre")
                .build();
        assertThrows(SQLException.class, () -> {
            genreDao.create(genre2);
        }, "Should throw SQLException for duplicate genre name");

        System.out.println("Correctly rejected duplicate genre name");
    }

    /**
     * Test creating a genre with empty name
     * Should throw SQLException or be handled by validation
     */
    @Test
    @Order(4)
    void testCreateGenreEmptyName(){
        System.out.println("Test: Create Genre with Empty Name");

        Genre genre = Genre.builder()
                .name("")
                .description("Genre with empty name")
                .build();
        assertThrows(Exception.class, () -> {
            genreDao.create(genre);
        }, "Should reject genre with empty name");

        System.out.println("Correctly rejected empty genre name");
    }

    /**
     * Test finding an existing genre by ID should return the genre with
     * all fields populated
     * @throws SQLException if a database error occurs
     */
    @Test
    @Order(5)
    void testFindByIdWithExistingGenre() throws SQLException{
        System.out.println("TEST: Find by ID by existing Genre");

        Genre genre = new Genre();
        genre.setName("FindById Test " + System.currentTimeMillis());
        genre.setDescription("Test description");
        Genre created = genreDao.create(genre);


        Genre found = genreDao.findById(created.getId());
        assertNotNull(found, "Found genre should not be null");
        assertEquals(created.getId(), found.getId(), "Genre ID should match");
        assertEquals(created.getName(), found.getName(), "Genre name should match");
        assertEquals(created.getDescription(), found.getDescription(), "Description should match");

        System.out.println("Successfully found genre: " + found.getName());
    }

    /**
     * Test finding a non-existent genre should return null
     * @throws SQLException if database error occurs
     */
    @Test
    @Order(6)
    void testFindByIdNull() throws SQLException{
        System.out.println("Test: Find By ID of Null Genre");

        Genre found = genreDao.findById(9999);
        assertNull(found, "Should return null for non-existent genre");
        System.out.println("Correctly returned null for non-existent genre");
    }
    /**
     * Test finding genre with invalid ID (negative)
     * Should return null or handle gracefully
     */
    @Test
    @Order(7)
    void testFindById_InvalidId() throws SQLException {
        System.out.println("Test: Find By ID - Invalid ID");

        Genre found = genreDao.findById(-1);
        assertNull(found, "Should return null for invalid ID");

        System.out.println("Correctly handled invalid ID");
    }
    /**
     * Test finding genre with zero ID
     * Should return null
     */
    @Test
    @Order(8)
    void testFindById_ZeroId() throws SQLException {
        System.out.println("Test: Find By ID - Zero ID");

        // Act
        Genre found = genreDao.findById(0);

        // Assert
        assertNull(found, "Should return null for zero ID");

        System.out.println("✅ Correctly handled zero ID");
    }

    /**
     * Test retrieving all genres
     * Should return a list ( empty or contain genres)
     */
    @Test
    @Order(9)
    void testFindAll_ReturnsGenres() throws SQLException {
        System.out.println("Test: Find All - Returns Genres");

        // Act
        List<Genre> genres = genreDao.findAll();

        // Assert
        assertNotNull(genres, "Genres list should not be null");
        assertTrue(genres.size() > 0, "Should have at least the test genre we created");

        System.out.println("Retrieved " + genres.size() + " genres");

        // Print first few genres for verification
        genres.stream().limit(3).forEach(g ->
                System.out.println("  - " + g.getName())
        );
    }

    /**
     * Test updating an existing genre
     * Should successfully update name and description
     */
    @Test
    @Order(10)
    void testUpdate_ExistingGenre() throws SQLException {
        System.out.println("Test: Update - Existing Genre");

        Genre genre = new Genre();
        genre.setName("Update Test " + System.currentTimeMillis());
        genre.setDescription("Original description");
        Genre created = genreDao.create(genre);

        created.setName("Updated " + created.getName());
        created.setDescription("Updated description");

        boolean updated = genreDao.update(created);
        assertTrue(updated, "Update should return true");

        // Verify the update
        Genre found = genreDao.findById(created.getId());
        assertNotNull(found);
        assertEquals(created.getName(), found.getName(), "Name should be updated");
        assertEquals(created.getDescription(), found.getDescription(), "Description should be updated");

        System.out.println("✅ Successfully updated genre");
    }


    /**
     * Test updating a non-existent genre
     * Should return false
     */
    @Test
    @Order(11)
    void testUpdate_NonExistentGenre() throws SQLException {
        System.out.println("Test: Update Non-Existent Genre");

        // Arrange
        Genre genre = Genre.builder()
                .id(99999)
                .name("Non-existent")
                .description("This genre doesn't exist")
                .build();

        // Act
        boolean updated = genreDao.update(genre);

        // Assert
        assertFalse(updated, "Update should return false for non-existent genre");

        System.out.println("✅ Correctly returned false for non-existent genre");
    }

    /**
     * Test updating genre with null object
     * Should throw IllegalArgumentException
     */
    @Test
    @Order(12)
    void testUpdate_NullGenre() {
        System.out.println("Test: Update Null Genre");

        assertThrows(IllegalArgumentException.class, () -> {
            genreDao.update(null);
        }, "Should throw IllegalArgumentException for null genre");

        System.out.println("Correctly rejected null genre");
    }
    @Test
    @Order(13)
    void testUpdate_InvalidId() throws SQLException {
        System.out.println("Test: Update - Invalid ID");

        Genre genre = Genre.builder()
                .id(0)
                .name("Invalid")
                .description("Invalid ID")
                .build();
        boolean updated = genreDao.update(genre);
        assertFalse(updated, "Update should return false for invalid ID");

        System.out.println("Correctly rejected invalid ID");
    }

    /**
     * Test updating to duplicate name
     * Should throw SQLException
     */
    @Test
    @Order(14)
    void testUpdate_DuplicateName() throws SQLException {
        System.out.println("Test: Update Duplicate Name");
        //Create two genres
        String existingName = "Existing " + System.currentTimeMillis();
        Genre genre1 = genreDao.create(Genre.builder()
                .name(existingName)
                .description("First")
                .build());

        Genre genre2 = genreDao.create(Genre.builder()
                .name("Original " + System.currentTimeMillis())
                .description("Second")
                .build());
        // update genre2 to have same name as genre1
        genre2.setName(existingName);
        assertThrows(SQLException.class, () -> {
            genreDao.update(genre2);
        }, "Should throw SQLException for duplicate name");

        System.out.println("Correctly rejected duplicate name on update");
    }

    /**
     * Test deleting an existing genre (not referenced by songs/albums)
     * Should successfully delete and return true
     */
    @Test
    @Order(15)
    void testDelete_ExistingGenre() throws SQLException {
        System.out.println("Test: Delete Existing Genre");
// Create a genre to delete
        Genre genre = genreDao.create(Genre.builder()
                .name("Delete Test " + System.currentTimeMillis())
                .description("Will be deleted")
                .build());
        boolean deleted = genreDao.delete(genre.getId());
        assertTrue(deleted, "Delete should return true");

        // Verify it's deleted
        Genre found = genreDao.findById(genre.getId());
        assertNull(found, "Genre should no longer exist");

        System.out.println(" Successfully deleted genre");
    }

    /**
     * Test deleting a non-existent genre
     * Should return false
     */
    @Test
    @Order(16)
    void testDelete_NonExistentGenre() throws SQLException {
        System.out.println("Test: Delete - Non-Existent Genre");

        // Act
        boolean deleted = genreDao.delete(99999);

        // Assert
        assertFalse(deleted, "Delete should return false for non-existent genre");

        System.out.println(" Correctly returned false for non-existent genre");
    }

    /**
     * Test deleting with invalid ID
     * Should return false
     */
    @Test
    @Order(17)
    void testDelete_InvalidId() throws SQLException {
        System.out.println("Test: Delete - Invalid ID");

        // Act & Assert
        assertFalse(genreDao.delete(-1), "Should return false for negative ID");
        assertFalse(genreDao.delete(0), "Should return false for zero ID");

        System.out.println("Correctly handled invalid IDs");
    }
    /**
     * Test complete CRUD cycle
     * Create, Read, Update, Delete
     */
    @Test
    @Order(19)
    void testCompleteCRUDCycle() throws SQLException {
        System.out.println("Test: Complete CRUD Cycle");

        // CREATE
        Genre genre = Genre.builder()
                .name("CRUD Test " + System.currentTimeMillis())
                .description("Testing full CRUD cycle")
                .build();
        Genre created = genreDao.create(genre);
        assertNotNull(created);
        assertNotNull(created.getId());
        System.out.println("  ✓ Created genre ID: " + created.getId());

        // READ
        Genre found = genreDao.findById(created.getId());
        assertNotNull(found);
        assertEquals(created.getName(), found.getName());
        System.out.println(" Found genre: " + found.getName());

        // UPDATE
        found.setDescription("Updated description");
        boolean updated = genreDao.update(found);
        assertTrue(updated);
        Genre afterUpdate = genreDao.findById(found.getId());
        assertEquals("Updated description", afterUpdate.getDescription());
        System.out.println(" Updated genre");

        // DELETE
        boolean deleted = genreDao.delete(found.getId());
        assertTrue(deleted);
        Genre afterDelete = genreDao.findById(found.getId());
        assertNull(afterDelete);
        System.out.println(" Deleted genre");

        System.out.println("Complete CRUD cycle successful");
    }

    /**
     * Test creating multiple genres
     * Should successfully create multiple distinct genres
     */
    @Test
    @Order(20)
    void testMultipleGenres_Create() throws SQLException {
        System.out.println("Test: Multiple Genres - Create");

        Genre g1 = genreDao.create(Genre.builder()
                .name("Multi Test 1 " + System.currentTimeMillis())
                .description("First genre")
                .build());

        Genre g2 = genreDao.create(Genre.builder()
                .name("Multi Test 2 " + System.currentTimeMillis())
                .description("Second genre")
                .build());

        Genre g3 = genreDao.create(Genre.builder()
                .name("Multi Test 3 " + System.currentTimeMillis())
                .description("Third genre")
                .build());
        assertNotNull(g1);
        assertNotNull(g2);
        assertNotNull(g3);
        assertNotEquals(g1.getId(), g2.getId(), "Each genre should have unique ID");
        assertNotEquals(g2.getId(), g3.getId(), "Each genre should have unique ID");

        List<Genre> allGenres = genreDao.findAll();
        assertTrue(allGenres.size() >= 3, "Should have at least 3 genres");

        System.out.println("Successfully created multiple genres");
    }
}

