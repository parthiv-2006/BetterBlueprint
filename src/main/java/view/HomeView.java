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
    private final Color COLOR_CONTENT_BACKGROUND = new Color(245, 247, 250); // Soft Gray-White
    private final Color COLOR_PRIMARY_BUTTON = new Color(41, 128, 185); // Nice Blue for CTA
    private final Color COLOR_TEXT_DARK = new Color(44, 62, 80);       // Dark Blue-Grey for text

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

    public HomeView(HomeViewModel homeViewModel) {
        this.homeViewModel = homeViewModel;
        this.setLayout(new BorderLayout());

        // === 3. CREATE THE TOP NAVBAR ===
        JPanel navbarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        navbarPanel.setBackground(COLOR_NAV_BAR);

        // Create buttons
        home = new JButton("Home");
        inputMetrics = new JButton("Input Health Metrics");
        accountSettings = new JButton("Account/Settings");
        myScore = new JButton("My Score");
        insights = new JButton("Insights");
        history = new JButton("History");
        goals = new JButton("Goals");

        // Style all 6 buttons
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
        mainCardLayout = new CardLayout();
        mainContentPanel = new JPanel(mainCardLayout);
        mainContentPanel.setBackground(COLOR_CONTENT_BACKGROUND); 

        // --- A. Create the "Home" page ---
        JPanel homeView = new JPanel();
        homeView.setLayout(new BoxLayout(homeView, BoxLayout.Y_AXIS));
        homeView.setBackground(COLOR_CONTENT_BACKGROUND);
        homeView.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60)); // Increased padding

        // -- Panel for Logo and Description (side-by-side)
        JPanel logoAndDescriptionPanel = new JPanel();
        logoAndDescriptionPanel.setLayout(new BoxLayout(logoAndDescriptionPanel, BoxLayout.X_AXIS));
        logoAndDescriptionPanel.setBackground(COLOR_CONTENT_BACKGROUND);
        logoAndDescriptionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add Logo
        try {
            // Try loading with a leading slash (absolute path in classpath)
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

        logoAndDescriptionPanel.add(Box.createRigidArea(new Dimension(30, 0))); // Increased gap

        // -- Right side of header: Welcome Title + Description
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(COLOR_CONTENT_BACKGROUND);
        
        JLabel welcomeTitle = new JLabel("Welcome to BetterBlueprint");
        welcomeTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeTitle.setForeground(COLOR_TEXT_DARK);
        welcomeTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        String descriptionText = "<html><body style='width: 350px; color: #555555;'>" + 
                "Track your daily metrics, calculate personalized health scores, " +
                "and receive AI-powered insights to improve your well-being." +
                "</body></html>";
        JLabel descriptionLabel = new JLabel(descriptionText);
        descriptionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(welcomeTitle);
        textPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        textPanel.add(descriptionLabel);

        logoAndDescriptionPanel.add(textPanel);

        // -- Button to guide user to Input Metrics --
        JButton goToInputMetrics = new JButton("Start Tracking Now");
        goToInputMetrics.setFont(new Font("Segoe UI", Font.BOLD, 16));
        goToInputMetrics.setForeground(Color.WHITE);
        goToInputMetrics.setBackground(COLOR_PRIMARY_BUTTON);
        goToInputMetrics.setFocusPainted(false);
        goToInputMetrics.setBorderPainted(false); // Flat look
        goToInputMetrics.setOpaque(true);
        goToInputMetrics.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Make button bigger
        goToInputMetrics.setMaximumSize(new Dimension(220, 50));
        goToInputMetrics.setPreferredSize(new Dimension(220, 50));

        goToInputMetrics.addActionListener(e -> mainCardLayout.show(mainContentPanel, "Input Metrics"));
        
        // Hover effect for CTA button
        goToInputMetrics.addMouseListener(new MouseAdapter() {
             public void mouseEntered(MouseEvent evt) {
                 goToInputMetrics.setBackground(COLOR_PRIMARY_BUTTON.darker());
                 goToInputMetrics.setCursor(new Cursor(Cursor.HAND_CURSOR));
             }
             public void mouseExited(MouseEvent evt) {
                 goToInputMetrics.setBackground(COLOR_PRIMARY_BUTTON);
             }
        });

        homeView.add(Box.createVerticalGlue()); // Push content to center vertically
        homeView.add(logoAndDescriptionPanel);
        homeView.add(Box.createRigidArea(new Dimension(0, 50)));
        homeView.add(goToInputMetrics);
        homeView.add(Box.createVerticalGlue());


        // --- B. Create the "My Score" page ---
        JPanel myScoreView = new JPanel(new BorderLayout(0, 20));
        myScoreView.setBackground(COLOR_CONTENT_BACKGROUND);
        myScoreView.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // --- Placeholder for the actual score ---
        JLabel scorePlaceholder = new JLabel("Your score will go here...");
        scorePlaceholder.setFont(new Font("SansSerif", Font.BOLD, 24));
        scorePlaceholder.setHorizontalAlignment(JLabel.CENTER);
        scorePlaceholder.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        myScoreView.add(scorePlaceholder, BorderLayout.CENTER);

        // --- C. Create other placeholder views ---
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
        mainContentPanel.add(homeView, "Home");
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
        home.addActionListener(e -> mainCardLayout.show(mainContentPanel, "Home"));
        inputMetrics.addActionListener(e -> mainCardLayout.show(mainContentPanel, "Input Metrics"));
        accountSettings.addActionListener(e -> mainCardLayout.show(mainContentPanel, "Account Settings"));
        myScore.addActionListener(e -> mainCardLayout.show(mainContentPanel, "My Score"));
        insights.addActionListener(e -> mainCardLayout.show(mainContentPanel, "Insights"));
        history.addActionListener(e -> mainCardLayout.show(mainContentPanel, "History"));
        goals.addActionListener(e -> mainCardLayout.show(mainContentPanel, "Goals"));

        // Set "Home" as the default homepage
        mainCardLayout.show(mainContentPanel, "Home");
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