package view;

import app.StyleConstants;
import interface_adapter.ViewManagerModel;
import interface_adapter.home.HomeViewModel;
import interface_adapter.settings.SettingsViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HomeView extends JPanel {
    private final String viewName = "home";

    private static final Color COLOR_NAV_BAR = new Color(37, 99, 235);
    private static final Color COLOR_NAV_BAR_HOVER = new Color(29, 78, 216);
    private static final Color COLOR_NAV_BAR_TEXT = Color.WHITE;
    private static final Color COLOR_CONTENT_BACKGROUND = new Color(239, 246, 255);
    private static final Color COLOR_PRIMARY_BUTTON = new Color(34, 197, 94);
    private static final Color COLOR_PRIMARY_BUTTON_HOVER = new Color(22, 163, 74);
    private static final Color COLOR_TEXT_DARK = new Color(31, 41, 55);
    private static final Color COLOR_TEXT_LIGHT = new Color(107, 114, 128);
    private static final Color COLOR_CARD = Color.WHITE;
    private static final Color COLOR_BORDER = new Color(191, 219, 254);

    public final JButton home;
    public final JButton inputMetrics;
    public final JButton accountSettings;
    public final JButton myScore;
    public final JButton insights;
    public final JButton history;
    public final JButton goals;

    private final CardLayout cardLayout;
    private final JPanel contentPanel;

    private final ViewManagerModel viewManagerModel;
    private final SettingsViewModel settingsViewModel;

    public HomeView(HomeViewModel homeViewModel, ViewManagerModel viewManagerModel, JPanel inputMetricsView, SettingsViewModel settingsViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.settingsViewModel = settingsViewModel;
        this.setLayout(new BorderLayout());

        // Create navbar
        JPanel navbarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        navbarPanel.setBackground(StyleConstants.COLOR_NAV_BAR);

        home = createNavButton("Home");
        inputMetrics = createNavButton("Metrics");
        myScore = createNavButton("Score");
        insights = createNavButton("Insights");
        history = createNavButton("History");
        accountSettings = createNavButton("Settings");
        goals = createNavButton("Goals");

        navbarPanel.add(home);
        navbarPanel.add(inputMetrics);
        navbarPanel.add(myScore);
        navbarPanel.add(insights);
        navbarPanel.add(history);
        navbarPanel.add(accountSettings);
        navbarPanel.add(goals);

        // Create content panel with CardLayout
        contentPanel = new JPanel();
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);
        contentPanel.setBackground(COLOR_CONTENT_BACKGROUND);

        // Create views
        JPanel homeContentView = createHomeContentView();
        JPanel myScoreView = createMyScorePlaceholderView();
        JPanel insightsView = createPlaceholderView("Insights");
        JPanel historyView = createMyHistoryPlaceholderView();
        JPanel accountSettingsView = createPlaceholderView("Settings");
        JPanel goalsView = createPlaceholderView("Goals");

        // Add views to content panel
        contentPanel.add(homeContentView, "Home");
        contentPanel.add(inputMetricsView, "Metrics");
        contentPanel.add(myScoreView, "Score");
        contentPanel.add(insightsView, "Insights");
        contentPanel.add(historyView, "History");
        contentPanel.add(goalsView, "Goals");

        // Assemble HomeView
        this.add(navbarPanel, BorderLayout.NORTH);
        this.add(contentPanel, BorderLayout.CENTER);

        // Add action listeners
        home.addActionListener(e -> cardLayout.show(contentPanel, "Home"));
        inputMetrics.addActionListener(e -> cardLayout.show(contentPanel, "Metrics"));
        myScore.addActionListener(e -> cardLayout.show(contentPanel, "Score"));
        insights.addActionListener(e -> cardLayout.show(contentPanel, "Insights"));
        history.addActionListener(e -> cardLayout.show(contentPanel, "History"));
        goals.addActionListener(e -> cardLayout.show(contentPanel, "Goals"));

        accountSettings.addActionListener(e -> {
            String currentUsername = homeViewModel.getState().getUsername();
            interface_adapter.settings.SettingsState settingsState = settingsViewModel.getState();
            settingsState.setUsername(currentUsername);
            settingsViewModel.setState(settingsState);
            viewManagerModel.setState("settings");
            viewManagerModel.firePropertyChange();
        });

        cardLayout.show(contentPanel, "Home");

        if (inputMetricsView instanceof InputMetricsView) {
            ((InputMetricsView) inputMetricsView).setHomeNavigation(cardLayout, contentPanel);
        }
    }

    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setFont(StyleConstants.FONT_NAV_BAR);
        button.setForeground(COLOR_NAV_BAR_TEXT);
        button.setBackground(StyleConstants.COLOR_NAV_BAR);
        button.setPreferredSize(StyleConstants.DIM_NAV_BUTTON);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 3, 0, StyleConstants.COLOR_NAV_BAR),
                BorderFactory.createEmptyBorder(5, 12, 5, 12)
        ));

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
                        BorderFactory.createMatteBorder(0, 0, 3, 0, StyleConstants.COLOR_NAV_BAR),
                        BorderFactory.createEmptyBorder(5, 12, 5, 12)
                ));
            }
        });

        return button;
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

        goToInputMetrics.addActionListener(e -> cardLayout.show(contentPanel, "Metrics"));

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

    private JPanel createHomeContentView() {
        JPanel homeView = new JPanel();
        homeView.setLayout(new BoxLayout(homeView, BoxLayout.Y_AXIS));
        homeView.setBackground(COLOR_CONTENT_BACKGROUND);
        homeView.setBorder(BorderFactory.createEmptyBorder(80, 80, 80, 80));

        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(COLOR_CARD);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1, true),
                BorderFactory.createEmptyBorder(40, 50, 40, 50)
        ));
        cardPanel.setMaximumSize(new Dimension(800, 500));
        cardPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel logoAndDescriptionPanel = new JPanel();
        logoAndDescriptionPanel.setLayout(new BoxLayout(logoAndDescriptionPanel, BoxLayout.X_AXIS));
        logoAndDescriptionPanel.setBackground(COLOR_CARD);
        logoAndDescriptionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

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

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(COLOR_CARD);

        JLabel welcomeTitle = new JLabel("Welcome to BetterBlueprint");
        welcomeTitle.setFont(StyleConstants.FONT_TITLE);
        welcomeTitle.setForeground(COLOR_TEXT_DARK);
        welcomeTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        String descriptionText = "<html><body style='width: 380px; color: #6B7280; line-height: 1.6;'>" +
                "Track your daily metrics, calculate personalized health scores, " +
                "and receive AI-powered insights to improve your well-being." +
                "</body></html>";
        JLabel descriptionLabel = new JLabel(descriptionText);
        descriptionLabel.setFont(StyleConstants.FONT_BODY);
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

    private JButton createCTAButton() {
        JButton button = new JButton("Start Tracking Now");
        button.setFont(StyleConstants.FONT_CTA_BUTTON);
        button.setForeground(Color.WHITE);
        button.setBackground(COLOR_PRIMARY_BUTTON);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(240, 55));
        button.setPreferredSize(new Dimension(240, 55));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_PRIMARY_BUTTON, 2, true),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        button.addActionListener(e -> cardLayout.show(contentPanel, "Metrics"));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(COLOR_PRIMARY_BUTTON_HOVER);
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(COLOR_PRIMARY_BUTTON_HOVER, 2, true),
                        BorderFactory.createEmptyBorder(10, 20, 10, 20)
                ));
            }
            public void mouseExited(MouseEvent evt) {
                button.setBackground(COLOR_PRIMARY_BUTTON);
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(COLOR_PRIMARY_BUTTON, 2, true),
                        BorderFactory.createEmptyBorder(10, 20, 10, 20)
                ));
            }
        });

        return button;
    }

    private JPanel createMyScorePlaceholderView() {
        JPanel myScoreView = new JPanel();
        myScoreView.setLayout(new BoxLayout(myScoreView, BoxLayout.Y_AXIS));
        myScoreView.setBackground(COLOR_CONTENT_BACKGROUND);
        myScoreView.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));

        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(COLOR_CARD);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1, true),
                BorderFactory.createEmptyBorder(40, 50, 40, 50)
        ));
        cardPanel.setMaximumSize(new Dimension(600, 400));
        cardPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("Your Health Score");
        titleLabel.setFont(StyleConstants.FONT_TITLE);
        titleLabel.setForeground(COLOR_TEXT_DARK);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel scorePlaceholder = new JLabel("No score available yet");
        scorePlaceholder.setFont(StyleConstants.FONT_BODY);
        scorePlaceholder.setForeground(COLOR_TEXT_LIGHT);
        scorePlaceholder.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel instructionLabel = new JLabel("Start tracking your metrics to see your score");
        instructionLabel.setFont(StyleConstants.FONT_BODY);
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

    private JPanel createMyHistoryPlaceholderView() {
        JPanel myHistoryView = new JPanel();
        myHistoryView.setLayout(new BoxLayout(myHistoryView, BoxLayout.Y_AXIS));
        myHistoryView.setBackground(COLOR_CONTENT_BACKGROUND);
        myHistoryView.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));

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
        JLabel titleLabel = new JLabel("Your Charts");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(COLOR_TEXT_DARK);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Score placeholder
        JLabel historyPlaceholder = new JLabel("Not enough data");
        historyPlaceholder.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        historyPlaceholder.setForeground(COLOR_TEXT_LIGHT);
        historyPlaceholder.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton styledGoToInputMetricsButton = styleAndAddGoToInputMetricsButton(2);

        cardPanel.add(titleLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        cardPanel.add(historyPlaceholder);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        cardPanel.add(styledGoToInputMetricsButton);

        myHistoryView.add(Box.createVerticalGlue());
        myHistoryView.add(cardPanel);
        myHistoryView.add(Box.createVerticalGlue());

        return myHistoryView;
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
        titleLabel.setFont(StyleConstants.FONT_TITLE);
        titleLabel.setForeground(COLOR_TEXT_DARK);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel comingSoonLabel = new JLabel("Coming Soon");
        comingSoonLabel.setFont(StyleConstants.FONT_BODY);
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
        return viewName;
    }
}
