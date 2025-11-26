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
    private String currentUserId;// Changed to non-final

    private final JTextArea insightsArea;
    private final JButton generateButton;
    private final JButton backButton;
    private final JLabel errorLabel;

    // Navigation fields
    private CardLayout homeCardLayout;
    private JPanel homeContentPanel;

    public HealthInsightsView(HealthInsightsViewModel healthInsightsViewModel,
                              HealthInsightsController healthInsightsController) {
        this.healthInsightsViewModel = healthInsightsViewModel;
        this.healthInsightsController = healthInsightsController;
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
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Error label
        errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(errorLabel, BorderLayout.SOUTH);

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
            if (healthInsightsController != null) {
                if (currentUserId != null && !currentUserId.isEmpty()) {
                    System.out.println("Generating insights for user: " + currentUserId);
                    insightsArea.setText("Generating insights...");
                    errorLabel.setText("");
                    healthInsightsController.execute(currentUserId);
                    updateViewFromState();
                } else {
                    errorLabel.setText("No user logged in. Please log in first.");
                    System.err.println("HealthInsightsView: No current user set!");
                }
            } else {
                System.err.println("HealthInsightsController is null!");
                errorLabel.setText("Health Insights feature is not properly initialized.");
            }
        } else if (e.getSource() == backButton) {
            if (homeCardLayout != null && homeContentPanel != null) {
                homeCardLayout.show(homeContentPanel, "Home");
            } else {
                System.out.println("Back button clicked - navigation to be implemented");
            }
        }
    }

    private void updateViewFromState() {
        HealthInsightsState state = healthInsightsViewModel.getState();
        System.out.println("Updating view with insights: " + state.getInsights());
        System.out.println("Error message: " + state.getErrorMessage());

        if (state.getInsights() != null && !state.getInsights().isEmpty()) {
            insightsArea.setText(state.getInsights());
        }

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