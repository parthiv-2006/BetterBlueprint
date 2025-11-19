package use_case.settings;

import entity.User;

/**
 * DAO interface for the Settings Use Case.
 */
public interface SettingsUserDataAccessInterface {

    /**
     * Returns the user with the given username.
     * @param username the username to look up
     * @return the user with the given username
     */
    User get(String username);

    /**
     * Saves the user.
     * @param user the user to save
     */
    void save(User user);

    /**
     * Returns the current logged-in username.
     * @return the current username
     */
    String getCurrentUsername();
}
