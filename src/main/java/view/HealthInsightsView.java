package view;

import interface_adapter.health_insights.HealthInsightsController;
import interface_adapter.health_insights.HealthInsightsState;
import interface_adapter.health_insights.HealthInsightsViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HealthInsightsView extends JPanel implements ActionListener {
    public final String viewName = "health insights";
    private final HealthInsightsViewModel healthInsightsViewModel;
    private HealthInsightsController healthInsightsController;
    private String currentUserId;

    private final JTextArea insightsArea;
    private final JButton generateButton;
    private final JButton backButton;
    private final JLabel errorLabel;
    private final JLabel statusLabel;

    // Navigation fields
    private CardLayout homeCardLayout;
    private JPanel homeContentPanel;

    public HealthInsightsView(HealthInsightsViewModel healthInsightsViewModel, HealthInsightsController healthInsightsController) {
        this.healthInsightsViewModel = healthInsightsViewModel;
        this.healthInsightsController = healthInsightsController;

        // Set up property change listener
        this.healthInsightsViewModel.addPropertyChangeListener(evt -> {
            updateViewFromState();
        });

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Health Insights");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        // Main content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Status label for loading messages
        statusLabel = new JLabel("Click 'Generate Insights' to get personalized health recommendations.");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        statusLabel.setForeground(Color.GRAY);

        // Insights display
        insightsArea = new JTextArea();
        insightsArea.setEditable(false);
        insightsArea.setLineWrap(true);
        insightsArea.setWrapStyleWord(true);
        insightsArea.setFont(new Font("Arial", Font.PLAIN, 14));
        insightsArea.setText("");
        insightsArea.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(insightsArea);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        scrollPane.setBorder(BorderFactory.createTitledBorder("Your Health Insights"));
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Status and error panel
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.add(statusLabel, BorderLayout.NORTH);

        errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusPanel.add(errorLabel, BorderLayout.SOUTH);

        contentPanel.add(statusPanel, BorderLayout.SOUTH);

        add(contentPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        generateButton = new JButton("Generate Insights");
        generateButton.addActionListener(this);
        buttonPanel.add(generateButton);

        backButton = new JButton("Back to Home");
        backButton.addActionListener(this);
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == generateButton) {
            handleGenerateInsights();
        } else if (e.getSource() == backButton) {
            handleBackToHome();
        }
    }

    private void handleGenerateInsights() {
        if (healthInsightsController != null) {
            if (currentUserId != null && !currentUserId.isEmpty()) {
                System.out.println("Generating insights for user: " + currentUserId);

                // Update UI immediately on EDT
                SwingUtilities.invokeLater(() -> {
                    insightsArea.setText("");
                    statusLabel.setText("Generating insights... Please wait.");
                    statusLabel.setForeground(Color.BLUE);
                    errorLabel.setText("");
                    generateButton.setEnabled(false);
                });

                // Execute on a separate thread to avoid blocking UI
                new Thread(() -> {
                    try {
                        healthInsightsController.execute(currentUserId);
                    } finally {
                        // Re-enable button after processing
                        SwingUtilities.invokeLater(() -> {
                            generateButton.setEnabled(true);
                        });
                    }
                }).start();
            } else {
                errorLabel.setText("No user logged in. Please log in first.");
                System.err.println("HealthInsightsView: No current user set!");
            }
        } else {
            System.err.println("HealthInsightsController is null!");
            errorLabel.setText("Health Insights feature is not properly initialized.");
        }
    }

    private void handleBackToHome() {
        if (homeCardLayout != null && homeContentPanel != null) {
            homeCardLayout.show(homeContentPanel, "Home");
        } else {
            System.out.println("Back button clicked - navigation to be implemented");
        }
    }

    private void updateViewFromState() {
        // Always run on EDT
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(this::updateViewFromState);
            return;
        }

        HealthInsightsState state = healthInsightsViewModel.getState();
        String insights = state.getInsights();
        String error = state.getErrorMessage();

        System.out.println("Updating view with insights: " + insights);
        System.out.println("Error message: " + error);

        if (error != null && !error.isEmpty()) {
            // Show error
            errorLabel.setText(error);
            statusLabel.setText("Error generating insights");
            statusLabel.setForeground(Color.RED);

            if (insights != null && !insights.isEmpty()) {
                insightsArea.setText(insights);
            } else {
                insightsArea.setText("");
            }
        } else if (insights != null && !insights.isEmpty()) {
            // Show success
            insightsArea.setText(insights);
            errorLabel.setText("");
            statusLabel.setText("Insights generated successfully!");
            statusLabel.setForeground(new Color(0, 128, 0)); // Dark green
        } else {
            // No insights yet
            insightsArea.setText("");
            errorLabel.setText("");
            statusLabel.setText("Click 'Generate Insights' to get personalized health recommendations.");
            statusLabel.setForeground(Color.GRAY);
        }

        // Force UI refresh
        insightsArea.revalidate();
        insightsArea.repaint();
        errorLabel.revalidate();
        errorLabel.repaint();
        statusLabel.revalidate();
        statusLabel.repaint();
    }

    public void setCurrentUser(String userId) {
        this.currentUserId = userId;
        System.out.println("HealthInsightsView: Current user set to: " + userId);

        // Update UI when user changes
        SwingUtilities.invokeLater(() -> {
            insightsArea.setText("");
            errorLabel.setText("");
            statusLabel.setText("Click 'Generate Insights' to get personalized health recommendations for " + userId);
            statusLabel.setForeground(Color.GRAY);
        });
    }

    public void setHealthInsightsController(HealthInsightsController controller) {
        this.healthInsightsController = controller;
    }

    public void setHomeNavigation(CardLayout cardLayout, JPanel contentPanel) {
        this.homeCardLayout = cardLayout;
        this.homeContentPanel = contentPanel;
    }
}