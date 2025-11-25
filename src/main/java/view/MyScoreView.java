package view;

import interface_adapter.daily_health_score.DailyHealthScoreController;
import interface_adapter.daily_health_score.DailyHealthScoreState;
import interface_adapter.daily_health_score.DailyHealthScoreViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;

/**
 * The View for displaying the user's daily health score.
 * This class observes the ViewModel and notifies the Controller on user actions.
 */

public class MyScoreView extends JPanel implements PropertyChangeListener {

    private final DailyHealthScoreViewModel viewModel;
    private DailyHealthScoreController controller;

    private final JLabel titleLabel = new JLabel("Daily Health Score");
    private final JLabel dateLabel = new JLabel("Date: " + LocalDate.now());
    private final JLabel instructionLabel = new JLabel("Click the button below to compute your health score for today.");
    private final JLabel scoreLabel = new JLabel();
    private final JLabel feedbackLabel = new JLabel();
    private final JLabel errorLabel = new JLabel();

    private final JButton computeButton = new JButton("Compute Today's Score");

    public MyScoreView(DailyHealthScoreViewModel viewModel,
                       DailyHealthScoreController controller) {
        this.viewModel = viewModel;
        this.controller = controller;

        viewModel.addPropertyChangeListener(this);
        setupLayout();
        System.out.println("MyScoreView created: " + this); // for testing
    }

    public void setController(DailyHealthScoreController controller) {
        this.controller = controller;

        // Remove OLD listeners to avoid duplicates
        for (ActionListener al : computeButton.getActionListeners()) {
            computeButton.removeActionListener(al);
        }

        // Add listener NOW that controller exists
        computeButton.addActionListener(e -> {
            System.out.println("BUTTON PRESSED");   // for testing
            LocalDate today = LocalDate.now();
            controller.computeDailyHealthScore(today);  // userId is retrieved automatically from logged-in user
        });
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50)); // Large margins

        // Main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Date (always shown)
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(dateLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Instruction label (shown initially, hidden after button click)
        instructionLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        instructionLabel.setForeground(new Color(100, 100, 100));
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(instructionLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Compute button
        computeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        computeButton.setFont(new Font("Arial", Font.BOLD, 14));
        contentPanel.add(computeButton);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Error label
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(errorLabel);

        // Score label (hidden initially)
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoreLabel.setVisible(false);
        contentPanel.add(scoreLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Feedback label (hidden initially, spans wider with HTML)
        feedbackLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        feedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        feedbackLabel.setVisible(false);
        contentPanel.add(feedbackLabel);

        add(contentPanel, BorderLayout.CENTER);
    }

//    private void setupActions() {
//        computeButton.addActionListener(e -> {
//            // Automatically use today's date
//            LocalDate today = LocalDate.now();
//            controller.computeDailyHealthScore(today, "user"); // replace "user" with dynamic login if available
//        });
//    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("MyScoreView.propertyChange called: " + evt.getPropertyName());
        if ("dailyHealthScoreState".equals(evt.getPropertyName())) {
            DailyHealthScoreState state = (DailyHealthScoreState) evt.getNewValue();
            System.out.println("State received - error: " + state.getErrorMessage() + ", score: " + state.getScore());

            if (state.getErrorMessage() != null) {
                // Show error, keep button visible
                errorLabel.setText(state.getErrorMessage());
                errorLabel.setVisible(true);
                instructionLabel.setVisible(false);
                scoreLabel.setVisible(false);
                feedbackLabel.setVisible(false);
            } else {
                // Success: Hide instruction and button, show score and feedback
                instructionLabel.setVisible(false);
                computeButton.setVisible(false);
                errorLabel.setVisible(false);

                scoreLabel.setText("Score: " + (state.getScore() != null ? state.getScore() : ""));
                scoreLabel.setVisible(true);

                // Format feedback with HTML for better text wrapping
                // Remove \n characters and clean up the text
                String feedbackText = state.getFeedback() != null ? state.getFeedback() : "";
                feedbackText = feedbackText.replace("\\n", " ").replace("\n", " ").trim();
                feedbackLabel.setText("<html><div style='width: 500px; text-align: center;'>" +
                                     feedbackText + "</div></html>");
                feedbackLabel.setVisible(true);
            }

            // Revalidate and repaint to update the UI
            revalidate();
            repaint();

            System.out.println("UI updated");
        }
    }
}

