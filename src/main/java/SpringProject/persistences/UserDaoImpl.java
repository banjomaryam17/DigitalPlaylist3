package SpringProject.persistences;

import SpringProject.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoImpl implements UserDao {

    private final Connector connector;

    public UserDaoImpl(Connector connector) {
        this.connector = connector;
    }

    /**
     * Checks if a user exists with the given email and password
     *
     * @param email user's email
     * @param password user's password
     * @return true if a matching user is found, otherwise false
     */
    @Override
    public boolean loginUser(String email, String password) {
        String sql = "SELECT 1 FROM users WHERE email = ? AND password = ?";

        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Logs in a user using username and password
     *
     * @param username user's username
     * @param password user's password
     * @return the matching {@link User}, or null if login fails
     */
    @Override
    public User login(String username, String password) {
        String sql = "SELECT username, email, password, userType FROM users WHERE username = ? AND password = ?";

        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapUser(rs);
            }

        } catch (SQLException e) {
            return null;
        }

        return null;
    }



    /**
     * Finds a user by username
     *
     * @param username username to search for
     * @return the matching {@link User}, or null if not found
     */    @Override
    public User findUserByUsername(String username) {
        String sql = "SELECT username, email, password, userType FROM users WHERE username = ?";

        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapUser(rs);
            }

        } catch (SQLException e) {
            return null;
        }

        return null;
    }

    /**
     * Finds a user by email
     * <p>
     * Method name kept the same as the interface ({@code findUserByThereEmail}).
     * </p>
     *
     * @param email email to search for
     * @return the matching {@link User}, or null if not found
     */
    @Override
    public User findUserByThereEmail(String email) {
        String sql = "SELECT username, email, password, userType FROM users WHERE email = ?";

        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapUser(rs);
            }

        } catch (SQLException e) {
            return null;
        }

        return null;
    }

    /**
     * Registers a new user.
     *
     * @param newUser user to insert
     * @return 1 if inserted, 0 if insert failed
     */
    @Override
    public int registerUser(User newUser) {
        String sql = "INSERT INTO users (username, email, password, userType) VALUES (?, ?, ?, ?)";

        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newUser.getUsername());
            ps.setString(2, newUser.getEmail());
            ps.setString(3, newUser.getPassword());
            ps.setInt(4, newUser.getUserType());

            return ps.executeUpdate();

        } catch (SQLException e) {
            return 0;
        }
    }

    /**
     * Updates the email for a given username.
     *
     * @param email new email
     * @param username user to update
     * @return true if updated, false if no row was changed
     * @throws RuntimeException if the update fails
     */
    @Override
    public boolean updateUserEmail(String email, String username) throws RuntimeException {
        String sql = "UPDATE users SET email = ? WHERE username = ?";

        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, username);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("updateUserEmail failed: " + e.getMessage());
        }
    }


    /**
     * Updates the password for a given username
     *
     * @param password new password
     * @param username user to update
     * @return true if updated, false if no row was changed
     * @throws RuntimeException if the update fails
     */
    @Override
    public boolean updateUserPassword(String password, String username) throws RuntimeException {
        String sql = "UPDATE users SET password = ? WHERE username = ?";

        try (Connection conn = connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, password);
            ps.setString(2, username);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("updateUserPassword failed: " + e.getMessage());
        }
    }

    /**
     * Maps the current {@link ResultSet} row to a {@link User} object.
     *
     * @param rs result set positioned on a row
     * @return mapped {@link User}
     * @throws SQLException if reading columns fails
     */
    private User mapUser(ResultSet rs) throws SQLException {
        String username = rs.getString("username");
        String email = rs.getString("email");
        String password = rs.getString("password");
        int userType = rs.getInt("userType");

        return new User(username, email, password, userType);
    }
}




