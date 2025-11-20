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
    private final HealthInsightsController healthInsightsController;

    private final JTextArea insightsArea;
    private final JButton generateButton;
    private final JButton backButton;
    private final JLabel errorLabel;

    public HealthInsightsView(HealthInsightsViewModel healthInsightsViewModel,
                              HealthInsightsController healthInsightsController) {
        this.healthInsightsViewModel = healthInsightsViewModel;
        this.healthInsightsController = healthInsightsController;

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

        JScrollPane scrollPane = new JScrollPane(insightsArea);
        scrollPane.setPreferredSize(new Dimension(500, 300));
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
            // For now, use a placeholder user ID
            String currentUserId = "test_user";
            healthInsightsController.execute(currentUserId);

            updateViewFromState();
        } else if (e.getSource() == backButton) {
            System.out.println("Back button clicked - navigation to be implemented");
        }
    }

    private void updateViewFromState() {
        HealthInsightsState state = healthInsightsViewModel.getState();
        insightsArea.setText(state.getInsights());
        errorLabel.setText(state.getErrorMessage());
    }
}