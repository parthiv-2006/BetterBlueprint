package interface_adapter.home;

/**
 * The state for the Login View Model.
 */
public class HomeState {
    private String username = "";
    private String password = "";

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() {
        return password;
    }

}
