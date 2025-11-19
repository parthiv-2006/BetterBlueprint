package entity;

/**
 * A simple entity representing a user. Users have a username and password..
 */
public class User {

    private final String name;
    private final String password;
    private int age;
    private int height;
    private int weight;

    /**
     * Creates a new user with the given non-empty name and non-empty password.
     * @param name the username
     * @param password the password
     * @throws IllegalArgumentException if the password or name are empty
     */
    public User(String name, String password) {
        if ("".equals(name)) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if ("".equals(password)) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        this.name = name;
        this.password = password;
        this.age = 0;           // default values
        this.height = 0;
        this.weight = 0;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public int getAge() {return this.age;}

    public int getHeight() {return this.height;}

    public int getWeight() {return this.weight;}

    public void setAge(int age) {this.age = age;
    }

    public void setHeight(int height) { this.height = height;
    }

    public void setWeight(int weight) {
    }


}
