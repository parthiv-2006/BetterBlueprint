package view;

import app.StyleConstants;
import interface_adapter.ViewManagerModel;
import interface_adapter.home.HomeViewModel;
import interface_adapter.settings.SettingsViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

public class HomeView extends JPanel {
    private final String viewName = "home";
    // --- 1. DEFINE BLUE/GREEN THEME COLOR PALETTE ---
    private static final Color COLOR_NAV_BAR = new Color(37, 99, 235);        // Blue-600
    private static final Color COLOR_NAV_BAR_HOVER = new Color(29, 78, 216);  // Blue-700
    private static final Color COLOR_NAV_BAR_TEXT = Color.WHITE;
    private static final Color COLOR_CONTENT_BACKGROUND = new Color(239, 246, 255); // Light blue tint
    private static final Color COLOR_PRIMARY_BUTTON = new Color(34, 197, 94); // Green-500
    private static final Color COLOR_PRIMARY_BUTTON_HOVER = new Color(22, 163, 74); // Green-600
    private static final Color COLOR_SECONDARY_BUTTON = new Color(37, 99, 235); // Blue-600
    private static final Color COLOR_TEXT_DARK = new Color(31, 41, 55);       // Dark gray
    private static final Color COLOR_TEXT_LIGHT = new Color(107, 114, 128);   // Gray-500
    private static final Color COLOR_CARD = Color.WHITE;
    private static final Color COLOR_BORDER = new Color(191, 219, 254);       // Light blue border

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

    public HomeView(HomeViewModel homeViewModel, ViewManagerModel viewManagerModel, JPanel inputMetricsView, SettingsViewModel settingsViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.settingsViewModel = settingsViewModel;
        this.setLayout(new BorderLayout());

        // === 3. CREATE THE TOP NAVBAR ===
        JPanel navbarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        navbarPanel.setBackground(StyleConstants.COLOR_NAV_BAR);

        // Create buttons
        home = new JButton("Home");
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
        JPanel contentPanel = new JPanel(new CardLayout());
        contentPanel.setBackground(StyleConstants.COLOR_CONTENT_BACKGROUND);

        // --- Create views ---
        JPanel homeContentView = createHomeContentView();
        JPanel myScoreView = createMyScoreView();
        JPanel inputMetricsView = createPlaceholderView("Input Metrics View");
        JPanel insightsView = createPlaceholderView("Insights View");
        JPanel historyView = createPlaceholderView("History View");
        JPanel goalsView = createPlaceholderView("Goals View");

        // --- Add sub-views to the content panel ---
        contentPanel.add(homeContentView, "Home");
        contentPanel.add(myScoreView, "My Score");
        contentPanel.add(inputMetricsView, "Input Metrics");
        contentPanel.add(insightsView, "Insights");
        contentPanel.add(historyView, "History");
        contentPanel.add(goalsView, "Goals");

        // === 5. ASSEMBLE THE HOMEVIEW ===
        this.add(navbarPanel, BorderLayout.NORTH);
        this.add(contentPanel, BorderLayout.CENTER);

        // === 6. ADD ACTION LISTENERS ===
        CardLayout cardLayout = (CardLayout) contentPanel.getLayout();

        home.addActionListener(e -> cardLayout.show(contentPanel, "Home"));
        inputMetrics.addActionListener(e -> cardLayout.show(contentPanel, "Input Metrics"));
        // Account Settings now navigates to the separate SettingsView
        accountSettings.addActionListener(e -> {
            // Get current username from HomeViewModel
            String currentUsername = homeViewModel.getState().getUsername();

            // Set it in SettingsViewModel state
            interface_adapter.settings.SettingsState settingsState = settingsViewModel.getState();
            settingsState.setUsername(currentUsername);
            settingsViewModel.setState(settingsState);

            // Switch to settings view
            viewManagerModel.setState("settings");
            viewManagerModel.firePropertyChange();
        });
        myScore.addActionListener(e -> cardLayout.show(contentPanel, "My Score"));
        insights.addActionListener(e -> cardLayout.show(contentPanel, "Insights"));
        history.addActionListener(e -> cardLayout.show(contentPanel, "History"));
        goals.addActionListener(e -> cardLayout.show(contentPanel, "Goals"));

        cardLayout.show(contentPanel, "Home");

        // Provide navigation context to InputMetricsView if it's the right type
        if (inputMetricsView instanceof InputMetricsView) {
            ((InputMetricsView) inputMetricsView).setHomeNavigation(mainCardLayout, mainContentPanel);
        }
        private JPanel createHomeContentView() {
        JPanel homeView = new JPanel();
        homeView.setLayout(new BoxLayout(homeView, BoxLayout.Y_AXIS));
        homeView.setBackground(StyleConstants.COLOR_CONTENT_BACKGROUND);
        homeView.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));

        JPanel logoAndDescriptionPanel = new JPanel();
        logoAndDescriptionPanel.setLayout(new BoxLayout(logoAndDescriptionPanel, BoxLayout.X_AXIS));
        logoAndDescriptionPanel.setBackground(StyleConstants.COLOR_CONTENT_BACKGROUND);
        logoAndDescriptionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

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

        // --- A. Create the "Home" page ---
        JPanel homeView = createHomeContentView();
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(StyleConstants.COLOR_CONTENT_BACKGROUND);


            // --- C. Create other placeholder views ---
        // inputMetricsView is passed as parameter (actual view, not placeholder)
        JPanel myScoreView = createMyScorePlaceholderView();
        JPanel insightsView = createPlaceholderView("Insights");
        JPanel historyView = createPlaceholderView("History");
        JPanel accountSettingsView = createPlaceholderView("Settings");
        JPanel goalsView = createPlaceholderView("Goals");
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

        JButton goToInputMetrics = styleAndAddGoToInputMetricsButton();

        homeView.add(Box.createVerticalGlue());
        homeView.add(logoAndDescriptionPanel);
        homeView.add(Box.createRigidArea(new Dimension(0, 50)));
        homeView.add(goToInputMetrics);
        homeView.add(Box.createVerticalGlue());

        return homeView;
    }

    private JButton styleAndAddGoToInputMetricsButton() {
        JButton goToInputMetrics = new JButton("Start Tracking Now");
        goToInputMetrics.setFont(StyleConstants.FONT_CTA_BUTTON);
        goToInputMetrics.setForeground(Color.WHITE);
        goToInputMetrics.setBackground(StyleConstants.COLOR_PRIMARY_BUTTON);
        goToInputMetrics.setFocusPainted(false);
        goToInputMetrics.setBorderPainted(false);
        goToInputMetrics.setOpaque(true);
        goToInputMetrics.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- Add all views to the main CardLayout panel ---
        mainContentPanel.add(homeView, "Home");
        mainContentPanel.add(inputMetricsView, "Metrics");
        mainContentPanel.add(myScoreView, "Score");
        mainContentPanel.add(insightsView, "Insights");
        mainContentPanel.add(historyView, "History");
        mainContentPanel.add(accountSettingsView, "Settings");
        mainContentPanel.add(goalsView, "Goals");
        mainContentPanel.add(goalsView, "Goals");

        // === 5. ASSEMBLE THE HOMEVIEW ===
        this.add(navbarPanel, BorderLayout.NORTH);
        this.add(mainContentPanel, BorderLayout.CENTER);

        // === 6. ADD ACTION LISTENERS ===
        home.addActionListener(e -> mainCardLayout.show(mainContentPanel, "Home"));
        inputMetrics.addActionListener(e -> mainCardLayout.show(mainContentPanel, "Metrics"));
        myScore.addActionListener(e -> mainCardLayout.show(mainContentPanel, "Score"));
        insights.addActionListener(e -> mainCardLayout.show(mainContentPanel, "Insights"));
        history.addActionListener(e -> mainCardLayout.show(mainContentPanel, "History"));
        accountSettings.addActionListener(e -> mainCardLayout.show(mainContentPanel, "Settings"));
        goals.addActionListener(e -> mainCardLayout.show(mainContentPanel, "Goals"));

        // Set "Home" as the default homepage
        mainCardLayout.show(mainContentPanel, "Home");
    }

    /**
     * Creates a navigation button with the given text (including icon emoji)
     * @param text The button text with icon
     * @return The created JButton
     */
    private JButton createNavButton(String text) {
        return new JButton(text);
        goToInputMetrics.setMaximumSize(new Dimension(220, 50));
        goToInputMetrics.setPreferredSize(new Dimension(220, 50));

        goToInputMetrics.addActionListener(e -> {
            JPanel parent = (JPanel) goToInputMetrics.getParent().getParent();
            CardLayout layout = (CardLayout) parent.getLayout();
            layout.show(parent, "Input Metrics");
        });

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

    private JPanel createMyScoreView() {
        JPanel myScoreView = new JPanel(new BorderLayout(0, 20));
        myScoreView.setBackground(StyleConstants.COLOR_CONTENT_BACKGROUND);
        myScoreView.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel scorePlaceholder = new JLabel("Your score will go here...");
        scorePlaceholder.setFont(new Font(StyleConstants.FONT_FAMILY, Font.BOLD, 24));
        scorePlaceholder.setHorizontalAlignment(JLabel.CENTER);
        scorePlaceholder.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        myScoreView.add(scorePlaceholder, BorderLayout.CENTER);
        return myScoreView;
    }

    private JPanel createPlaceholderView(String labelText) {
        JPanel view = new JPanel();
        view.setBackground(StyleConstants.COLOR_CONTENT_BACKGROUND);
        view.add(new JLabel("This is the " + labelText));
        return view;
    }

    /**
     * A helper method to style our navbar buttons
     * @param button The JButton to style.
     */
    private void styleNavbarButton(JButton button) {
        button.setFont(StyleConstants.FONT_NAV_BAR);
        button.setForeground(StyleConstants.COLOR_NAV_BAR_TEXT);
        button.setBackground(StyleConstants.COLOR_NAV_BAR);
        button.setPreferredSize(StyleConstants.DIM_NAV_BUTTON);
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
                button.setBackground(StyleConstants.COLOR_NAV_BAR_HOVER);
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 3, 0, COLOR_PRIMARY_BUTTON),
                        BorderFactory.createEmptyBorder(5, 12, 5, 12)
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(StyleConstants.COLOR_NAV_BAR);
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
    private JButton styleAndAddGoToInputMetricsButton() {
        JButton goToInputMetrics = new JButton("Start Tracking Now");
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
    private String getHtmlColor(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
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
        JButton goToInputMetrics = styleAndAddGoToInputMetricsButton();

        cardPanel.add(logoAndDescriptionPanel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        cardPanel.add(goToInputMetrics);

        homeView.add(Box.createVerticalGlue());
        homeView.add(cardPanel);
        homeView.add(Box.createVerticalGlue());

        return homeView;
    }

    /**
     * Creates a placeholder view for My Score.
     */
    private JPanel createMyScorePlaceholderView() {
        JPanel myScoreView = new JPanel();
        myScoreView.setLayout(new BoxLayout(myScoreView, BoxLayout.Y_AXIS));
        myScoreView.setBackground(COLOR_CONTENT_BACKGROUND);
        myScoreView.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));

        // Create card panel
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(COLOR_CARD);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1, true),
                BorderFactory.createEmptyBorder(40, 50, 40, 50)
        ));
        cardPanel.setMaximumSize(new Dimension(600, 400));
        cardPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Title
        JLabel titleLabel = new JLabel("Your Health Score");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(COLOR_TEXT_DARK);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Score placeholder
        JLabel scorePlaceholder = new JLabel("No score available yet");
        scorePlaceholder.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        scorePlaceholder.setForeground(COLOR_TEXT_LIGHT);
        scorePlaceholder.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel instructionLabel = new JLabel("Start tracking your metrics to see your score");
        instructionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        instructionLabel.setForeground(COLOR_TEXT_LIGHT);
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        cardPanel.add(titleLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        cardPanel.add(scorePlaceholder);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        cardPanel.add(instructionLabel);

        myScoreView.add(Box.createVerticalGlue());
        myScoreView.add(cardPanel);
        myScoreView.add(Box.createVerticalGlue());

        return myScoreView;
    }

    /**
     * Creates a generic placeholder view for other tabs.
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
        return "home"; }
}
