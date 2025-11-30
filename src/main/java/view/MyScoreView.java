package view;

import interface_adapter.daily_health_score.DailyHealthScoreController;
import interface_adapter.daily_health_score.DailyHealthScoreState;
import interface_adapter.daily_health_score.DailyHealthScoreViewModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

    // color scheme - matching other views
    private static final Color PRIMARY_COLOR = new Color(37, 99, 235); // Blue-600
    private static final Color PRIMARY_HOVER = new Color(29, 78, 216); // Blue-700
    private static final Color SECONDARY_COLOR = new Color(34, 197, 94); // Green-500
    private static final Color BACKGROUND_COLOR = new Color(239, 246, 255); // Light blue tint
    private static final Color CARD_COLOR = new Color(255, 255, 255);
    private static final Color TEXT_COLOR = new Color(31, 41, 55);
    private static final Color ERROR_COLOR = new Color(239, 68, 68);
    private static final Color BORDER_COLOR = new Color(191, 219, 254); // Light blue border
    private static final Color SUBTITLE_COLOR = new Color(107, 114, 128);

    private final JLabel titleLabel = new JLabel("Daily Health Score");
    private final JLabel dateLabel = new JLabel("Date: " + LocalDate.now());
    private final JLabel instructionLabel = new JLabel("Click the button below to compute your health score for today.");
    private final JLabel scoreLabel = new JLabel();
    private final JLabel feedbackLabel = new JLabel();
    private final JLabel errorLabel = new JLabel();

    // Metrics breakdown section
    private final JLabel breakdownTitleLabel = new JLabel("Score Breakdown");
    private final JLabel sleepLabel = new JLabel();
    private final JLabel exerciseLabel = new JLabel();
    private final JLabel caloriesLabel = new JLabel();
    private final JLabel waterLabel = new JLabel();
    private final JLabel stepsLabel = new JLabel();
    private final JPanel metricsPanel = new JPanel();

    private final JButton computeButton;

    public MyScoreView(DailyHealthScoreViewModel viewModel,
                       DailyHealthScoreController controller) {
        this.viewModel = viewModel;
        this.controller = controller;

        // Create button with styling
        computeButton = createStyledButton("Compute Today's Score");

        viewModel.addPropertyChangeListener(this);
        setupLayout();
    }

    public void setController(DailyHealthScoreController controller) {
        this.controller = controller;

        // Remove OLD listeners to avoid duplicates
        for (ActionListener al : computeButton.getActionListeners()) {
            computeButton.removeActionListener(al);
        }

        // Add listener now that controller exists
        computeButton.addActionListener(e -> {
            LocalDate today = LocalDate.now();
            controller.computeDailyHealthScore(today);  // userId is retrieved automatically from logged-in user
        });
    }

    private void setupLayout() {
        // Set up the main panel with background
        setLayout(new GridBagLayout());
        setBackground(BACKGROUND_COLOR);

        // Create a card-style center panel
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(CARD_COLOR);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(40, 50, 40, 50)
        ));
        cardPanel.setMaximumSize(new Dimension(700, 800));

        // Title
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Date label (always shown)
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        dateLabel.setForeground(SUBTITLE_COLOR);
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Instruction label (shown initially, hidden after button click)
        instructionLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        instructionLabel.setForeground(SUBTITLE_COLOR);
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Error label
        errorLabel.setForeground(ERROR_COLOR);
        errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorLabel.setBorder(new EmptyBorder(10, 20, 10, 20));
        errorLabel.setVisible(false);

        // Score label (hidden initially) - styled with larger font and green color
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        scoreLabel.setForeground(SECONDARY_COLOR);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoreLabel.setVisible(false);

        // Feedback label (hidden initially) - card style with border
        feedbackLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        feedbackLabel.setForeground(TEXT_COLOR);
        feedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        feedbackLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(20, 25, 20, 25)
        ));
        feedbackLabel.setBackground(new Color(249, 250, 251)); // Very light gray
        feedbackLabel.setOpaque(true);
        feedbackLabel.setVisible(false);

        // Metrics breakdown section (hidden initially)
        breakdownTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        breakdownTitleLabel.setForeground(TEXT_COLOR);
        breakdownTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Setup metrics panel
        metricsPanel.setLayout(new GridLayout(2, 2, 20, 15));
        metricsPanel.setBackground(CARD_COLOR);
        metricsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        metricsPanel.setMaximumSize(new Dimension(550, 120));
        metricsPanel.setVisible(false);

        // Style individual metric labels
        sleepLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sleepLabel.setForeground(TEXT_COLOR);
        sleepLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(12, 15, 12, 15)
        ));
        sleepLabel.setBackground(new Color(249, 250, 251));
        sleepLabel.setOpaque(true);
        sleepLabel.setHorizontalAlignment(SwingConstants.CENTER);

        exerciseLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        exerciseLabel.setForeground(TEXT_COLOR);
        exerciseLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(12, 15, 12, 15)
        ));
        exerciseLabel.setBackground(new Color(249, 250, 251));
        exerciseLabel.setOpaque(true);
        exerciseLabel.setHorizontalAlignment(SwingConstants.CENTER);

        caloriesLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        caloriesLabel.setForeground(TEXT_COLOR);
        caloriesLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(12, 15, 12, 15)
        ));
        caloriesLabel.setBackground(new Color(249, 250, 251));
        caloriesLabel.setOpaque(true);
        caloriesLabel.setHorizontalAlignment(SwingConstants.CENTER);

        waterLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        waterLabel.setForeground(TEXT_COLOR);
        waterLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(12, 15, 12, 15)
        ));
        waterLabel.setBackground(new Color(249, 250, 251));
        waterLabel.setOpaque(true);
        waterLabel.setHorizontalAlignment(SwingConstants.CENTER);

        stepsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        stepsLabel.setForeground(TEXT_COLOR);
        stepsLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(12, 15, 12, 15)
        ));
        stepsLabel.setBackground(new Color(249, 250, 251));
        stepsLabel.setOpaque(true);
        stepsLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Add metric labels to panel
        metricsPanel.add(sleepLabel);
        metricsPanel.add(exerciseLabel);
        metricsPanel.add(caloriesLabel);
        metricsPanel.add(waterLabel);
        metricsPanel.add(stepsLabel);

        // Add components to card panel with spacing
        cardPanel.add(titleLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        cardPanel.add(dateLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        cardPanel.add(instructionLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        cardPanel.add(computeButton);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        cardPanel.add(errorLabel);
        cardPanel.add(scoreLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        cardPanel.add(feedbackLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        cardPanel.add(breakdownTitleLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        cardPanel.add(metricsPanel);

        // Add card to main panel
        this.add(cardPanel);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(300, 45));
        button.setPreferredSize(new Dimension(300, 45));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(PRIMARY_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR);
            }
        });

        return button;
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("dailyHealthScoreState".equals(evt.getPropertyName())) {
            DailyHealthScoreState state = (DailyHealthScoreState) evt.getNewValue();

            if (state.getErrorMessage() != null) {
                // Show error, keep button visible
                errorLabel.setText(state.getErrorMessage());
                errorLabel.setVisible(true);
                instructionLabel.setVisible(false);
                scoreLabel.setVisible(false);
                feedbackLabel.setVisible(false);
                breakdownTitleLabel.setVisible(false);
                metricsPanel.setVisible(false);
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
                feedbackLabel.setText("<html><div style='width: 550px; text-align: center;'>" +
                        feedbackText + "</div></html>");
                feedbackLabel.setVisible(true);

                // Display metrics breakdown
                if (state.getSleepHours() != null) {
                    sleepLabel.setText("<html><b>Sleep:</b> " + state.getSleepHours() + " hrs</html>");
                }
                if (state.getExerciseMinutes() != null) {
                    exerciseLabel.setText("<html><b>Exercise:</b> " + state.getExerciseMinutes() + " min</html>");
                }
                if (state.getCalories() != null) {
                    caloriesLabel.setText("<html><b>Calories:</b> " + state.getCalories() + " cal</html>");
                }
                if (state.getWaterIntake() != null) {
                    waterLabel.setText("<html><b>Water:</b> " + state.getWaterIntake() + " L</html>");
                }
                if (state.getSteps() != null) {
                    stepsLabel.setText("<html><b>Steps:</b> " + state.getSteps() + "</html>");
                }

                breakdownTitleLabel.setVisible(true);
                metricsPanel.setVisible(true);
            }

            // Revalidate and repaint to update the UI
            revalidate();
            repaint();
        }
    }
}
