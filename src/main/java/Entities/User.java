package Entities;

/**
 * A simple entity representing a user. Users have a username and password..
 */
public class User {
    final private String username;
    final private String password;
    private Integer age;
    private Integer height;
    private Integer weight;

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

    public User(String username, String password, int age, int height, int weight) {
        this(username, password);
        validateAge(age);
        validateHeight(height);
        validateWeight(weight);
        this.age = age;
        this.height = height;
        this.weight = weight;
    }

    // Getters are fine
    public String getName() { return username; }
    public String getPassword() { return password; }
    public int getAge() {
        return age != null ? age : 0;
    }

    public int getHeight() {
        return height != null ? height : 0;
    }

    public int getWeight() {
        return weight != null ? weight : 0;
    }

    // Business logic methods
    public void updateAgeHeightWeight(int newAge, int newHeight, int newWeight) {
        validateAge(newAge);
        validateHeight(newHeight);
        validateWeight(newWeight);
        this.age = newAge;
        this.height = newHeight;
        this.weight = newWeight;
    }

    private void validateAge(int age) {
        if (age < 0 || age > 150) {
            throw new IllegalArgumentException("Age must be between 0 and 150");
        }
    }

    private void validateHeight(int height) {
        if (height <= 0) {
            throw new IllegalArgumentException("Height must be positive");
        }
    }

    private void validateWeight(int weight) {
        if (weight <= 0) {
            throw new IllegalArgumentException("Weight must be positive");
        }
    }
}
