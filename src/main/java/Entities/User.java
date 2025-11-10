package main.java.Entities;

/**
 * A simple entity representing a user. Users have a username password, age, height, and weight.
 */

public class User {
    private String username;
    private String password;
    private int age;
    private int height;
    private int weight;


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

    /**
     * Creates a user with all attributes.
     * @param username the username
     * @param password the password
     * @param age the user's age in years
     * @param height the user's height
     * @param weight the user's weight
     */
    public User(String username, String password, int age, int height, int weight) {
        this(username, password);  // reuses validation logic
        this.age = age;
        this.height = height;
        this.weight = weight;
    }

    public String getName() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getAge() {return age;};

    public void setAge(int age) {this.age = age;}

    public int getHeight() {return height;}

    public void setHeight(int height) {this.height = height;}

    public int getWeight() {return weight;}

    public void setWeight(int weight) {this.weight = weight;}

}
