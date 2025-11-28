package data_access;

import Entities.User;

public interface UserDataAccessInterface {
    User get(String username);
    boolean existsByName(String identifier);
    void save(User user);
    String getCurrentUsername();
    void setCurrentUsername(String name);
}