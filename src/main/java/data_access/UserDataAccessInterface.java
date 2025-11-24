package data_access;

import Entities.User;

public interface UserDataAccessInterface {
    User getUserById(String userId);
    User getUserByUsername(String username);
    void saveUser(User user);
}
