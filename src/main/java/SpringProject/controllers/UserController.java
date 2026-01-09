package SpringProject.controllers;

import SpringProject.entities.User;
import SpringProject.persistences.UserDao;
import SpringProject.persistences.UserDaoImpl;
import SpringProject.persistences.Connector;
import SpringProject.persistences.MySqlConnector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserDao userDao;

    public UserController() {
        Connector connector = new MySqlConnector("database.properties");
        this.userDao = new UserDaoImpl(connector);
    }

    /**
     * Gets a user by their username.
     * @param username the username to search for
     * @return the matching  User
     */
     @GetMapping(path = "/{username}", produces = "application/json")
    public User getUser(@PathVariable String username) {
        User u = userDao.findUserByUsername(username);
        if (u == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return u;
    }

    /**
     * Registers a new user.
     *
     * @param newUser user data from JSON body
     * @return created user
     */
    @PostMapping(path = "/register", consumes = "application/json", produces = "application/json")
    public User register(@RequestBody User newUser) {
        if (newUser == null || newUser.getUsername() == null || newUser.getUsername().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username required");
        }

        int added = userDao.registerUser(newUser);
        if (added != 1) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists or cannot register");
        }

        return userDao.findUserByUsername(newUser.getUsername());
    }

    /**
     * Logs in a user
     * @param req login details ,username + password
     * @return user if login is correct
     */
    @PostMapping(path = "/login", consumes = "application/json", produces = "application/json")
    public User login(@RequestBody LoginRequest req) {
        if (req == null || req.username == null || req.password == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username and password required");
        }

        User u = userDao.login(req.username, req.password);
        if (u == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong username or password");
        }
        return u;
    }


    /**
     * Updates a user's email.
     *
     * @param username username to update
     * @param req contains the new email
     * @return true if updated
     */
    @PutMapping(path = "/{username}/email", consumes = "application/json", produces = "application/json")
    public boolean updateEmail(@PathVariable String username, @RequestBody EmailRequest req) {
        if (req == null || req.email == null || req.email.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email required");
        }

        boolean updated = userDao.updateUserEmail(req.email, username);
        if (!updated) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return true;
    }

    /**
     * Updates a user's password.
     *
     * @param username username to update
     * @param req contains the new password
     * @return true if updated
     */
    @PutMapping(path = "/{username}/password", consumes = "application/json", produces = "application/json")
    public boolean updatePassword(@PathVariable String username, @RequestBody PasswordRequest req) {
        if (req == null || req.password == null || req.password.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password required");
        }

        boolean updated = userDao.updateUserPassword(req.password, username);
        if (!updated) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return true;
    }

    /** JSON body for login requests. */
    public static class LoginRequest {
        public String username;
        public String password;
    }

    /** JSON body for changing email. */
    public static class EmailRequest {
        public String email;
    }
    /** JSON body for changing password. */
    public static class PasswordRequest {
        public String password;
    }
}
