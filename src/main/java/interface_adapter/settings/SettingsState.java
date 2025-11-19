package interface_adapter.settings;

/**
 * The state for the Settings View Model.
 */
public class SettingsState {
    private String age = "";
    private String height = "";
    private String weight = "";
    private String settingsError;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getSettingsError() {
        return settingsError;
    }

    public void setSettingsError(String settingsError) {
        this.settingsError = settingsError;
    }
}
