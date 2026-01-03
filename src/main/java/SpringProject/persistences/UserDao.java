package SpringProject.persistences;

import SpringProject.entities.User;
import SpringProject.entities.User;

    public interface UserDao {


// UserDaoClass

        public boolean loginUser(String email, String password);
        public User findUserByUsername(String username);

        public int registerUser(User newUser);
        public User findUserByThereEmail(String email);

        public User login(String username, String password);

        public boolean updateUserEmail(String email, String username) throws RuntimeException;
        public boolean updateUserPassword(String password, String username) throws RuntimeException;


    }


