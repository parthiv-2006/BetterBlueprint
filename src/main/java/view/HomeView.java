package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.home.HomeViewModel;
import interface_adapter.settings.SettingsViewModel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import use_case.healthHistory.healthHistoryOutputBoundary;
import use_case.healthHistory.healthHistoryOutputData;
import use_case.healthHistory.healthMetricRecord;
import use_case.healthHistory.healthHistoryInteractor;

public class HomeView extends JPanel {
    // --- 1. DEFINE BLUE/GREEN THEME COLOR PALETTE ---
    private static final Color COLOR_NAV_BAR = new Color(37, 99, 235); // Blue-600
    private static final Color COLOR_NAV_BAR_HOVER = new Color(29, 78, 216); // Blue-700
    private static final Color COLOR_NAV_BAR_TEXT = Color.WHITE;
    private static final Color COLOR_CONTENT_BACKGROUND = new Color(239, 246, 255); // Light blue tint
    private static final Color COLOR_PRIMARY_BUTTON = new Color(34, 197, 94); // Green-500
    private static final Color COLOR_PRIMARY_BUTTON_HOVER = new Color(22, 163, 74); // Green-600
    private static final Color COLOR_SECONDARY_BUTTON = new Color(37, 99, 235); // Blue-600
    private static final Color COLOR_TEXT_DARK = new Color(31, 41, 55); // Dark gray
    private static final Color COLOR_TEXT_LIGHT = new Color(107, 114, 128); // Gray-500
    private static final Color COLOR_CARD = Color.WHITE;
    private static final Color COLOR_BORDER = new Color(191, 219, 254); // Light blue border

    // --- 2. DEFINE UI COMPONENTS ---
    public final JButton home;
    public final JButton inputMetrics;
    public final JButton accountSettings;
    public final JButton myScore;
    public final JButton insights;
    public final JButton history;
    public final JButton goals;
    private final CardLayout mainCardLayout;
    private final JPanel mainContentPanel;
    private final ViewManagerModel viewManagerModel;
    private final SettingsViewModel settingsViewModel;
    private final HomeViewModel homeViewModel;
    private final JPanel healthInsightsView;

    public HomeView(HomeViewModel homeViewModel, ViewManagerModel viewManagerModel, JPanel inputMetricsView, SettingsViewModel settingsViewModel, JPanel myScoreView, JPanel healthInsightsView, JPanel goalsView) {
        this.homeViewModel = homeViewModel;
        this.viewManagerModel = viewManagerModel;
        this.settingsViewModel = settingsViewModel;
        this.healthInsightsView = healthInsightsView;
        this.setLayout(new BorderLayout());

        // === 3. CREATE THE TOP NAVBAR ===
        JPanel navbarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        navbarPanel.setBackground(COLOR_NAV_BAR);

        // Create buttons
        home = createNavButton("Home");
        inputMetrics = createNavButton("Metrics");
        myScore = createNavButton("Score");
        insights = createNavButton("Insights");
        history = createNavButton("History");
        accountSettings = createNavButton("Settings");
        goals = createNavButton("Goals");

        // Style all buttons
        styleNavbarButton(home);
        styleNavbarButton(inputMetrics);
        styleNavbarButton(myScore);
        styleNavbarButton(insights);
        styleNavbarButton(history);
        styleNavbarButton(accountSettings);
        styleNavbarButton(goals);

        // Add buttons to the navbar
        navbarPanel.add(home);
        navbarPanel.add(inputMetrics);
        navbarPanel.add(myScore);
        navbarPanel.add(insights);
        navbarPanel.add(history);
        navbarPanel.add(accountSettings);
        navbarPanel.add(goals);

        // === 4. CREATE THE CENTER CONTENT PANEL ===
        mainCardLayout = new CardLayout();
        mainContentPanel = new JPanel(mainCardLayout);
        mainContentPanel.setBackground(COLOR_CONTENT_BACKGROUND);

        // Provide navigation context to InputMetricsView if it's the right type
        if (inputMetricsView instanceof InputMetricsView) {
            ((InputMetricsView) inputMetricsView).setHomeNavigation(mainCardLayout, mainContentPanel);
        }

        // Provide navigation context to GoalsView if it's the right type
        if (goalsView instanceof GoalsView) {
            ((GoalsView) goalsView)
                    .setHomeNavigation(mainCardLayout, mainContentPanel);
        }

        // --- A. Create the "Home" page ---
        JPanel homeView = createHomeContentView();

        // --- C. Create other placeholder views ---
        // inputMetricsView is passed as parameter (actual view, not placeholder)
        JPanel myScorePanel = myScoreView;

        // Use the actual HealthInsightsView passed as parameter instead of placeholder
        JPanel insightsView = healthInsightsView;

        // Set up navigation for HealthInsightsView
        if (healthInsightsView instanceof HealthInsightsView) {
            HealthInsightsView insightsViewInstance = (HealthInsightsView) healthInsightsView;
            insightsViewInstance.setHomeNavigation(mainCardLayout, mainContentPanel);

            // PASS THE CURRENT USERNAME
            String currentUsername = homeViewModel.getState().getUsername();
            System.out.println("HomeView: Setting current user for HealthInsights: " + currentUsername);
            insightsViewInstance.setCurrentUser(currentUsername);
        }

        // Create HealthHistoryView (concrete type so we can update it)
        final HealthHistoryView historyView = new HealthHistoryView();

        // Create a simple presenter that forwards interactor output into the view
        healthHistoryOutputBoundary directPresenter = new healthHistoryOutputBoundary() {
            @Override
            public void prepareSuccessView(healthHistoryOutputData data) {
                List<String> formatted = new ArrayList<>();
                List<Double> values = new ArrayList<>();
                List<healthMetricRecord> records = data.getRecords();
                DateTimeFormatter iso = DateTimeFormatter.ISO_LOCAL_DATE;
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM-dd");

                if (records != null) {
                    for (healthMetricRecord r : records) {
                        String rawDate = r.getDate().toString();
                        try {
                            formatted.add(java.time.LocalDate.parse(rawDate, iso).format(fmt));
                        } catch (Exception ex) {
                            formatted.add(rawDate);
                        }
                        values.add(r.getValue());
                    }
                }
                historyView.updateData(formatted, values, data.getMetricType());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                historyView.updateData(new ArrayList<>(), new ArrayList<>(), "");
            }
        };

        final healthHistoryInteractor historyInteractor = new healthHistoryInteractor(null, directPresenter);

        // Build a history panel that contains controls + the chart
        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.setBackground(COLOR_CONTENT_BACKGROUND);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        controls.setBackground(COLOR_CONTENT_BACKGROUND);

        String[] metrics = {"Water Intake", "Calories", "Exercise Minutes" };
        JComboBox<String> metricCombo = new JComboBox<>(metrics);
        metricCombo.setSelectedItem("Calories");

        String[] ranges = { "week", "month", "year" };
        JComboBox<String> rangeCombo = new JComboBox<>(ranges);
        rangeCombo.setSelectedItem("week");

        controls.add(new JLabel("Metric:"));
        controls.add(metricCombo);
        controls.add(Box.createHorizontalStrut(16));
        controls.add(new JLabel("Range:"));
        controls.add(rangeCombo);

        historyPanel.add(controls, BorderLayout.NORTH);
        historyPanel.add(historyView, BorderLayout.CENTER);

        // refresh action: read combos and request data
        String userId = "user1";
        Runnable refresh = () -> {
            String selectedMetric = (String) metricCombo.getSelectedItem();
            String metricKey = mapMetricSelectionToKey(selectedMetric);
            String selectedRange = (String) rangeCombo.getSelectedItem();
            historyInteractor.fetchHistory(metricKey, selectedRange, userId);
        };

        metricCombo.addActionListener(e -> refresh.run());
        rangeCombo.addActionListener(e -> refresh.run());

        // Initial load
        refresh.run();

        JPanel accountSettingsView = createPlaceholderView("Settings");
//        JPanel goalsView = createPlaceholderView("Goals");

        // --- Add all views to the main CardLayout panel ---
        mainContentPanel.add(homeView, "Home");
        mainContentPanel.add(inputMetricsView, "Metrics");
        mainContentPanel.add(myScoreView, "Score");
        mainContentPanel.add(insightsView, "Insights"); // Use actual insights view
        mainContentPanel.add(historyPanel, "History");
        mainContentPanel.add(accountSettingsView, "Settings");
        mainContentPanel.add(goalsView, "Goals");

        // === 5. ASSEMBLE THE HOMEVIEW ===
        this.add(navbarPanel, BorderLayout.NORTH);
        this.add(mainContentPanel, BorderLayout.CENTER);

        // === 6. ADD ACTION LISTENERS ===
        home.addActionListener(e -> mainCardLayout.show(mainContentPanel, "Home"));
        inputMetrics.addActionListener(e -> mainCardLayout.show(mainContentPanel, "Metrics"));
        myScore.addActionListener(e -> mainCardLayout.show(mainContentPanel, "Score"));
        insights.addActionListener(e -> mainCardLayout.show(mainContentPanel, "Insights"));

        // Update chart data on-demand when History is clicked, then show it
        history.addActionListener(e -> {
            // When History nav is clicked, refresh from current selections and show panel
            refresh.run();
            mainCardLayout.show(mainContentPanel, "History");
        });

        accountSettings.addActionListener(e -> {
            String currentUsername = homeViewModel.getState().getUsername();
            interface_adapter.settings.SettingsState settingsState = settingsViewModel.getState();
            settingsState.setUsername(currentUsername);
            settingsViewModel.setState(settingsState);
            viewManagerModel.setState("settings");
            viewManagerModel.firePropertyChange();
        });

        goals.addActionListener(e -> {
            try {
                mainCardLayout.show(mainContentPanel, "Goals");
            } catch (Exception ex) {
                System.err.println("HomeView: failed when clicking Goals: " + ex.getMessage());
            }
        });


        // Set "Home" as the default homepage
        mainCardLayout.show(mainContentPanel, "Home");

        homeViewModel.addPropertyChangeListener(evt -> {
            if (healthInsightsView instanceof HealthInsightsView) {
                String newUsername = homeViewModel.getState().getUsername();
                System.out.println("HomeView: User changed to: " + newUsername);
                ((HealthInsightsView) healthInsightsView).setCurrentUser(newUsername);
            }
        });
    }

    /**
     * Creates a navigation button with the given text (including icon emoji)
     * @param text The button text with icon
     * @return The created JButton
     */
    private JButton createNavButton(String text) {
        return new JButton(text);
    }

    /**
     * A helper method to style our navbar buttons
     * @param button The JButton to style.
     */
    private void styleNavbarButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(COLOR_NAV_BAR_TEXT);
        button.setBackground(COLOR_NAV_BAR);
        button.setPreferredSize(new Dimension(120, 55));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // --- Remove all default Swing button styling ---
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 3, 0, COLOR_NAV_BAR),
                BorderFactory.createEmptyBorder(5, 12, 5, 12)
        ));

        // --- Add hover effect ---
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(COLOR_NAV_BAR_HOVER);
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 3, 0, COLOR_PRIMARY_BUTTON),
                        BorderFactory.createEmptyBorder(5, 12, 5, 12)
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(COLOR_NAV_BAR);
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 3, 0, COLOR_NAV_BAR),
                        BorderFactory.createEmptyBorder(5, 12, 5, 12)
                ));
            }
        });
    }

    /**
     * Creates and styles the "Start Tracking Now" button and adds its action listener.
     * This method resolves the long surrounding method warning from the IDE.
     */
    private JButton styleAndAddGoToInputMetricsButton(int n) {
        /**
         * If n = 1, then the button displays 'Start Tracking Now'
         * if n = 2, then the button displays 'Input More Data'
         */
        JButton goToInputMetrics;
        if (n == 1) {
            goToInputMetrics = new JButton("Start Tracking Now");
        } else {
            goToInputMetrics = new JButton("Input More Data");
        }

        goToInputMetrics.setFont(new Font("Segoe UI", Font.BOLD, 16));
        goToInputMetrics.setForeground(Color.WHITE);
        goToInputMetrics.setBackground(COLOR_PRIMARY_BUTTON);
        goToInputMetrics.setFocusPainted(false);
        goToInputMetrics.setBorderPainted(false);
        goToInputMetrics.setOpaque(true);
        goToInputMetrics.setAlignmentX(Component.CENTER_ALIGNMENT);
        goToInputMetrics.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Make button bigger with rounded appearance
        goToInputMetrics.setMaximumSize(new Dimension(240, 55));
        goToInputMetrics.setPreferredSize(new Dimension(240, 55));
        goToInputMetrics.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_PRIMARY_BUTTON, 2, true),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        goToInputMetrics.addActionListener(e -> mainCardLayout.show(mainContentPanel, "Metrics"));

        // Hover effect for CTA button
        goToInputMetrics.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                goToInputMetrics.setBackground(COLOR_PRIMARY_BUTTON_HOVER);
                goToInputMetrics.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(COLOR_PRIMARY_BUTTON_HOVER, 2, true),
                        BorderFactory.createEmptyBorder(10, 20, 10, 20)
                ));
            }

            public void mouseExited(MouseEvent evt) {
                goToInputMetrics.setBackground(COLOR_PRIMARY_BUTTON);
                goToInputMetrics.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(COLOR_PRIMARY_BUTTON, 2, true),
                        BorderFactory.createEmptyBorder(10, 20, 10, 20)
                ));
            }
        });

        return goToInputMetrics;
    }

    /**
     * Creates the main content panel for the Home view, including logo, description, and CTA.
     */
    private JPanel createHomeContentView() {
        JPanel homeView = new JPanel();
        homeView.setLayout(new BoxLayout(homeView, BoxLayout.Y_AXIS));
        homeView.setBackground(COLOR_CONTENT_BACKGROUND);
        homeView.setBorder(BorderFactory.createEmptyBorder(80, 80, 80, 80));

        // -- Create a centered card panel --
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(COLOR_CARD);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1, true),
                BorderFactory.createEmptyBorder(40, 50, 40, 50)
        ));
        cardPanel.setMaximumSize(new Dimension(800, 500));
        cardPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // -- Panel for Logo and Description (side-by-side)
        JPanel logoAndDescriptionPanel = new JPanel();
        logoAndDescriptionPanel.setLayout(new BoxLayout(logoAndDescriptionPanel, BoxLayout.X_AXIS));
        logoAndDescriptionPanel.setBackground(COLOR_CARD);
        logoAndDescriptionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add Logo
        try {
            java.net.URL logoUrl = getClass().getResource("/BetterBlueprint.png");
            if (logoUrl != null) {
                ImageIcon logoIcon = new ImageIcon(logoUrl);
                Image scaledImage = logoIcon.getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH);
                JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));
                logoLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
                logoAndDescriptionPanel.add(logoLabel);
            } else {
                logoAndDescriptionPanel.add(new JLabel("Logo Not Found"));
            }
        } catch (Exception e) {
            logoAndDescriptionPanel.add(new JLabel("Logo Error"));
        }

        logoAndDescriptionPanel.add(Box.createRigidArea(new Dimension(40, 0)));

        // -- Right side of header: Welcome Title and Description
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(COLOR_CARD);

        JLabel welcomeTitle = new JLabel("Welcome to BetterBlueprint");
        welcomeTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        welcomeTitle.setForeground(COLOR_TEXT_DARK);
        welcomeTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        String descriptionText = "<html><body style='width: 380px; color: #6B7280; line-height: 1.6;'>" +
                "Track your daily metrics, calculate personalized health scores, " +
                "and receive AI-powered insights to improve your well-being." +
                "</body></html>";

        JLabel descriptionLabel = new JLabel(descriptionText);
        descriptionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(welcomeTitle);
        textPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        textPanel.add(descriptionLabel);

        logoAndDescriptionPanel.add(textPanel);

        // -- Button to guide the user to Input Metrics --
        JButton goToInputMetrics2 = styleAndAddGoToInputMetricsButton(1);

        cardPanel.add(logoAndDescriptionPanel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        cardPanel.add(goToInputMetrics2);

        homeView.add(Box.createVerticalGlue());
        homeView.add(cardPanel);
        homeView.add(Box.createVerticalGlue());

        return homeView;
    }

    /**
     * Creates a placeholder view for other tabs.
     * @param labelText The text to display in the placeholder.
     */
    private JPanel createPlaceholderView(String labelText) {
        JPanel view = new JPanel();
        view.setLayout(new BoxLayout(view, BoxLayout.Y_AXIS));
        view.setBackground(COLOR_CONTENT_BACKGROUND);
        view.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));

        // Create card panel
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(COLOR_CARD);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1, true),
                BorderFactory.createEmptyBorder(40, 50, 40, 50)
        ));
        cardPanel.setMaximumSize(new Dimension(600, 300));
        cardPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel(labelText);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(COLOR_TEXT_DARK);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel comingSoonLabel = new JLabel("Coming Soon");
        comingSoonLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        comingSoonLabel.setForeground(COLOR_TEXT_LIGHT);
        comingSoonLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        cardPanel.add(titleLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        cardPanel.add(comingSoonLabel);

        view.add(Box.createVerticalGlue());
        view.add(cardPanel);
        view.add(Box.createVerticalGlue());

        return view;
    }

    public String getViewName() {
        return "home";
    }

    // Helper to map user-friendly selection to interactor metric key
    private static String mapMetricSelectionToKey(String selection) {
        if (selection == null) return "calories";
        switch (selection) {
            case "Steps": return "steps";
            case "Water Intake": return "water";
            case "Exercise Minutes": return "exercise";
            case "Calories": default: return "calories";
        }
    }

    // === ADD THE NEW METHOD HERE ===
    public void updateCurrentUser(String username) {
        homeViewModel.getState().setUsername(username);
        if (healthInsightsView instanceof HealthInsightsView) {
            ((HealthInsightsView) healthInsightsView).setCurrentUser(username);
        }
    }
}