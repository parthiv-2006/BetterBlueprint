package view;

import interface_adapter.health_insights.HealthInsightsController;
import interface_adapter.health_insights.HealthInsightsState;
import interface_adapter.health_insights.HealthInsightsViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class HealthInsightsView extends JPanel implements PropertyChangeListener {
    public final String viewName = "health insights";

    private final HealthInsightsViewModel healthInsightsViewModel;
    private HealthInsightsController healthInsightsController;
    private String currentUserId;

    private final JTextArea insightsArea;
    private final JButton generateButton;
    private final JButton backButton;
    private final JLabel errorLabel;

    // Navigation fields
    private CardLayout homeCardLayout;
    private JPanel homeContentPanel;

    public HealthInsightsView(HealthInsightsViewModel healthInsightsViewModel, HealthInsightsController healthInsightsController) {
        this.healthInsightsViewModel = healthInsightsViewModel;
        this.healthInsightsController = healthInsightsController;

        // Add property change listener to the view model
        this.healthInsightsViewModel.addPropertyChangeListener(this);

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

        // Insights display
        insightsArea = new JTextArea();
        insightsArea.setEditable(false);
        insightsArea.setLineWrap(true);
        insightsArea.setWrapStyleWord(true);
        insightsArea.setFont(new Font("Arial", Font.PLAIN, 14));
        insightsArea.setText("Click 'Generate Insights' to get personalized health recommendations.");
        insightsArea.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(insightsArea);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        scrollPane.setBorder(BorderFactory.createTitledBorder("Your Health Insights"));
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Error label
        errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        contentPanel.add(errorLabel, BorderLayout.SOUTH);

        add(contentPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        generateButton = new JButton("Generate Insights");
        generateButton.addActionListener(this::handleGenerateInsights);

        backButton = new JButton("Back to Home");
        backButton.addActionListener(this::handleBackToHome);

        buttonPanel.add(generateButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Initialize view with current state
        updateViewFromState();
    }

    private void handleGenerateInsights(ActionEvent e) {
        if (healthInsightsController != null) {
            if (currentUserId != null && !currentUserId.isEmpty()) {
                System.out.println("Generating insights for user: " + currentUserId);

                // Show loading state
                SwingUtilities.invokeLater(() -> {
                    insightsArea.setText("Generating insights... Please wait.");
                    errorLabel.setText("");
                });

                // Execute the controller
                healthInsightsController.execute(currentUserId);
            } else {
                SwingUtilities.invokeLater(() -> {
                    errorLabel.setText("No user logged in. Please log in first.");
                });
                System.err.println("HealthInsightsView: No current user set!");
            }
        } else {
            SwingUtilities.invokeLater(() -> {
                errorLabel.setText("Health Insights feature is not properly initialized.");
            });
            System.err.println("HealthInsightsController is null!");
        }
    }

    private void handleBackToHome(ActionEvent e) {
        if (homeCardLayout != null && homeContentPanel != null) {
            homeCardLayout.show(homeContentPanel, "Home");
        } else {
            System.out.println("Back button clicked - navigation to be implemented");
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            updateViewFromState();
        }
    }

    private void updateViewFromState() {
        HealthInsightsState state = healthInsightsViewModel.getState();

        SwingUtilities.invokeLater(() -> {
            System.out.println("Updating view with insights: " + state.getInsights());
            System.out.println("Error message: " + state.getErrorMessage());

            // Update insights text
            if (state.getInsights() != null && !state.getInsights().isEmpty()) {
                insightsArea.setText(state.getInsights());
            }

            // Update error message
            if (state.getErrorMessage() != null && !state.getErrorMessage().isEmpty()) {
                errorLabel.setText(state.getErrorMessage());
            } else {
                errorLabel.setText("");
            }

            // Force UI refresh
            insightsArea.revalidate();
            insightsArea.repaint();
            this.revalidate();
            this.repaint();
        });
    }

    public void setCurrentUser(String userId) {
        this.currentUserId = userId;
        System.out.println("HealthInsightsView: Current user set to: " + userId);
    }

    public void setHealthInsightsController(HealthInsightsController controller) {
        this.healthInsightsController = controller;
    }

    public void setHomeNavigation(CardLayout cardLayout, JPanel contentPanel) {
        this.homeCardLayout = cardLayout;
        this.homeContentPanel = contentPanel;
    }
}