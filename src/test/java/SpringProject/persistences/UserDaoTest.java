package SpringProject.persistences;

import SpringProject.entities.User;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for UserDao
 * Tests all operations against the test database
 *
 * @author Amena
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserDaoTest {

    private static Connector connector;
    private static UserDao userDao;

    private static String testUsername;
    private static String testEmail;
    private static String testPassword;

    /**
     * Set up test database connection before all tests
     */
    @BeforeAll
    static void setupDatabase() {
        connector = new MySqlConnector("test_database.properties");
        userDao = new UserDaoImpl(connector);
        System.out.println("=== UserDao Test Suite Started ===");
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
        if (userDao != null) {
            UserDao.closeConnection();
        }
        System.out.println("=== UserDao Test Suite Completed ===");
    }


    /**
     * Test registering a valid user
     * Should successfully add and return user when searched
     */
    @Test
    @Order(1)
    void testRegisterUser_Success() {
        System.out.println("Test: Register User - Success");

        testUsername = "testUser_" + System.currentTimeMillis();
        testEmail = testUsername + "@demo.com";
        testPassword = "pass123";

        User u = User.builder()
                .username(testUsername)
                .email(testEmail)
                .password(testPassword)
                .userType(1)
                .build();

        int rows = userDao.registerUser(u);

        assertEquals(1, rows, "Should insert 1 user row");

        User found = userDao.findUserByUsername(testUsername);
        assertNotNull(found, "User should be found after registration");
        assertEquals(testUsername, found.getUsername(), "Username should match");
        assertEquals(testEmail, found.getEmail(), "Email should match");
        assertEquals(1, found.getUserType(), "UserType should match");

        System.out.println("User created with username: " + testUsername);
    }

    /**
     * Test registering a null user
     * Your DAO returns 0 for null user
     */
    @Test
    @Order(2)
    void testRegisterUser_NullUser() {
        System.out.println("Test: Register User - Null User");

        int rows = userDao.registerUser(null);
        assertEquals(0, rows, "Should return 0 when trying to register null user");

        System.out.println(" Correctly handled null user");
    }

    /**
     * Test registering a duplicate username/email
     * Should return -1
     */
    @Test
    @Order(3)
    void testRegisterUser_Duplicate() {
        System.out.println("Test: Register User - Duplicate");

        String username = "dupUser_" + System.currentTimeMillis();

        User u1 = User.builder()
                .username(username)
                .email(username + "_1@demo.com")
                .password("pass123")
                .userType(1)
                .build();

        User u2 = User.builder()
                .username(username) // same username
                .email(username + "_2@demo.com")
                .password("pass123")
                .userType(1)
                .build();

        int first = userDao.registerUser(u1);
        int second = userDao.registerUser(u2);

        assertEquals(1, first, "First insert should succeed");
        assertEquals(-1, second, "Second insert should fail and return -1");

        System.out.println(" Correctly rejected duplicate user");
    }


    /**
     * Test finding user by username
     * Should return user if exists
     */
    @Test
    @Order(4)
    void testFindUserByUsername_ExistingUser() {
        System.out.println("Test: Find User By Username - Existing User");

        User found = userDao.findUserByUsername(testUsername);
        assertNotNull(found, "User should be found");
        assertEquals(testUsername, found.getUsername(), "Username should match");

        System.out.println(" Found user: " + found.getUsername());
    }

    /**
     * Test finding user by username that does not exist
     * Should return null
     */
    @Test
    @Order(5)
    void testFindUserByUsername_NonExistentUser() {
        System.out.println("Test: Find User By Username - Non-Existent User");

        User found = userDao.findUserByUsername("no_user_" + System.currentTimeMillis());
        assertNull(found, "Should return null for non-existent user");

        System.out.println(" Correctly returned null");
    }

    /**
     * Test login with correct username and password
     * Should return User
     */
    @Test
    @Order(6)
    void testLogin_Success() {
        System.out.println("Test: Login - Success");

        User loggedIn = userDao.login(testUsername, testPassword);

        assertNotNull(loggedIn, "Login should return user");
        assertEquals(testUsername, loggedIn.getUsername(), "Username should match");

        System.out.println(" Login successful");
    }

    /**
     * Test login with wrong password
     * Should return null
     */
    @Test
    @Order(7)
    void testLogin_WrongPassword() {
        System.out.println("Test: Login - Wrong Password");

        User loggedIn = userDao.login(testUsername, "wrongpass");
        assertNull(loggedIn, "Login should fail and return null");

        System.out.println(" Correctly failed login");
    }


    /**
     * Test updating email
     * Should return true and DB should have new email
     */
    @Test
    @Order(8)
    void testUpdateUserEmail_Success() {
        System.out.println("Test: Update User Email - Success");

        String newEmail = "updated_" + System.currentTimeMillis() + "@demo.com";

        boolean updated = userDao.updateUserEmail(newEmail, testUsername);
        assertTrue(updated, "Email update should return true");

        User found = userDao.findUserByUsername(testUsername);
        assertNotNull(found, "User should still exist");
        assertEquals(newEmail, found.getEmail(), "Email should be updated");

        System.out.println(" Email updated successfully");
    }

    /**
     * Test updating password
     * Should return true and login should work with new password
     */
    @Test
    @Order(9)
    void testUpdateUserPassword_Success() {
        System.out.println("Test: Update User Password - Success");

        String newPass = "newPass_" + System.currentTimeMillis();

        boolean updated = userDao.updateUserPassword(newPass, testUsername);
        assertTrue(updated, "Password update should return true");

        // old password should fail
        User oldLogin = userDao.login(testUsername, testPassword);
        assertNull(oldLogin, "Old password should not work");

        // new password should work
        User newLogin = userDao.login(testUsername, newPass);
        assertNotNull(newLogin, "New password should work");

        // update stored password variable for  future tests
        testPassword = newPass;

        System.out.println(" Password updated successfully");
    }

    /**
     * Test updating email for non-existent user
     * Should return false
     */
    @Test
    @Order(10)
    void testUpdateUserEmail_NonExistentUser() {
        System.out.println("Test: Update User Email - Non-Existent User");

        boolean updated = userDao.updateUserEmail("x@demo.com", "no_user_" + System.currentTimeMillis());
        assertFalse(updated, "Should return false for non-existent user");

        System.out.println(" Correctly returned false");
    }

    /**
     * Test updating password for non-existent user
     * Should return false
     */
    @Test
    @Order(11)
    void testUpdateUserPassword_NonExistentUser() {
        System.out.println("Test, Update User Password - Non Existent User");

        boolean updated = userDao.updateUserPassword("abc123", "no_user_" + System.currentTimeMillis());
        assertFalse(updated, "Should return false for non-existent user");

        System.out.println(" Correctly returned false");
    }
}
