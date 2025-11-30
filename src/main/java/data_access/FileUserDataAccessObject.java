package data_access;

import Entities.User;
import Entities.UserFactory;
import use_case.change_password.ChangePasswordUserDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.settings.SettingsUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;
import use_case.goals.GoalsUserDataAccessInterface;


import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * DAO for user data implemented using a File to persist the data.
 */
public class FileUserDataAccessObject implements SignupUserDataAccessInterface,
        LoginUserDataAccessInterface,
        ChangePasswordUserDataAccessInterface,
        LogoutUserDataAccessInterface,
        SettingsUserDataAccessInterface,
        UserDataAccessInterface,
        GoalsUserDataAccessInterface {

    private static final String HEADER = "username,password,age,height,weight";

    private final File csvFile;
    private final Map<String, Integer> headers = new LinkedHashMap<>();
    private final Map<String, User> accounts = new HashMap<>();

    private String currentUsername;

    /**
     * Construct this DAO for saving to and reading from a local file.
     * @param csvPath the path of the file to save to
     * @param userFactory factory for creating user objects
     * @throws RuntimeException if there is an IOException when accessing the file
     */
    public FileUserDataAccessObject(String csvPath, UserFactory userFactory) {

        csvFile = new File(csvPath);
        headers.put("username", 0);
        headers.put("password", 1);
        headers.put("age", 2);
        headers.put("height", 3);
        headers.put("weight", 4);

        if (csvFile.length() == 0) {
            save();
        }
        else {

            try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
                final String header = reader.readLine();

                if (!header.equals(HEADER)) {
                    throw new RuntimeException(String.format("header should be%n: %s%n but was:%n%s", HEADER, header));
                }

                String row;
                while ((row = reader.readLine()) != null) {
                    // Skip empty lines
                    if (row.trim().isEmpty()) {
                        continue;
                    }
                    final String[] col = row.split(",");

                    // Validate that the row has all required columns
                    if (col.length < 5) {
                        throw new RuntimeException(String.format(
                            "Invalid CSV row - expected 5 columns but got %d: %s", col.length, row));
                    }

                    final String username = String.valueOf(col[headers.get("username")]);
                    final String password = String.valueOf(col[headers.get("password")]);
                    final int age = Integer.parseInt(col[headers.get("age")]);
                    final int height = Integer.parseInt(col[headers.get("height")]);
                    final int weight = Integer.parseInt(col[headers.get("weight")]);
                    final User user = userFactory.create(username, password, age, height, weight);
                    accounts.put(username, user);
                }
            }
            catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
            writer.write(HEADER);
            writer.newLine();

            for (User user : accounts.values()) {
                String line = String.format("%s,%s,%d,%d,%d",
                        user.getName(),
                        user.getPassword(),
                        user.getAge(),
                        user.getHeight(),
                        user.getWeight());
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(User user) {
        accounts.put(user.getName(), user);
        this.save();
    }

    @Override
    public User get(String username) {
        return accounts.get(username);
    }

    @Override
    public void setCurrentUsername(String name) {
        currentUsername = name;
    }

    @Override
    public String getCurrentUsername() {
        if (currentUsername != null) return currentUsername;
        // fallback: if we have any accounts loaded from users.csv, return the first username
        if (!accounts.isEmpty()) {
            return accounts.keySet().iterator().next();
        }
        return null;
    }

    @Override
    public boolean existsByName(String identifier) {
        return accounts.containsKey(identifier);
    }

    @Override
    public void changePassword(User user) {
        // Replace the User object in the map
        accounts.put(user.getName(), user);
        save();
    }
}
