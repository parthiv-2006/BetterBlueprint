package view;

import interface_adapter.home.HomeViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

public class HomeView extends JPanel implements ActionListener, PropertyChangeListener {
    private final String viewName = "home";
    private final HomeViewModel homeViewModel;
    // --- 1. DEFINE A NEW HEALTH APP COLOR PALETTE ---
    private final Color COLOR_NAV_BAR = new Color(22, 160, 133);       // Main Teal/Green
    private final Color COLOR_NAV_BAR_HOVER = new Color(20, 140, 113);  // Darker Teal on hover
    private final Color COLOR_NAV_BAR_TEXT = Color.WHITE;
    private final Color COLOR_CONTENT_BACKGROUND = Color.WHITE;

    // --- 2. DEFINE UI COMPONENTS ---
    public final JButton inputMetrics;
    public final JButton accountSettings;
    public final JButton myScore;
    public final JButton insights;
    public final JButton history;
    public final JButton goals;

    private final CardLayout mainCardLayout;
    private final JPanel mainContentPanel;

    public HomeView(HomeViewModel homeViewModel) {
        this.homeViewModel = homeViewModel;
        this.setLayout(new BorderLayout());

        // === 3. CREATE THE TOP NAVBAR ===
        JPanel navbarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        navbarPanel.setBackground(COLOR_NAV_BAR);

        // Create buttons
        inputMetrics = new JButton("Input Health Metrics");
        accountSettings = new JButton("Account/Settings");
        myScore = new JButton("My Score");
        insights = new JButton("Insights");
        history = new JButton("History");
        goals = new JButton("Goals");

        // Style all 6 buttons
        styleNavbarButton(inputMetrics);
        styleNavbarButton(accountSettings);
        styleNavbarButton(myScore);
        styleNavbarButton(insights);
        styleNavbarButton(history);
        styleNavbarButton(goals);

        // Add buttons to navbar
        navbarPanel.add(inputMetrics);
        navbarPanel.add(accountSettings);
        navbarPanel.add(myScore);
        navbarPanel.add(insights);
        navbarPanel.add(history);
        navbarPanel.add(goals);

        // === 4. CREATE THE CENTER CONTENT PANEL ===
        mainCardLayout = new CardLayout();
        mainContentPanel = new JPanel(mainCardLayout);
        mainContentPanel.setBackground(COLOR_CONTENT_BACKGROUND); // Set a clean white background

        // --- A. Create the "My Score" page (Homepage) ---
        JPanel myScoreView = new JPanel(new BorderLayout(0, 20));
        myScoreView.setBackground(COLOR_CONTENT_BACKGROUND);
        myScoreView.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // -- Panel for Logo and Description (side-by-side)
        JPanel logoAndDescriptionPanel = new JPanel();
        logoAndDescriptionPanel.setLayout(new BoxLayout(logoAndDescriptionPanel, BoxLayout.X_AXIS));
        logoAndDescriptionPanel.setBackground(COLOR_CONTENT_BACKGROUND);

        // Add Logo
        try {
            ImageIcon logoIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("BetterBlueprint.png")));
            Image scaledImage = logoIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));
            logoLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
            logoAndDescriptionPanel.add(logoLabel);
        } catch (NullPointerException e) {
            logoAndDescriptionPanel.add(new JLabel("Logo Not Found"));
        }

        logoAndDescriptionPanel.add(Box.createRigidArea(new Dimension(20, 0)));

        // Add Description
        String descriptionText = "<html>A desktop Java application that allows users to log daily health metrics " +
                "(sleep, water, exercise, calories), calculate personalized health scores, " +
                "and receive Al-generated insights.</html>";
        JLabel descriptionLabel = new JLabel(descriptionText);
        descriptionLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        descriptionLabel.setAlignmentY(Component.CENTER_ALIGNMENT); // Center vertically
        // Set a max size to force text wrapping
        descriptionLabel.setMaximumSize(new Dimension(400, 150));
        logoAndDescriptionPanel.add(descriptionLabel);

        // --- Placeholder for the actual score ---
        JLabel scorePlaceholder = new JLabel("Your score will go here...");
        scorePlaceholder.setFont(new Font("SansSerif", Font.BOLD, 24));
        scorePlaceholder.setHorizontalAlignment(JLabel.CENTER);
        scorePlaceholder.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        // --- "My Score" page ---
        myScoreView.add(logoAndDescriptionPanel, BorderLayout.NORTH);
        myScoreView.add(scorePlaceholder, BorderLayout.CENTER);

        // --- B. Create other placeholder views ---
        // (These will now also have a white background)
        JPanel inputMetricsView = new JPanel();
        inputMetricsView.setBackground(COLOR_CONTENT_BACKGROUND);
        inputMetricsView.add(new JLabel("This is the Input Metrics View"));

        JPanel accountSettingsView = new JPanel();
        accountSettingsView.setBackground(COLOR_CONTENT_BACKGROUND);
        accountSettingsView.add(new JLabel("This is the Account/Settings View"));

        JPanel insightsView = new JPanel();
        insightsView.setBackground(COLOR_CONTENT_BACKGROUND);
        insightsView.add(new JLabel("This is the Insights View"));

        JPanel historyView = new JPanel();
        historyView.setBackground(COLOR_CONTENT_BACKGROUND);
        historyView.add(new JLabel("This is the History View"));

        JPanel goalsView = new JPanel();
        goalsView.setBackground(COLOR_CONTENT_BACKGROUND);
        goalsView.add(new JLabel("This is the Goals View"));

        // --- C. Add all views to the main CardLayout panel ---
        mainContentPanel.add(myScoreView, "My Score");
        mainContentPanel.add(inputMetricsView, "Input Metrics");
        mainContentPanel.add(accountSettingsView, "Account Settings");
        mainContentPanel.add(insightsView, "Insights");
        mainContentPanel.add(historyView, "History");
        mainContentPanel.add(goalsView, "Goals");

        // === 5. ASSEMBLE THE HOMEVIEW ===
        this.add(navbarPanel, BorderLayout.NORTH);
        this.add(mainContentPanel, BorderLayout.CENTER);

        // === 6. ADD ACTION LISTENERS (same as before) ===
        inputMetrics.addActionListener(e -> mainCardLayout.show(mainContentPanel, "Input Metrics"));
        accountSettings.addActionListener(e -> mainCardLayout.show(mainContentPanel, "Account Settings"));
        myScore.addActionListener(e -> mainCardLayout.show(mainContentPanel, "My Score"));
        insights.addActionListener(e -> mainCardLayout.show(mainContentPanel, "Insights"));
        history.addActionListener(e -> mainCardLayout.show(mainContentPanel, "History"));
        goals.addActionListener(e -> mainCardLayout.show(mainContentPanel, "Goals"));

        // Set "My Score" as the default homepage
        mainCardLayout.show(mainContentPanel, "My Score");
    }

    /**
     * A helper method to style our navbar buttons
     */
    private void styleNavbarButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setForeground(COLOR_NAV_BAR_TEXT);
        button.setBackground(COLOR_NAV_BAR);
        button.setPreferredSize(new Dimension(140, 50));

        // --- Remove all default Swing button styling ---
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);

        // --- Add hover effect ---
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(COLOR_NAV_BAR_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(COLOR_NAV_BAR);
            }
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public String getViewName() { return viewName; }
}