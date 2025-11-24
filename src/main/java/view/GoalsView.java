package view;

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

    private final JButton generateButton;
    private final JButton backButton;
    private GoalsController goalsController; // wired later

    // Navigation back to HomeView
    private CardLayout homeCardLayout;
    private JPanel homeContentPanel;

    // Color scheme
    private static final Color PRIMARY_COLOR = new Color(37, 99, 235); // Blue-600
    private static final Color PRIMARY_HOVER = new Color(29, 78, 216); // Blue-700
    private static final Color SECONDARY_COLOR = new Color(34, 197, 94); // Green-500
    private static final Color SECONDARY_HOVER = new Color(22, 163, 74); // Green-600
    private static final Color BACKGROUND_COLOR = new Color(239, 246, 255); // Light blue tint
    private static final Color CARD_COLOR = new Color(255, 255, 255);
    private static final Color TEXT_COLOR = new Color(31, 41, 55);
    private static final Color ERROR_COLOR = new Color(239, 68, 68);
    private static final Color BORDER_COLOR = new Color(191, 219, 254); // Light blue border

    public GoalsView(GoalsViewModel goalsViewModel) {
        this.goalsViewModel = goalsViewModel;
        this.goalsViewModel.addPropertyChangeListener(this);

        this.setLayout(new GridBagLayout());
        this.setBackground(BACKGROUND_COLOR);

        innerCardPanel.setBackground(BACKGROUND_COLOR);

        // Add two cards
        innerCardPanel.add(createGoalSelectionCard(), "SELECT");
        innerCardPanel.add(createGoalDetailsCard(), "DETAILS");
        innerCardPanel.add(createGoalResultCard(), "RESULT");

        // Show selection first
        innerCardLayout.show(innerCardPanel, "SELECT");

        this.add(innerCardPanel);

        // Buttons on details card
        generateButton = createStyledButton("Generate Plan", true);
        backButton = createStyledButton("Back to Goal Selection", false);

        generateButton.addActionListener(e -> handleGenerate());
        backButton.addActionListener(e -> innerCardLayout.show(innerCardPanel, "SELECT"));

        attachButtonsAndPlanToDetailsCard();

        addDocumentListeners();
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
        maintainButton.addActionListener(e -> goToDetailsFor("Weight Maintenance"));
        gainButton.addActionListener(e -> goToDetailsFor("Weight Gain"));

        cardPanel.add(title);
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
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_HOVER);
            }

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
        cardPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        cardPanel.add(selectedGoalLabel);
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

        resultTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        resultTitleLabel.setForeground(TEXT_COLOR);
        resultTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);


        JButton backToDetailsButton = createStyledButton("Back to Details", false);
        backToDetailsButton.addActionListener(e -> innerCardLayout.show(innerCardPanel, "DETAILS"));

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setBackground(CARD_COLOR);
        buttonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonsPanel.add(backToDetailsButton);

        cardPanel.add(resultTitleLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));
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
        button.setOpaque(true);
        button.setContentAreaFilled(true);

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

    private void handleGenerate() {
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

        errorMessageLabel.setText("");

        // TODO: call goalsController here when use case is implemented

        innerCardLayout.show(innerCardPanel, "RESULT");

    }

    private void addDocumentListeners() {
        targetWeightField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void update() {
                GoalsState state = goalsViewModel.getState();
                state.setTargetWeight(targetWeightField.getText());
                goalsViewModel.setState(state);
            }
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { update(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { update(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { update(); }
        });

        timeframeField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void update() {
                GoalsState state = goalsViewModel.getState();
                state.setTimeframe(timeframeField.getText());
                goalsViewModel.setState(state);
            }
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { update(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { update(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { update(); }
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        GoalsState state = (GoalsState) evt.getNewValue();

        if (state.getErrorMessage() != null) {
            errorMessageLabel.setText(state.getErrorMessage());
            errorMessageLabel.setForeground(ERROR_COLOR);
        } else {
            errorMessageLabel.setText("");
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
