package Entities;

/**
 * Factory for creating CommonUser objects.
 */
public class UserFactory {

    public User create(String username, String password, int age, int height, int weight) {
        return new User(username, password, age, height, weight);
    }

    public User create(String username, String password) {
        return new User(username, password);
    }
}