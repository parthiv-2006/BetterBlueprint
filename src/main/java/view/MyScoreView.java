package view;

import Entities.HealthMetrics;
import Entities.HealthScore;
import interface_adapter.daily_health_score.DailyHealthScoreController;
import interface_adapter.daily_health_score.DailyHealthScoreState;
import interface_adapter.daily_health_score.DailyHealthScoreViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The View for displaying the user's daily health score.
 * This class observes the ViewModel and notifies the Controller on user actions.
 */
public class MyScoreView extends JPanel implements ActionListener, PropertyChangeListener {

    private final DailyHealthScoreController controller;
    private final DailyHealthScoreViewModel viewModel;

    // UI components
    private final JLabel titleLabel = new JLabel("My Daily Health Score");
    private final JLabel dateLabel = new JLabel("Date: ---");
    private final JLabel scoreLabel = new JLabel("Score: ---");
    private final JLabel summaryLabel = new JLabel("Summary: ---");
    private final JLabel errorLabel = new JLabel("");

    private final JButton refreshButton = new JButton("Refresh Score");

    public MyScoreView(DailyHealthScoreController controller,
                       DailyHealthScoreViewModel viewModel) {

        this.controller = controller;
        this.viewModel = viewModel;

        // Listen for state changes from the ViewModel
        this.viewModel.addPropertyChangeListener(this);

        setupUI();
    }

    // ---------------- UI SETUP ----------------

    private void setupUI() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));

        dateLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        summaryLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        errorLabel.setForeground(Color.RED);
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        refreshButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        refreshButton.addActionListener(this);

        this.add(titleLabel);
        this.add(Box.createRigidArea(new Dimension(0, 15)));
        this.add(dateLabel);
        this.add(Box.createRigidArea(new Dimension(0, 5)));
        this.add(scoreLabel);
        this.add(Box.createRigidArea(new Dimension(0, 5)));
        this.add(summaryLabel);
        this.add(Box.createRigidArea(new Dimension(0, 10)));
        this.add(errorLabel);
        this.add(Box.createRigidArea(new Dimension(0, 20)));
        this.add(refreshButton);
    }

    // ---------------- ACTION LISTENER ----------------

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == refreshButton) {

            // Example: The controller needs username + today's date.
            // This depends on your actual InputData design.

            HealthMetrics healthMetrics = viewModel.getState().getHealthMetrics();
            // String username = viewModel.getState().getUsername();  // If applicable
            // String today = java.time.LocalDate.now().toString();

            // execute takes in HealthMetrics
            controller.execute(healthMetrics);
        }
    }

    // ---------------- PROPERTY CHANGE LISTENER ----------------

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        DailyHealthScoreState state = viewModel.getState();

        // Update UI based on the new state
        if (state.getDate() != null) {
            dateLabel.setText("Date: " + state.getDate());
        }

        if (state.getScore() != 0) {
            scoreLabel.setText("Score: " + state.getScore());
        } else {
            scoreLabel.setText("Score: ---");
        }

        if (state.getSummaryFeedback() != null) {
            summaryLabel.setText("Summary: " + state.getSummaryFeedback());
        } else {
            summaryLabel.setText("Summary: ---");
        }

        if (state.getErrorMessage() != null) {
            errorLabel.setText(state.getErrorMessage());
        } else {
            errorLabel.setText("");
        }

        this.revalidate();
        this.repaint();
    }
}

