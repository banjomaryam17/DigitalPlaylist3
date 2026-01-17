package SpringProject.persistences;

import SpringProject.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Slf4j
@Repository
public class UserDaoImpl {

    private static final int DUPLICATE_KEY_ERROR_CODE = 1062;

    private final Connector connector;

    public UserDaoImpl(Connector connector) {
        this.connector = connector;
    }


    public void closeConnection() {
        connector.freeConnection();
    }

    @Override
    public boolean loginUser(String email, String password) {
        if (email == null || password == null) return false;

        Connection conn = connector.getConnection();
        if (conn == null) return false;

        String sql = "SELECT 1 FROM users WHERE email = ? AND password = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            log.error("loginUser() failed: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public User findUserByUsername(String username) {
        if (username == null || username.isBlank()) return null;

        Connection conn = connector.getConnection();
        if (conn == null) return null;

        String sql = "SELECT * FROM users WHERE username = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUserRow(rs);
                }
            }

        } catch (SQLException e) {
            log.error("findUserByUsername() failed: {}", e.getMessage());
        }

        return null;
    }

    @Override
    public int registerUser(User newUser) {
        if (newUser == null) return 0;

        Connection conn = connector.getConnection();
        if (conn == null) return 0;

        String sql = "INSERT INTO users (username, email, password, userType) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newUser.getUsername());
            ps.setString(2, newUser.getEmail());
            ps.setString(3, newUser.getPassword());
            ps.setInt(4, newUser.getUserType());

            return ps.executeUpdate();

        } catch (SQLException e) {
            if (e.getErrorCode() == DUPLICATE_KEY_ERROR_CODE) {
                log.warn("registerUser(): duplicate user/email: {}", e.getMessage());
                return -1;
            }
            log.error("registerUser() failed: {}", e.getMessage());
            return 0;
        }
    }

    @Override
    public User findUserByThereEmail(String email) {
        if (email == null || email.isBlank()) return null;

        Connection conn = connector.getConnection();
        if (conn == null) return null;

        String sql = "SELECT * FROM users WHERE email = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUserRow(rs);
                }
            }

        } catch (SQLException e) {
            log.error("findUserByThereEmail() failed: {}", e.getMessage());
        }

        return null;
    }

    @Override
    public User login(String username, String password) {
        if (username == null || password == null) return null;

        Connection conn = connector.getConnection();
        if (conn == null) return null;

        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUserRow(rs);
                }
            }

        } catch (SQLException e) {
            log.error("login() failed: {}", e.getMessage());
        }

        return null;
    }

    @Override
    public boolean updateUserEmail(String email, String username) throws RuntimeException {
        if (email == null || email.isBlank() || username == null || username.isBlank()) return false;

        Connection conn = connector.getConnection();
        if (conn == null) return false;

        String sql = "UPDATE users SET email = ? WHERE username = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, username);

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            log.error("updateUserEmail() failed: {}", e.getMessage());
            throw new RuntimeException("Database error updating email");
        }
    }

    @Override
    public boolean updateUserPassword(String password, String username) throws RuntimeException {
        if (password == null || password.isBlank() || username == null || username.isBlank()) return false;

        Connection conn = connector.getConnection();
        if (conn == null) return false;

        String sql = "UPDATE users SET password = ? WHERE username = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, password);
            ps.setString(2, username);

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            log.error("updateUserPassword() failed: {}", e.getMessage());
            throw new RuntimeException("Database error updating password");
        }
    }

    private static User mapUserRow(ResultSet rs) throws SQLException {
        return User.builder()
                .username(rs.getString("username"))
                .email(rs.getString("email"))
                .password(rs.getString("password"))
                .userType(rs.getInt("userType"))
                .build();
    }
}
