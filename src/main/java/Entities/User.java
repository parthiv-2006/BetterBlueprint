package Entities;

/**
 * A simple entity representing a user. Users have a username and password..
 */
public class User {
    private static final int MIN_AGE = 0;
    private static final int MAX_AGE = 150;
    private static final int MIN_HEIGHT = 0;
    private static final int MIN_WEIGHT = 0;
    private static final int DEFAULT_VALUE = 0;

    final private String username;
    final private String password;
    private Integer age;
    private Integer height;
    private Integer weight;

    public User(String username, String password) {
        if ("".equals(username)) {
            throw new IllegalArgumentException("Usefrname cannot be empty");
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
        return age != null ? age : DEFAULT_VALUE;
    }

    public int getHeight() {
        return height != null ? height : DEFAULT_VALUE;
    }

    public int getWeight() {
        return weight != null ? weight : DEFAULT_VALUE;
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

    /**
     * Update only the age field.
     * @param newAge the new age value
     */
    public void updateAge(int newAge) {
        validateAge(newAge);
        this.age = newAge;
    }

    /**
     * Update only the height field.
     * @param newHeight the new height value
     */
    public void updateHeight(int newHeight) {
        validateHeight(newHeight);
        this.height = newHeight;
    }

    /**
     * Update only the weight field.
     * @param newWeight the new weight value
     */
    public void updateWeight(int newWeight) {
        validateWeight(newWeight);
        this.weight = newWeight;
    }

    private void validateAge(int age) {
        if (age < MIN_AGE || age > MAX_AGE) {
            throw new IllegalArgumentException("Age must be between " + MIN_AGE + " and " + MAX_AGE);
        }
    }

    private void validateHeight(int height) {
        if (height < MIN_HEIGHT) {
            throw new IllegalArgumentException("Height cannot be negative");
        }
    }

    private void validateWeight(int weight) {
        if (weight < MIN_WEIGHT) {
            throw new IllegalArgumentException("Weight cannot be negative");
        }
    }
}
