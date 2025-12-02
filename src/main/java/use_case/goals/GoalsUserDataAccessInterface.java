package use_case.goals;

import Entities.User;

/**
 * Data access interface for the Goals use case.
 * Implementations provide access to the currently logged-in user and user lookup.
 */
public interface GoalsUserDataAccessInterface {

    /**
     * @return the username of the current logged-in user, or null if none.
     */
    String getCurrentUsername();

    /**
     * @param username the username to look up
     * @return the corresponding User entity, or null if not found
     */
    User get(String username);
}
