package app;

import view.HomeView; // Import your new view

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        // Create the main application window (JFrame)
        JFrame application = new JFrame("BetterBlueprint Health App");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Create a CardLayout panel to hold all views
        CardLayout cardLayout = new CardLayout();
        JPanel views = new JPanel(cardLayout);
        application.add(views);

        // ----------------------------------------------------
        // Create your HomeView and add it to the views panel
        // ----------------------------------------------------
        HomeView homeView = new HomeView();
        views.add(homeView, "Home"); // Give it a name, e.g., "Home"

        // --- You will add other views here as they are built ---
        // LoginView loginView = new LoginView();
        // views.add(loginView, "Login");
        // ----------------------------------------------------

        // For now, we'll show the Home view directly.
        // Later, you'll show "Login" first.
        cardLayout.show(views, "Home");

        // Finish setting up the frame
        application.pack();
        application.setLocationRelativeTo(null); // Center the window
        application.setVisible(true);
    }
}