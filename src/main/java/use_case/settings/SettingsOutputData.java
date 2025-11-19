package use_case.settings;

/**
 * Output Data for the Settings Use Case.
 */
public class SettingsOutputData {

    private final int age;
    private final int height;
    private final int weight;

    public SettingsOutputData(int age, int height, int weight) {
        this.age = age;
        this.height = height;
        this.weight = weight;
    }

    public int getAge() {
        return age;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }
}
