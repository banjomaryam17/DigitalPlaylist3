package SpringProject;

import SpringProject.entities.User;
import SpringProject.persistences.MySqlConnector;
import SpringProject.persistences.UserDaoImpl;
import SpringProject.persistences.Connector;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoImplTest {

    private Connector connector;
    private userDao userDao;

    private String createdUsername;


    @BeforeEach
    void setup() {
        connector = new MySqlConnector("database_test.properties");

        Connection testConn = connector.getConnection();
        assertNotNull(testConn, "DB connection failed. Check database.properties username/password.");
        try { testConn.close(); } catch (Exception ignored) {}

        userDao = new UserDaoImpl(connector);
    }


    @AfterEach
    void cleanup() {
        if (createdUsername != null) {
            deleteUser(createdUsername);
        }
    }

    @Test
    void registerUser_insertsUser_andCanFindByUsername() {
        String unique = String.valueOf(System.currentTimeMillis());

        User u = new User(
                "TestUser" + unique,
                "test" + unique + "@gmail.com",
                "pass123",
                1
        );

        int inserted = userDao.registerUser(u);
        assertEquals(1, inserted);

        createdUsername = u.getUsername();

        User fromDb = userDao.findUserByUsername(u.getUsername());
        assertNotNull(fromDb);

        assertEquals(u.getUsername(), fromDb.getUsername());
        assertEquals(u.getEmail(), fromDb.getEmail());
        assertEquals(u.getPassword(), fromDb.getPassword());
        assertEquals(u.getUserType(), fromDb.getUserType());
    }

    @Test
    void loginUser_returnsTrue_whenCorrectDetails() {
        String unique = String.valueOf(System.currentTimeMillis());

        User u = new User(
                "LoginUser" + unique,
                "login" + unique + "@gmail.com",
                "abc123",
                1
        );

        int inserted = userDao.registerUser(u);
        assertEquals(1, inserted);

        createdUsername = u.getUsername();

        boolean ok = userDao.loginUser(u.getEmail(), u.getPassword());
        assertTrue(ok);
    }

    @Test
    void loginUser_returnsFalse_whenWrongPassword() {
        String unique = String.valueOf(System.currentTimeMillis());

        User u = new User(
                "WrongPass" + unique,
                "wrong" + unique + "@gmail.com",
                "realpass",
                1
        );

        int inserted = userDao.registerUser(u);
        assertEquals(1, inserted);

        createdUsername = u.getUsername();

        boolean ok = userDao.loginUser(u.getEmail(), "notThePassword");
        assertFalse(ok);
    }

    @Test
    void updateUserEmail_changesEmail() {
        String unique = String.valueOf(System.currentTimeMillis());

        User u = new User(
                "EmailUser" + unique,
                "old" + unique + "@gmail.com",
                "pass",
                1
        );

        int inserted = userDao.registerUser(u);
        assertEquals(1, inserted);

        createdUsername = u.getUsername();

        String newEmail = "new" + unique + "@gmail.com";
        boolean updated = userDao.updateUserEmail(newEmail, u.getUsername());
        assertTrue(updated);

        User fromDb = userDao.findUserByUsername(u.getUsername());
        assertNotNull(fromDb);
        assertEquals(newEmail, fromDb.getEmail());
    }


    private void deleteUser(String username) {
        String sql = "DELETE FROM users WHERE username = ?";

        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.executeUpdate();

        } catch (SQLException e) {
        }
    }
}
