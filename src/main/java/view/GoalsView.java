package view;

import Entities.User;
import data_access.FileUserDataAccessObject;
import interface_adapter.goals.GoalsController;
import interface_adapter.goals.GoalsState;
import interface_adapter.goals.GoalsViewModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The View for the Goals / Caloric Intake Plane use case.
 * First screen: choose goal type.
 * Second screen: enter details depending on goal type.
 * Third Screen: For Final Plan
 */
public class GoalsView extends JPanel implements PropertyChangeListener {

    private final String viewName = "goals";
    private final GoalsViewModel goalsViewModel;

    private final CardLayout innerCardLayout = new CardLayout();
    private final JPanel innerCardPanel = new JPanel(innerCardLayout);

    // Selected goal type ("Weight Loss", "Weight Maintenance", "Weight Gain")
    private String selectedGoalType = null;

    // DETAILS screen components
    private final JLabel detailsTitleLabel = new JLabel("Goal Details");
    private final JLabel selectedGoalLabel = new JLabel(" ");
    private final JTextField targetWeightField = new JTextField(20);
    private final JTextField timeframeField = new JTextField(20);
    private final JLabel errorMessageLabel = new JLabel();

    // RESULT screen components
    private final JLabel resultTitleLabel = new JLabel("Your Daily Caloric Plan");
    private final JLabel resultSummaryLabel = new JLabel(" ");  // NEW
    private final JLabel explanationLabel = new JLabel(" ");    // NEW
    private final JLabel disclaimerLabel = new JLabel(" ");

    private final JLabel intakeLabel = new JLabel("Daily calories intake goal: -- kcal");
    private final JLabel burnLabel = new JLabel("Daily calories burn goal: -- kcal");
    private final JLabel differenceLabel = new JLabel("Caloric difference: -- kcal");

    private final JButton generateButton;
    private final JButton backButton;
    private GoalsController goalsController; // wired later

    // Navigation back to HomeView
    private CardLayout homeCardLayout;
    private JPanel homeContentPanel;

    // Current logged-in user (provided by outer app)
    private User currentUser;
    private FileUserDataAccessObject userDataAccessObject; // optional fallback
    private final JLabel currentWeightLabel = new JLabel("Current weight: -- kg");

    // List of weight display labels that need updating when user is set
    private final java.util.List<JLabel> weightLabels = new java.util.ArrayList<>();

    // Color scheme
    private static final Color PRIMARY_COLOR = new Color(37, 99, 235);
    private static final Color PRIMARY_HOVER = new Color(29, 78, 216);
    private static final Color SECONDARY_COLOR = new Color(34, 197, 94);
    private static final Color SECONDARY_HOVER = new Color(22, 163, 74);
    private static final Color BACKGROUND_COLOR = new Color(239, 246, 255);
    private static final Color CARD_COLOR = new Color(255, 255, 255);
    private static final Color TEXT_COLOR = new Color(31, 41, 55);
    private static final Color ERROR_COLOR = new Color(239, 68, 68);
    private static final Color BORDER_COLOR = new Color(191, 219, 254);

    public GoalsView(GoalsViewModel goalsViewModel) {
        this.goalsViewModel = goalsViewModel;
        this.goalsViewModel.addPropertyChangeListener(this);

        this.setLayout(new GridBagLayout());
        this.setBackground(BACKGROUND_COLOR);

        innerCardPanel.setBackground(BACKGROUND_COLOR);

        // Add three cards
        innerCardPanel.add(createGoalSelectionCard(), "SELECT");
        innerCardPanel.add(createGoalDetailsCard(), "DETAILS");
        innerCardPanel.add(createGoalResultCard(), "RESULT");

        // Show selection first
        innerCardLayout.show(innerCardPanel, "SELECT");

        // Build main panel with cards
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(BACKGROUND_COLOR);

        mainPanel.add(innerCardPanel);

        this.add(mainPanel);


        // Buttons on details card
        generateButton = createStyledButton("Generate Plan", "SECONDARY");
        backButton = createStyledButton("Back to Goal Selection", "PRIMARY");

        generateButton.addActionListener(e -> handleGenerate());
        backButton.addActionListener(e -> innerCardLayout.show(innerCardPanel, "SELECT"));

        attachButtonsAndPlanToDetailsCard();
    }

    // Public API for outer app to provide the current user
    public void setCurrentUser(User user) {
        this.currentUser = user;
        updateCurrentWeightDisplay();
        // Update all weight labels in panels
        for (JLabel label : weightLabels) {
            updateWeightDisplayLabel(label);
        }
    }

    // Optional: allow AppBuilder to pass the DAO so the view can refresh itself when needed
    public void setUserDataAccess(FileUserDataAccessObject dao) {
        this.userDataAccessObject = dao;
    }

    // Helper: return whether the current user has a positive weight set
    public boolean hasWeightSet() {
        // If we don't have an in-memory user, try to fetch from DAO if available
        if (currentUser == null && userDataAccessObject != null) {
            String cur = userDataAccessObject.getCurrentUsername();
            if (cur != null) {
                User u = userDataAccessObject.get(cur);
                if (u != null) {
                    setCurrentUser(u);
                }
            }
        }
        return currentUser != null && currentUser.getWeight() > 0;
    }

    // Helper: get current weight or -1 if unknown
    public int getCurrentWeight() {
        if (currentUser == null) return -1;
        return currentUser.getWeight();
    }

    // Called by ribbon click handler. If user has not set weight, redirect to settings.
    // Assumption: the settings card name in the Home view's CardLayout is "settings"; adjust if different.
    public void openOrRedirectToSettings() {
        boolean weightSet = currentUser != null && currentUser.getWeight() > 0;

        if (!weightSet) {
            // Instead of navigating away, show an informational message and keep the Goals selection visible.
            JOptionPane.showMessageDialog(this,
                    "Please input your weight in Settings",
                    "Input required",
                    JOptionPane.INFORMATION_MESSAGE);

            // Ensure the goals selection card is visible so the user sees the Goals UI.
            innerCardLayout.show(innerCardPanel, "SELECT");
            return;
        }

        // If weight is set, simply show the selection card inside the Goals view.
        innerCardLayout.show(innerCardPanel, "SELECT");
    }

    private void updateCurrentWeightDisplay() {
        // If we don't have a user but have DAO, attempt to populate it
        if (currentUser == null && userDataAccessObject != null) {
            String cur = userDataAccessObject.getCurrentUsername();
            if (cur != null) {
                User u = userDataAccessObject.get(cur);
                if (u != null) {
                    this.currentUser = u;
                }
            }
        }

        if (currentUser == null) {
            currentWeightLabel.setText("Current weight: -- kg");
            return;
        }

        int w = currentUser.getWeight();
        if (w <= 0) {
            currentWeightLabel.setText("Current weight: not set — open Settings");
        } else {
            currentWeightLabel.setText("Current weight: " + w + " kg");
        }
    }

    // When user chooses 'Maintain Weight' let the use case compute the plan.
    private void handleMaintainSelected() {
        try {
            this.selectedGoalType = "Weight Maintenance";

            // Update ViewModel state
            GoalsState state = goalsViewModel.getState();
            state.setGoalType(this.selectedGoalType);
            goalsViewModel.setState(state);

            errorMessageLabel.setText("");

            if (goalsController != null) {
                // For maintenance, target is blank; timeframe is not really used, but we pass "1".
                goalsController.execute(this.selectedGoalType, "", "1");
            }

            // UI-only summary label; the numbers & explanation come from the presenter via state.
            resultSummaryLabel.setText(
                    "Goal: " + selectedGoalType +
                            "  |  Target: (maintain current weight)  |  Timeframe: N/A"
            );

            innerCardLayout.show(innerCardPanel, "RESULT");
        } catch (Exception ex) {
            System.err.println("GoalsView: unexpected error in handleMaintainSelected: " + ex.getMessage());
            errorMessageLabel.setText("Unexpected error: " + ex.getMessage());
            errorMessageLabel.setForeground(ERROR_COLOR);
        }
    }


    // CARD 1: GOAL TYPE SELECTION

    private JPanel createGoalSelectionCard() {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(CARD_COLOR);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(40, 50, 40, 50)
        ));
        cardPanel.setMaximumSize(new Dimension(650, 500));

        JLabel title = new JLabel("Select Your Goal");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT_COLOR);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel currentWeightDisplayLabel = new JLabel(" ");
        currentWeightDisplayLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        currentWeightDisplayLabel.setForeground(new Color(107, 114, 128));
        currentWeightDisplayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        updateWeightDisplayLabel(currentWeightDisplayLabel);
        weightLabels.add(currentWeightDisplayLabel);

        JLabel subtitle = new JLabel(
                "Choose your desired goal");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(107, 114, 128));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton lossButton = createGoalOptionButton("Weight Loss");
        JButton maintainButton = createGoalOptionButton("Maintain Weight");
        JButton gainButton = createGoalOptionButton("Weight Gain");

        // Wire selection behaviour
        lossButton.addActionListener(e -> goToDetailsFor("Weight Loss"));
        // For maintain weight we don't need timeframe — go straight to results
        maintainButton.addActionListener(e -> handleMaintainSelected());
        gainButton.addActionListener(e -> goToDetailsFor("Weight Gain"));

        cardPanel.add(title);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        cardPanel.add(currentWeightDisplayLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        cardPanel.add(subtitle);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        cardPanel.add(lossButton);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        cardPanel.add(maintainButton);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        cardPanel.add(gainButton);

        JPanel outer = new JPanel();
        outer.setBackground(BACKGROUND_COLOR);
        outer.setLayout(new BoxLayout(outer, BoxLayout.Y_AXIS));
        outer.add(Box.createVerticalGlue());
        outer.add(cardPanel);
        outer.add(Box.createVerticalGlue());

        return outer;
    }

    private JButton createGoalOptionButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(300, 55));
        button.setPreferredSize(new Dimension(300, 55));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setOpaque(true);
        button.setContentAreaFilled(true);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_HOVER);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR);
            }
        });

        return button;
    }

    private void goToDetailsFor(String goalType) {
        this.selectedGoalType = goalType;

        // Update ViewModel state
        GoalsState state = goalsViewModel.getState();
        state.setGoalType(goalType);
        goalsViewModel.setState(state);

        // Adjust DETAILS UI based on goal type
        selectedGoalLabel.setText("Selected Goal: " + goalType);

        boolean needsTarget =
                !"Weight Maintenance".equals(goalType);

        targetWeightField.setVisible(needsTarget);
        targetWeightField.setText("");
        timeframeField.setText("");
        errorMessageLabel.setText("");

        innerCardLayout.show(innerCardPanel, "DETAILS");
    }

    // CARD 2: GOAL DETAILS

    private JPanel createGoalDetailsCard() {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(CARD_COLOR);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(40, 50, 40, 50)
        ));
        cardPanel.setMaximumSize(new Dimension(650, 600));

        detailsTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        detailsTitleLabel.setForeground(TEXT_COLOR);
        detailsTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel currentWeightDisplayLabel = new JLabel(" ");
        currentWeightDisplayLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        currentWeightDisplayLabel.setForeground(new Color(107, 114, 128));
        currentWeightDisplayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        updateWeightDisplayLabel(currentWeightDisplayLabel);
        weightLabels.add(currentWeightDisplayLabel);

        selectedGoalLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        selectedGoalLabel.setForeground(new Color(107, 114, 128));
        selectedGoalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        styleTextField(targetWeightField);
        styleTextField(timeframeField);

        JPanel targetPanel = createInputPanel("Target Weight (kg)", targetWeightField);
        JPanel timeframePanel = createInputPanel("Timeframe (weeks)", timeframeField);

        errorMessageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        errorMessageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorMessageLabel.setBorder(new EmptyBorder(10, 0, 10, 0));

        // Add components (buttons & their panel will be attached later)
        cardPanel.add(detailsTitleLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        cardPanel.add(currentWeightDisplayLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        // Wrap selectedGoalLabel in a centered panel
        JPanel goalLabelPanel = new JPanel();
        goalLabelPanel.setLayout(new BoxLayout(goalLabelPanel, BoxLayout.X_AXIS));
        goalLabelPanel.setBackground(CARD_COLOR);
        goalLabelPanel.add(Box.createHorizontalGlue());
        goalLabelPanel.add(selectedGoalLabel);
        goalLabelPanel.add(Box.createHorizontalGlue());
        cardPanel.add(goalLabelPanel);

        cardPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        cardPanel.add(targetPanel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        cardPanel.add(timeframePanel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        cardPanel.add(errorMessageLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel outer = new JPanel();
        outer.setBackground(BACKGROUND_COLOR);
        outer.setLayout(new BoxLayout(outer, BoxLayout.Y_AXIS));
        outer.add(Box.createVerticalGlue());
        outer.add(cardPanel);
        outer.add(Box.createVerticalGlue());

        return outer;
    }

    // CARD 3: GOAL RESULT
    private JPanel createGoalResultCard() {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(CARD_COLOR);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(40, 50, 40, 50)
        ));
        cardPanel.setMaximumSize(new Dimension(650, 600));

        // Title
        resultTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        resultTitleLabel.setForeground(TEXT_COLOR);
        resultTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Summary (Goal | Target | Timeframe)
        resultSummaryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        resultSummaryLabel.setForeground(new Color(107, 114, 128));
        resultSummaryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Short explanation text
        explanationLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        explanationLabel.setForeground(TEXT_COLOR);
        explanationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Disclaimer (smaller, grey)
        disclaimerLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        disclaimerLabel.setForeground(new Color(120, 120, 120));
        disclaimerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        intakeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        intakeLabel.setForeground(TEXT_COLOR);
        intakeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        burnLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        burnLabel.setForeground(TEXT_COLOR);
        burnLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        differenceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        differenceLabel.setForeground(TEXT_COLOR);
        differenceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel goalsPanel = new JPanel();
        goalsPanel.setLayout(new BoxLayout(goalsPanel, BoxLayout.Y_AXIS));
        goalsPanel.setBackground(new Color(248, 250, 252));
        goalsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(15, 15, 15, 15)
        ));
        goalsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        goalsPanel.add(intakeLabel);
        goalsPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        goalsPanel.add(burnLabel);
        goalsPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        goalsPanel.add(differenceLabel);

        JButton backToDetailsButton = createStyledButton("Back to Goal Selection", "PRIMARY");
        backToDetailsButton.addActionListener(e -> innerCardLayout.show(innerCardPanel, "SELECT"));

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setBackground(CARD_COLOR);
        buttonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonsPanel.add(backToDetailsButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        cardPanel.add(resultTitleLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        cardPanel.add(resultSummaryLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        cardPanel.add(goalsPanel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        cardPanel.add(explanationLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        cardPanel.add(disclaimerLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        cardPanel.add(buttonsPanel);

        JPanel outer = new JPanel();
        outer.setBackground(BACKGROUND_COLOR);
        outer.setLayout(new BoxLayout(outer, BoxLayout.Y_AXIS));
        outer.add(Box.createVerticalGlue());
        outer.add(cardPanel);
        outer.add(Box.createVerticalGlue());

        return outer;
    }


    private void attachButtonsAndPlanToDetailsCard() {
        JPanel outerDetails = (JPanel) innerCardPanel.getComponent(1); // "DETAILS"
        JPanel cardPanel = (JPanel) outerDetails.getComponent(1);      // inner card

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setBackground(CARD_COLOR);
        buttonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonsPanel.add(generateButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonsPanel.add(backButton);

        cardPanel.add(buttonsPanel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));
    }

    // Shared helpers

    private JPanel createInputPanel(String labelText, JComponent fieldComponent) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CARD_COLOR);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(TEXT_COLOR);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        fieldComponent.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(fieldComponent);

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

        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(PRIMARY_COLOR, 2, true),
                        new EmptyBorder(11, 14, 11, 14)
                ));
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                        new EmptyBorder(12, 15, 12, 15)
                ));
            }
        });
    }

    private JButton createStyledButton(String text, String style) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(300, 45));
        button.setPreferredSize(new Dimension(300, 45));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setContentAreaFilled(true);

        switch (style) {
            case "PRIMARY":     // Blue
                button.setBackground(PRIMARY_COLOR);
                button.setForeground(Color.WHITE);
                button.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        button.setBackground(PRIMARY_HOVER);
                    }
                    @Override
                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        button.setBackground(PRIMARY_COLOR);
                    }
                });
                break;

            case "SECONDARY":   // GREEN
                button.setBackground(SECONDARY_COLOR);
                button.setForeground(Color.WHITE);
                button.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        button.setBackground(SECONDARY_HOVER);
                    }
                    @Override
                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        button.setBackground(SECONDARY_COLOR);
                    }
                });
                break;
            default:
                // Fallback neutral style
                button.setBackground(new Color(120, 120, 120));
                button.setForeground(Color.WHITE);
                break;
        }

        return button;
    }


    private void handleGenerate() {
        try {
            if (selectedGoalType == null) {
                errorMessageLabel.setText("Please select a goal first.");
                errorMessageLabel.setForeground(ERROR_COLOR);
                return;
            }

            String target = targetWeightField.getText().trim();
            String timeframe = timeframeField.getText().trim();

            boolean needsTarget = !"Weight Maintenance".equals(selectedGoalType);

            if (needsTarget && (target.isEmpty() || timeframe.isEmpty())) {
                errorMessageLabel.setText("Please enter target weight and timeframe.");
                errorMessageLabel.setForeground(ERROR_COLOR);
                return;
            }

            if (!needsTarget && timeframe.isEmpty()) {
                errorMessageLabel.setText("Please enter a timeframe.");
                errorMessageLabel.setForeground(ERROR_COLOR);
                return;
            }

            // Validate target weight matches goal type
            if (needsTarget && currentUser != null) {
                try {
                    double targetWeight = Double.parseDouble(target);
                    double currentWeight = currentUser.getWeight();

                    if ("Weight Loss".equals(selectedGoalType) && targetWeight > currentWeight) {
                        JOptionPane.showMessageDialog(this,
                                "Target weight should be less than your current weight for a weight loss plan.",
                                "Invalid Target Weight",
                                JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    if ("Weight Gain".equals(selectedGoalType) && targetWeight < currentWeight) {
                        JOptionPane.showMessageDialog(this,
                                "Target weight should be more than your current weight for a weight gain plan.",
                                "Invalid Target Weight",
                                JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    errorMessageLabel.setText("Target weight must be a valid number.");
                    errorMessageLabel.setForeground(ERROR_COLOR);
                    return;
                }
            }

            errorMessageLabel.setText("");

            if (goalsController != null) {
                String targetForUseCase = needsTarget ? target : "";
                goalsController.execute(selectedGoalType, targetForUseCase, timeframe);
            }

            // Build the summary line using what the user entered
            String targetDisplay = needsTarget ? (target + " kg") : "(maintain current weight)";
            String currentWeightStr = (currentUser != null && currentUser.getWeight() > 0)
                ? currentUser.getWeight() + " kg"
                : "-- kg";
            resultSummaryLabel.setText(
                    "Goal: " + selectedGoalType +
                            "  |  Current: " + currentWeightStr +
                            "  |  Target: " + targetDisplay +
                            "  |  Timeframe: " + timeframe + " weeks"
            );

            innerCardLayout.show(innerCardPanel, "RESULT");
        } catch (Exception ex) {
            System.err.println("GoalsView: unexpected error in handleGenerate: " + ex.getMessage());
            errorMessageLabel.setText("Unexpected error: " + ex.getMessage());
            errorMessageLabel.setForeground(ERROR_COLOR);
        }
    }

    private void updateWeightDisplayLabel(JLabel label) {
        if (currentUser == null) {
            label.setText("Current weight: -- kg");
            return;
        }

        int w = currentUser.getWeight();
        if (w <= 0) {
            label.setText("Current weight: not set — open Settings");
        } else {
            label.setText("Current weight: " + w + " kg");
        }
    }

    public void propertyChange(PropertyChangeEvent evt) {
        GoalsState state = (GoalsState) evt.getNewValue();

        if (state.getErrorMessage() != null) {
            errorMessageLabel.setText(state.getErrorMessage());
            errorMessageLabel.setForeground(ERROR_COLOR);
        } else {
            errorMessageLabel.setText("");
        }

        if (state.getDailyIntakeCalories() != null) {
            intakeLabel.setText("Daily calories intake goal: " +
                    state.getDailyIntakeCalories() + " kcal");
        }

        if (state.getDailyBurnCalories() != null) {
            burnLabel.setText("Daily calories burn goal: " +
                    state.getDailyBurnCalories() + " kcal");
        }

        // Calculate and display the difference
        if (state.getDailyIntakeCalories() != null && state.getDailyBurnCalories() != null) {
            try {
                double intake = Double.parseDouble(state.getDailyIntakeCalories());
                double burn = Double.parseDouble(state.getDailyBurnCalories());
                double difference = intake - burn;

                String differenceText;
                if (difference > 0) {
                    differenceText = "Caloric difference: +" + String.format("%.0f", difference) + " kcal (surplus)";
                } else if (difference < 0) {
                    differenceText = "Caloric difference: " + String.format("%.0f", difference) + " kcal (deficit)";
                } else {
                    differenceText = "Caloric difference: 0 kcal (balanced)";
                }
                differenceLabel.setText(differenceText);
            } catch (NumberFormatException e) {
                differenceLabel.setText("Caloric difference: -- kcal");
            }
        }

        if (state.getExplanation() != null) {
            explanationLabel.setText(state.getExplanation());
        }

        if (state.shouldRedirectToSettings()) {

            if (homeCardLayout != null && homeContentPanel != null) {
                homeCardLayout.show(homeContentPanel, "Settings");
            }
        }


    }

    public String getViewName() {
        return viewName;
    }

    public void setGoalsController(GoalsController controller) {
        this.goalsController = controller;
    }

    public void setHomeNavigation(CardLayout cardLayout, JPanel contentPanel) {
        this.homeCardLayout = cardLayout;
        this.homeContentPanel = contentPanel;
    }
}
