package view;

import interface_adapter.health_insights.HealthInsightsController;
import interface_adapter.health_insights.HealthInsightsState;
import interface_adapter.health_insights.HealthInsightsViewModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class HealthInsightsView extends JPanel implements PropertyChangeListener {
    public final String viewName = "health insights";

    private final HealthInsightsViewModel healthInsightsViewModel;
    private HealthInsightsController healthInsightsController;
    private String currentUserId;

    private CardLayout homeCardLayout;
    private JPanel homeContentPanel;

    private static final Color PRIMARY_COLOR = new Color(37, 99, 235); // Blue-600
    private static final Color PRIMARY_HOVER = new Color(29, 78, 216); // Blue-700
    private static final Color SECONDARY_COLOR = new Color(34, 197, 94); // Green-500
    private static final Color BACKGROUND_COLOR = new Color(239, 246, 255); // Light blue tint
    private static final Color CARD_COLOR = new Color(255, 255, 255);
    private static final Color TEXT_COLOR = new Color(31, 41, 55);
    private static final Color ERROR_COLOR = new Color(239, 68, 68);
    private static final Color BORDER_COLOR = new Color(191, 219, 254); // Light blue border
    private static final Color SUBTITLE_COLOR = new Color(107, 114, 128);

    private final JLabel titleLabel = new JLabel("Health Insights");
    private final JLabel instructionLabel = new JLabel("Click the button below to generate personalized health insights.");
    private final JTextArea insightsTextArea;
    private final JLabel errorLabel = new JLabel();
    private final JButton generateButton;
    private final JButton backButton;

    public HealthInsightsView(HealthInsightsViewModel healthInsightsViewModel, HealthInsightsController healthInsightsController) {
        this.healthInsightsViewModel = healthInsightsViewModel;
        this.healthInsightsController = healthInsightsController;

        this.healthInsightsViewModel.addPropertyChangeListener(this);

        generateButton = createStyledButton("Generate Insights");
        backButton = createStyledButton("Back to Home");

        insightsTextArea = new JTextArea();
        insightsTextArea.setEditable(false);
        insightsTextArea.setLineWrap(true);
        insightsTextArea.setWrapStyleWord(true);
        insightsTextArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        insightsTextArea.setText("Your personalized health insights will appear here.");
        insightsTextArea.setBackground(CARD_COLOR);
        insightsTextArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(15, 15, 15, 15)
        ));

        setupLayout();
        updateViewFromState();
    }

    private void setupLayout() {
        setLayout(new GridBagLayout());
        setBackground(BACKGROUND_COLOR);

        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(CARD_COLOR);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(40, 50, 40, 50)
        ));
        cardPanel.setMaximumSize(new Dimension(700, 700));

        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        instructionLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        instructionLabel.setForeground(SUBTITLE_COLOR);
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        errorLabel.setForeground(ERROR_COLOR);
        errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorLabel.setBorder(new EmptyBorder(10, 20, 10, 20));
        errorLabel.setVisible(false);

        JScrollPane scrollPane = new JScrollPane(insightsTextArea);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        scrollPane.setMaximumSize(new Dimension(600, 300));
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                "Your Health Insights"
        ));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(CARD_COLOR);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setMaximumSize(new Dimension(500, 60));

        buttonPanel.add(generateButton);
        buttonPanel.add(backButton);

        cardPanel.add(titleLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        cardPanel.add(instructionLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        cardPanel.add(scrollPane);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        cardPanel.add(errorLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        cardPanel.add(buttonPanel);

        this.add(cardPanel);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(200, 45));
        button.setPreferredSize(new Dimension(200, 45));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);

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

        if (text.equals("Generate Insights")) {
            button.addActionListener(this::handleGenerateInsights);
        } else if (text.equals("Back to Home")) {
            button.addActionListener(this::handleBackToHome);
        }

        return button;
    }

    private void handleGenerateInsights(ActionEvent e) {
        if (healthInsightsController != null) {
            if (currentUserId != null && !currentUserId.isEmpty()) {
                insightsTextArea.setText("Generating insights... Please wait.");
                errorLabel.setText("");
                errorLabel.setVisible(false);

                healthInsightsController.execute(currentUserId);
            } else {
                errorLabel.setText("No user logged in. Please log in first.");
                errorLabel.setVisible(true);
            }
        } else {
            errorLabel.setText("Health Insights feature is not properly initialized.");
            errorLabel.setVisible(true);
        }
    }

    private void handleBackToHome(ActionEvent e) {
        if (homeCardLayout != null && homeContentPanel != null) {
            homeCardLayout.show(homeContentPanel, "Home");
        } else {
            System.out.println("Back to Home requested - navigation not set up");
        }
    }

    public void setHomeNavigation(CardLayout cardLayout, JPanel contentPanel) {
        this.homeCardLayout = cardLayout;
        this.homeContentPanel = contentPanel;
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
            if (state.getInsights() != null && !state.getInsights().isEmpty()) {
                insightsTextArea.setText(state.getInsights());
            }

            if (state.getErrorMessage() != null && !state.getErrorMessage().isEmpty()) {
                errorLabel.setText(state.getErrorMessage());
                errorLabel.setVisible(true);
            } else {
                errorLabel.setVisible(false);
            }
            revalidate();
            repaint();
        });
    }

    public void setCurrentUser(String userId) {
        this.currentUserId = userId;
    }

}