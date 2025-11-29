package view;

import interface_adapter.input_metrics.InputMetricsController;
import interface_adapter.input_metrics.InputMetricsState;
import interface_adapter.input_metrics.InputMetricsViewModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The View for the Input Metrics use case.
 */
public class InputMetricsView extends JPanel implements PropertyChangeListener {
    private final String viewName = "input metrics";
    private final InputMetricsViewModel inputMetricsViewModel;

    private final JTextField sleepHoursField = new JTextField(20);
    private final JTextField stepsField = new JTextField(20); // ADDED steps field
    private final JTextField waterIntakeField = new JTextField(20);
    private final JTextField caloriesField = new JTextField(20);
    private final JTextField exerciseDurationField = new JTextField(20);
    private final JLabel errorMessageLabel = new JLabel();

    private final JButton submitButton;
    private final JButton backButton;
    private InputMetricsController inputMetricsController;

    // For navigation within HomeView
    private CardLayout homeCardLayout;
    private JPanel homeContentPanel;

    // Modern color scheme - Blue and Green theme
    private static final Color PRIMARY_COLOR = new Color(37, 99, 235); // Blue-600
    private static final Color PRIMARY_HOVER = new Color(29, 78, 216); // Blue-700
    private static final Color SECONDARY_COLOR = new Color(34, 197, 94); // Green-500
    private static final Color SECONDARY_HOVER = new Color(22, 163, 74); // Green-600
    private static final Color BACKGROUND_COLOR = new Color(239, 246, 255); // Light blue tint
    private static final Color CARD_COLOR = new Color(255, 255, 255);
    private static final Color TEXT_COLOR = new Color(31, 41, 55);
    private static final Color ERROR_COLOR = new Color(239, 68, 68);
    private static final Color SUCCESS_COLOR = new Color(34, 197, 94);
    private static final Color BORDER_COLOR = new Color(191, 219, 254); // Light blue border

    public InputMetricsView(InputMetricsViewModel inputMetricsViewModel) {
        this.inputMetricsViewModel = inputMetricsViewModel;
        this.inputMetricsViewModel.addPropertyChangeListener(this);

        // Set up the main panel
        this.setLayout(new GridBagLayout());
        this.setBackground(BACKGROUND_COLOR);

        // Create a card-style center panel
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(CARD_COLOR);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(40, 50, 40, 50)
        ));
        cardPanel.setMaximumSize(new Dimension(600, 700));

        // Title
        JLabel title = new JLabel(InputMetricsViewModel.TITLE_LABEL);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT_COLOR);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtitle
        JLabel subtitle = new JLabel("Track your daily health metrics");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(107, 114, 128));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Style input fields
        styleTextField(sleepHoursField);
        styleTextField(stepsField); // ADDED: style steps field
        styleTextField(waterIntakeField);
        styleTextField(caloriesField);
        styleTextField(exerciseDurationField);

        // Create input panels
        JPanel sleepPanel = createInputPanel(InputMetricsViewModel.SLEEP_LABEL, sleepHoursField);
        JPanel stepsPanel = createInputPanel(InputMetricsViewModel.STEPS_LABEL, stepsField); // ADDED: steps panel
        JPanel waterPanel = createInputPanel(InputMetricsViewModel.WATER_LABEL, waterIntakeField);
        JPanel caloriesPanel = createInputPanel(InputMetricsViewModel.CALORIES_LABEL, caloriesField);
        JPanel exercisePanel = createInputPanel(InputMetricsViewModel.EXERCISE_LABEL, exerciseDurationField);

        // Error message label
        errorMessageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        errorMessageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorMessageLabel.setBorder(new EmptyBorder(10, 0, 10, 0));

        // Buttons
        submitButton = createStyledButton(InputMetricsViewModel.SUBMIT_BUTTON_LABEL, true);
        backButton = createStyledButton(InputMetricsViewModel.BACK_BUTTON_LABEL, false);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setBackground(CARD_COLOR);
        buttonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonsPanel.add(submitButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonsPanel.add(backButton);

        // Add action listeners
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSubmit();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Navigate back to Home within the HomeView's CardLayout
                if (homeCardLayout != null && homeContentPanel != null) {
                    homeCardLayout.show(homeContentPanel, "Home");
                }
            }
        });

        // Add document listeners to update state
        addDocumentListeners();

        // Add components to card panel
        cardPanel.add(title);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        cardPanel.add(subtitle);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        cardPanel.add(sleepPanel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        cardPanel.add(stepsPanel); // ADDED: steps panel
        cardPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        cardPanel.add(waterPanel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        cardPanel.add(caloriesPanel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        cardPanel.add(exercisePanel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        cardPanel.add(errorMessageLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        cardPanel.add(buttonsPanel);

        // Add card to main panel
        this.add(cardPanel);
    }

    private void handleSubmit() {
        if (inputMetricsController == null) {
            return;
        }

        try {
            // Parse input values
            float sleepHours = Float.parseFloat(sleepHoursField.getText().trim());
            int steps = Integer.parseInt(stepsField.getText().trim()); // ADDED: parse steps
            float waterIntake = Float.parseFloat(waterIntakeField.getText().trim());
            int calories = Integer.parseInt(caloriesField.getText().trim());
            float exerciseDuration = Float.parseFloat(exerciseDurationField.getText().trim());

            // Get current username from state or use empty string (interactor will fetch it)
            String username = inputMetricsViewModel.getState().getUsername();

            // Execute the use case - UPDATED to include steps
            inputMetricsController.execute(username, sleepHours, steps, waterIntake, calories, exerciseDuration);

        } catch (NumberFormatException e) {
            errorMessageLabel.setText("Please enter valid numbers for all fields.");
            errorMessageLabel.setForeground(ERROR_COLOR);
        }
    }

    private JPanel createInputPanel(String labelText, JTextField textField) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CARD_COLOR);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(TEXT_COLOR);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        textField.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(textField);

        return panel;
    }

    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(12, 15, 12, 15)
        ));
        textField.setBackground(Color.WHITE);
        textField.setForeground(TEXT_COLOR);

        // Add focus effect
        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(PRIMARY_COLOR, 2, true),
                        new EmptyBorder(11, 14, 11, 14)
                ));
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                        new EmptyBorder(12, 15, 12, 15)
                ));
            }
        });
    }

    private JButton createStyledButton(String text, boolean isPrimary) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(300, 45));
        button.setPreferredSize(new Dimension(300, 45));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (isPrimary) {
            button.setBackground(SECONDARY_COLOR);
            button.setForeground(Color.WHITE);
            button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setBackground(SECONDARY_HOVER);
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setBackground(SECONDARY_COLOR);
                }
            });
        } else {
            button.setBackground(PRIMARY_COLOR);
            button.setForeground(Color.WHITE);
            button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setBackground(PRIMARY_HOVER);
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setBackground(PRIMARY_COLOR);
                }
            });
        }

        return button;
    }

    private void addDocumentListeners() {
        // Add document listeners to sync with state
        sleepHoursField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void update() {
                InputMetricsState state = inputMetricsViewModel.getState();
                state.setSleepHours(sleepHoursField.getText());
                inputMetricsViewModel.setState(state);
            }

            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                update();
            }
        });

        // ADDED: Document listener for steps field
        stepsField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void update() {
                InputMetricsState state = inputMetricsViewModel.getState();
                state.setSteps(stepsField.getText());
                inputMetricsViewModel.setState(state);
            }

            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                update();
            }
        });

        waterIntakeField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void update() {
                InputMetricsState state = inputMetricsViewModel.getState();
                state.setWaterIntake(waterIntakeField.getText());
                inputMetricsViewModel.setState(state);
            }

            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                update();
            }
        });

        caloriesField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void update() {
                InputMetricsState state = inputMetricsViewModel.getState();
                state.setCaloriesConsumed(caloriesField.getText());
                inputMetricsViewModel.setState(state);
            }

            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                update();
            }
        });

        exerciseDurationField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void update() {
                InputMetricsState state = inputMetricsViewModel.getState();
                state.setExerciseDuration(exerciseDurationField.getText());
                inputMetricsViewModel.setState(state);
            }

            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                update();
            }
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        InputMetricsState state = (InputMetricsState) evt.getNewValue();

        if (state.getErrorMessage() != null) {
            errorMessageLabel.setText(state.getErrorMessage());
            // Check if it's a success message (contains "successfully")
            if (state.getErrorMessage().toLowerCase().contains("success")) {
                errorMessageLabel.setForeground(SUCCESS_COLOR);
                // Clear fields on success
                sleepHoursField.setText("");
                stepsField.setText(""); // ADDED: clear steps field
                waterIntakeField.setText("");
                caloriesField.setText("");
                exerciseDurationField.setText("");
            } else {
                errorMessageLabel.setForeground(ERROR_COLOR);
            }
        } else {
            errorMessageLabel.setText("");
        }
    }

    public String getViewName() {
        return viewName;
    }

    public void setInputMetricsController(InputMetricsController controller) {
        this.inputMetricsController = controller;
    }

    /**
     * Sets the HomeView's CardLayout and content panel for navigation.
     * @param cardLayout the CardLayout from HomeView
     * @param contentPanel the content panel from HomeView
     */
    public void setHomeNavigation(CardLayout cardLayout, JPanel contentPanel) {
        this.homeCardLayout = cardLayout;
        this.homeContentPanel = contentPanel;
    }
}