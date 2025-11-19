package use_case.settings;

/**
 * The Input Data for the Settings Use Case.
 */
public class SettingsInputData {

    private final String age;
    private final String height;
    private final String weight;

    public SettingsInputData(String age, String height, String weight) {
        this.age = age;
        this.height = height;
        this.weight = weight;
    }

    public String getAge() {
        return age;
    }

    public String getHeight() {
        return height;
    }

    public String getWeight() {
        return weight;
    }
}
