package view;

import interface_adapter.daily_health_score.DailyHealthScoreController;
import interface_adapter.daily_health_score.DailyHealthScoreState;
import interface_adapter.daily_health_score.DailyHealthScoreViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
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
    private final DailyHealthScoreController controller;

    private final JLabel dateLabel = new JLabel("Date:");
    private final JLabel scoreLabel = new JLabel("Score:");
    private final JLabel feedbackLabel = new JLabel("Feedback:");
    private final JLabel errorLabel = new JLabel();

    private final JButton computeButton = new JButton("Compute Today's Score");

    public MyScoreView(DailyHealthScoreViewModel viewModel,
                       DailyHealthScoreController controller) {
        this.viewModel = viewModel;
        this.controller = controller;

        viewModel.addPropertyChangeListener(this);
        setupLayout();
        setupActions();
    }

    private void setupLayout() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;

        // Row 0: Button
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(computeButton, gbc);

        // Row 1: Error message
        gbc.gridy = 1;
        errorLabel.setForeground(Color.RED);
        add(errorLabel, gbc);

        // Row 2: Date
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        add(dateLabel, gbc);

        // Row 3: Score
        gbc.gridy = 3;
        add(scoreLabel, gbc);

        // Row 4: Feedback
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(feedbackLabel, gbc);
    }

    private void setupActions() {
        computeButton.addActionListener(e -> {
            // Automatically use today's date
            LocalDate today = LocalDate.now();
            controller.computeDailyHealthScore(today, "user"); // replace "user" with dynamic login if available
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("dailyHealthScoreState".equals(evt.getPropertyName())) {
            DailyHealthScoreState state = (DailyHealthScoreState) evt.getNewValue();

            if (state.getErrorMessage() != null) {
                errorLabel.setText(state.getErrorMessage());
                dateLabel.setText("Date:");
                scoreLabel.setText("Score:");
                feedbackLabel.setText("Feedback:");
            } else {
                errorLabel.setText("");
                dateLabel.setText("Date: " + (state.getDate() != null ? state.getDate().toString() : ""));
                scoreLabel.setText("Score: " + (state.getScore() != null ? state.getScore() : ""));
                feedbackLabel.setText("<html>Feedback: " + (state.getFeedback() != null ? state.getFeedback() : "") + "</html>");
            }
        }
    }
}

