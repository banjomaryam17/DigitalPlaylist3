package SpringProject.services;

import lombok.extern.slf4j.Slf4j;
import SpringProject.entities.User;
import SpringProject.persistences.UserDao;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServices {

    private UserDao userDao;

    public UserServices(UserDao userDao) {
        this.userDao = userDao;
    }

    public void shutdownServices() {
        UserDao.closeConnection();
    }

    /**
     * Login user by email + password (eturns boolean
     */
    public boolean loginUser(String email, String password) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email must be provided");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password must be provided");
        }

        log.info("Logging in user with email '{}'", email);
        return userDao.loginUser(email, password);
    }

    /**
     * Find user by username
     */
    public User findUserByUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username must be provided");
        }

        log.info("Finding user by username '{}'", username);
        return userDao.findUserByUsername(username);
    }

    /**
     * Register user
     */
    public int registerUser(User newUser) {
        if (newUser == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        log.info("Registering a new user");
        return userDao.registerUser(newUser);
    }

    /**
     * Find user by email
     */
    public User findUserByThereEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email must be provided");
        }

        log.info("Finding user by email '{}'", email);
        return userDao.findUserByThereEmail(email);
    }

    /**
     * Login by username + password returns User
     */
    public User login(String username, String password) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username must be provided");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password must be provided");
        }

        log.info("Logging in user with username '{}'", username);
        return userDao.login(username, password);
    }

    /**
     * Update email
     */
    public boolean updateUserEmail(String email, String username) throws RuntimeException {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username must be provided");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email must be provided");
        }

        log.info("Updating email for username '{}'", username);
        return userDao.updateUserEmail(email, username);
    }

    /**
     * Update password
     */
    public boolean updateUserPassword(String password, String username) throws RuntimeException {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username must be provided");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password must be provided");
        }

        log.info("Updating password for username '{}'", username);
        return userDao.updateUserPassword(password, username);
    }
}
