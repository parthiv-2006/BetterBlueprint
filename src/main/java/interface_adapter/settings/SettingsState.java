package interface_adapter.settings;

/**
 * The state for the Settings View Model.
 */
public class SettingsState {
    private String username = "";
    private String age = "";
    private String height = "";
    private String weight = "";
    private String settingsError;
    private String passwordError;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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

    public String getPasswordError() {
        return passwordError;
    }

    public void setPasswordError(String passwordError) {
        this.passwordError = passwordError;
    }
}
