package main.entity;

/**
 * A simple entity representing a user. Users have a username and password.
 */

public class User {
    private String username;
    private String password;


    /**
     * Creates a new user with the given non-empty name and non-empty password.
     * @param username the username
     * @param password the password
     * @throws IllegalArgumentException if the password or name are empty
     */
    public User(String username, String password) {
        if ("".equals(username)) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if ("".equals(password)) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        this.username = username;
        this.password = password;
    }

    public String getName() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
