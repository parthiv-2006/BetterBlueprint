package view;

import app.StyleConstants;
import interface_adapter.ViewManagerModel;
import interface_adapter.home.HomeViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HomeView extends JPanel {
    private final String viewName = "home";

    public final JButton home;
    public final JButton inputMetrics;
    public final JButton accountSettings;
    public final JButton myScore;
    public final JButton insights;
    public final JButton history;
    public final JButton goals;

    private final ViewManagerModel viewManagerModel;

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
        goals = new JButton("Goals");

        // Style all 7 buttons
        styleNavbarButton(home);
        styleNavbarButton(inputMetrics);
        styleNavbarButton(accountSettings);
        styleNavbarButton(myScore);
        styleNavbarButton(insights);
        styleNavbarButton(history);
        styleNavbarButton(goals);

        // Add buttons to navbar
        navbarPanel.add(home);
        navbarPanel.add(inputMetrics);
        navbarPanel.add(accountSettings);
        navbarPanel.add(myScore);
        navbarPanel.add(insights);
        navbarPanel.add(history);
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
            viewManagerModel.setState("settings");
            viewManagerModel.firePropertyChange();
        });
        myScore.addActionListener(e -> cardLayout.show(contentPanel, "My Score"));
        insights.addActionListener(e -> cardLayout.show(contentPanel, "Insights"));
        history.addActionListener(e -> cardLayout.show(contentPanel, "History"));
        goals.addActionListener(e -> cardLayout.show(contentPanel, "Goals"));

        cardLayout.show(contentPanel, "Home");
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

        logoAndDescriptionPanel.add(Box.createRigidArea(new Dimension(30, 0)));

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

    private void styleNavbarButton(JButton button) {
        button.setFont(StyleConstants.FONT_NAV_BAR);
        button.setForeground(StyleConstants.COLOR_NAV_BAR_TEXT);
        button.setBackground(StyleConstants.COLOR_NAV_BAR);
        button.setPreferredSize(StyleConstants.DIM_NAV_BUTTON);

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);

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

    private String getHtmlColor(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }

    public String getViewName() { return viewName; }
}
