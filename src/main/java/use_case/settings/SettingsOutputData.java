package use_case.settings;

/**
 * Output Data for the Settings Use Case.
 */
public class SettingsOutputData {

    private final int age;
    private final int height;
    private final int weight;
    private final boolean ageUpdated;
    private final boolean heightUpdated;
    private final boolean weightUpdated;

    public SettingsOutputData(int age, int height, int weight,
                              boolean ageUpdated, boolean heightUpdated, boolean weightUpdated) {
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.ageUpdated = ageUpdated;
        this.heightUpdated = heightUpdated;
        this.weightUpdated = weightUpdated;
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

    public boolean isAgeUpdated() {
        return ageUpdated;
    }

    public boolean isHeightUpdated() {
        return heightUpdated;
    }

    public boolean isWeightUpdated() {
        return weightUpdated;
    }
}
