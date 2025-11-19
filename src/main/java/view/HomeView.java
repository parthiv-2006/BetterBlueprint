package view;

import app.StyleConstants;
import interface_adapter.ViewManagerModel; // New Import
import interface_adapter.home.HomeViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// Removed ActionListener and PropertyChangeListener interfaces
public class HomeView extends JPanel {
    private final String viewName = "home";

    public final JButton home;
    public final JButton inputMetrics;
    public final JButton accountSettings;
    public final JButton myScore;
    public final JButton insights;
    public final JButton history;
    public final JButton caloriePlan;

    // Use ViewManagerModel to trigger navigation changes (Clean Architecture)
    private final ViewManagerModel viewManagerModel;

    // The local CardLayout and JPanel are no longer needed since the main CardLayout
    // in AppBuilder now manages the primary view switching (Login, Signup, Home).
    // The inner navigation logic can now use the ViewManagerModel.

    /**
     * Updated constructor to accept the ViewManagerModel.
     * @param homeViewModel The ViewModel for the Home tab data.
     * @param viewManagerModel The model used to switch between main application views.
     */
    public HomeView(HomeViewModel homeViewModel, ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
        this.setLayout(new BorderLayout());

        // === 3. CREATE THE TOP NAVBAR ===
        JPanel navbarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        navbarPanel.setBackground(StyleConstants.COLOR_NAV_BAR);

        // Create buttons
        home = new JButton("Home");
        inputMetrics = new JButton("Input Health Metrics");
        accountSettings = new JButton("Account/Settings");
        myScore = new JButton("My Score");
        insights = new JButton("Insights");
        history = new JButton("History");
        caloriePlan = new JButton("Calorie Plan");

        // Style all 7 buttons
        styleNavbarButton(home);
        styleNavbarButton(inputMetrics);
        styleNavbarButton(accountSettings);
        styleNavbarButton(myScore);
        styleNavbarButton(insights);
        styleNavbarButton(history);
        styleNavbarButton(caloriePlan);

        // Add buttons to navbar
        navbarPanel.add(home);
        navbarPanel.add(inputMetrics);
        navbarPanel.add(accountSettings);
        navbarPanel.add(myScore);
        navbarPanel.add(insights);
        navbarPanel.add(history);
        navbarPanel.add(caloriePlan);

        // === 4. CREATE THE CENTER CONTENT PANEL ===
        // Note: The main card layout is now managed outside this view (in AppBuilder).
        // This view only presents the Home content by default.

        // Create a separate content panel to hold the views *within* HomeView
        // This is necessary because HomeView is now just one panel in the main CardLayout
        JPanel contentPanel = new JPanel(new CardLayout());
        contentPanel.setBackground(StyleConstants.COLOR_CONTENT_BACKGROUND);

        // --- A. Create the DEDICATED "Home" page ---
        JPanel homeContentView = createHomeContentView();

        // --- B. Create the SEPARATE "My Score" page ---
        JPanel myScoreView = createMyScoreView();

        // --- C. Create other placeholder views ---
        JPanel inputMetricsView = createPlaceholderView("Input Metrics View");
        JPanel accountSettingsView = createPlaceholderView("Account/Settings View");
        JPanel insightsView = createPlaceholderView("Insights View");
        JPanel historyView = createPlaceholderView("History View");
        JPanel caloriePlanView = createPlaceholderView("caloriePlan View");

        // --- D. Add all sub-views to the content panel ---
        contentPanel.add(homeContentView, "Home");
        contentPanel.add(myScoreView, "My Score");
        contentPanel.add(inputMetricsView, "Input Metrics");
        contentPanel.add(accountSettingsView, "Account Settings");
        contentPanel.add(insightsView, "Insights");
        contentPanel.add(historyView, "History");
        contentPanel.add(caloriePlanView, "Calorie Plan");

        // === 5. ASSEMBLE THE HOMEVIEW ===
        this.add(navbarPanel, BorderLayout.NORTH);
        this.add(contentPanel, BorderLayout.CENTER);

        // === 6. ADD ACTION LISTENERS to switch views within the HomeView container ===
        CardLayout cardLayout = (CardLayout) contentPanel.getLayout();

        home.addActionListener(e -> cardLayout.show(contentPanel, "Home"));
        inputMetrics.addActionListener(e -> cardLayout.show(contentPanel, "Input Metrics"));
        accountSettings.addActionListener(e -> cardLayout.show(contentPanel, "Account Settings"));
        myScore.addActionListener(e -> cardLayout.show(contentPanel, "My Score"));
        insights.addActionListener(e -> cardLayout.show(contentPanel, "Insights"));
        history.addActionListener(e -> cardLayout.show(contentPanel, "History"));
        caloriePlan.addActionListener(e -> cardLayout.show(contentPanel, "Calorie Plan"));

        // Set "Home" as the default homepage
        cardLayout.show(contentPanel, "Home");
    }

    /**
     * Creates the main content panel for the Home view, including logo, description, and CTA.
     */
    private JPanel createHomeContentView() {
        JPanel homeView = new JPanel();
        homeView.setLayout(new BoxLayout(homeView, BoxLayout.Y_AXIS));
        homeView.setBackground(StyleConstants.COLOR_CONTENT_BACKGROUND);
        homeView.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));

        // -- Panel for Logo and Description (side-by-side)
        JPanel logoAndDescriptionPanel = new JPanel();
        logoAndDescriptionPanel.setLayout(new BoxLayout(logoAndDescriptionPanel, BoxLayout.X_AXIS));
        logoAndDescriptionPanel.setBackground(StyleConstants.COLOR_CONTENT_BACKGROUND);
        logoAndDescriptionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add Logo
        try {
            java.net.URL logoUrl = getClass().getResource("/BetterBlueprint.png");

            if (logoUrl != null) {
                ImageIcon logoIcon = new ImageIcon(logoUrl);
                Image scaledImage = logoIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));
                logoLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
                logoAndDescriptionPanel.add(logoLabel);
            } else {
                logoAndDescriptionPanel.add(new JLabel("Logo Not Found"));
            }
        } catch (Exception e) {
            logoAndDescriptionPanel.add(new JLabel("Logo Error"));
        }

        logoAndDescriptionPanel.add(Box.createRigidArea(new Dimension(30, 0)));

        // -- Right side of header: Welcome Title + Description
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(StyleConstants.COLOR_CONTENT_BACKGROUND);

        JLabel welcomeTitle = new JLabel("Welcome to BetterBlueprint");
        welcomeTitle.setFont(StyleConstants.FONT_TITLE);
        welcomeTitle.setForeground(StyleConstants.COLOR_TEXT_DARK);
        welcomeTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        String descriptionText = "<html><body style='width: 350px; color: " + getHtmlColor(StyleConstants.COLOR_TEXT_DARK.darker()) + ";'>" +
                "Track your daily metrics, calculate personalized health scores, " +
                "and receive AI-powered insights to improve your well-being." +
                "</body></html>";
        JLabel descriptionLabel = new JLabel(descriptionText);
        descriptionLabel.setFont(StyleConstants.FONT_BODY);
        descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(welcomeTitle);
        textPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        textPanel.add(descriptionLabel);

        logoAndDescriptionPanel.add(textPanel);

        // -- Button to guide user to Input Metrics --
        JButton goToInputMetrics = styleAndAddGoToInputMetricsButton();

        homeView.add(Box.createVerticalGlue());
        homeView.add(logoAndDescriptionPanel);
        homeView.add(Box.createRigidArea(new Dimension(0, 50)));
        homeView.add(goToInputMetrics);
        homeView.add(Box.createVerticalGlue());

        return homeView;
    }

    /**
     * Creates and styles the "Start Tracking Now" button and adds its action listener.
     */
    private JButton styleAndAddGoToInputMetricsButton() {
        JButton goToInputMetrics = new JButton("Start Tracking Now");
        goToInputMetrics.setFont(StyleConstants.FONT_CTA_BUTTON);
        goToInputMetrics.setForeground(Color.WHITE);
        goToInputMetrics.setBackground(StyleConstants.COLOR_PRIMARY_BUTTON);
        goToInputMetrics.setFocusPainted(false);
        goToInputMetrics.setBorderPainted(false);
        goToInputMetrics.setOpaque(true);
        goToInputMetrics.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Make button bigger
        goToInputMetrics.setMaximumSize(new Dimension(220, 50));
        goToInputMetrics.setPreferredSize(new Dimension(220, 50));

        // The action listener now uses the ViewManagerModel to switch to the Input Metrics view
        // Note: For inner tabs (not main application views), the local CardLayout is used.
        // This button's action remains targeted to a view *within* HomeView.
        goToInputMetrics.addActionListener(e -> {
            // Find the parent CardLayout to switch the inner view
            JPanel parent = (JPanel) goToInputMetrics.getParent().getParent();
            CardLayout layout = (CardLayout) parent.getLayout();
            layout.show(parent, "Input Metrics");
        });

        // Hover effect for CTA button
        goToInputMetrics.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                goToInputMetrics.setBackground(StyleConstants.COLOR_PRIMARY_BUTTON.darker());
                goToInputMetrics.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(MouseEvent evt) {
                goToInputMetrics.setBackground(StyleConstants.COLOR_PRIMARY_BUTTON);
            }
        });
        return goToInputMetrics;
    }

    /**
     * Creates the separate My Score view.
     */
    private JPanel createMyScoreView() {
        JPanel myScoreView = new JPanel(new BorderLayout(0, 20));
        myScoreView.setBackground(StyleConstants.COLOR_CONTENT_BACKGROUND);
        myScoreView.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // --- Placeholder for the actual score ---
        JLabel scorePlaceholder = new JLabel("Your score will go here...");
        scorePlaceholder.setFont(new Font(StyleConstants.FONT_FAMILY, Font.BOLD, 24));
        scorePlaceholder.setHorizontalAlignment(JLabel.CENTER);
        scorePlaceholder.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        myScoreView.add(scorePlaceholder, BorderLayout.CENTER);
        return myScoreView;
    }

    /**
     * Creates a generic placeholder view for other tabs.
     */
    private JPanel createPlaceholderView(String labelText) {
        JPanel view = new JPanel();
        view.setBackground(StyleConstants.COLOR_CONTENT_BACKGROUND);
        view.add(new JLabel("This is the " + labelText));
        return view;
    }

    /**
     * A helper method to style our navbar buttons
     */
    private void styleNavbarButton(JButton button) {
        button.setFont(StyleConstants.FONT_NAV_BAR);
        button.setForeground(StyleConstants.COLOR_NAV_BAR_TEXT);
        button.setBackground(StyleConstants.COLOR_NAV_BAR);
        button.setPreferredSize(StyleConstants.DIM_NAV_BUTTON);

        // --- Remove all default Swing button styling ---
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);

        // --- Add hover effect ---
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(StyleConstants.COLOR_NAV_BAR_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(StyleConstants.COLOR_NAV_BAR);
            }
        });
    }

    /**
     * Converts a Color object to a hex string for HTML use.
     */
    private String getHtmlColor(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }

    public String getViewName() { return viewName; }
}