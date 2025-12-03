package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.change_password.ChangePasswordController;
import interface_adapter.home.HomeViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.settings.SettingsController;
import interface_adapter.settings.SettingsState;
import interface_adapter.settings.SettingsViewModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The View for the Settings use case.
 */
public class SettingsView extends JPanel implements PropertyChangeListener {

    private final String viewName = "settings";
    private final SettingsViewModel settingsViewModel;
    private final ViewManagerModel viewManagerModel;
    private final HomeViewModel homeViewModel;
    private SettingsController settingsController;
    private LogoutController logoutController;
    private ChangePasswordController changePasswordController;

    // UI Dimension Constants
    private static final int TEXT_FIELD_COLUMNS = 15;
    private static final int CARD_PADDING_TOP = 30;
    private static final int CARD_PADDING_SIDE = 40;
    private static final int CARD_PADDING_BOTTOM = 30;
    private static final int CARD_MAX_WIDTH = 520;
    private static final int CARD_MAX_HEIGHT = 600;
    private static final int BORDER_THICKNESS = 1;
    private static final int TITLE_FONT_SIZE = 28;
    private static final int SPACING_SMALL = 8;
    private static final int SPACING_MEDIUM = 12;
    private static final int SPACING_LARGE = 16;

    private final JTextField ageField = new JTextField(TEXT_FIELD_COLUMNS);
    private final JTextField heightField = new JTextField(TEXT_FIELD_COLUMNS);
    private final JTextField weightField = new JTextField(TEXT_FIELD_COLUMNS);
    private final JPasswordField newPasswordField = new JPasswordField(TEXT_FIELD_COLUMNS);
    private final JButton saveButton;
    private final JButton cancelButton;
    private final JButton changePasswordButton;
    private final JButton logoutButton;
    private final JLabel errorMessageLabel = new JLabel();

    private static final Color PRIMARY_COLOR = new Color(37, 99, 235);
    private static final Color PRIMARY_HOVER = new Color(29, 78, 216);
    private static final Color SECONDARY_COLOR = new Color(34, 197, 94);
    private static final Color SECONDARY_HOVER = new Color(22, 163, 74);
    private static final Color BACKGROUND_COLOR = new Color(239, 246, 255);
    private static final Color CARD_COLOR = new Color(255, 255, 255);
    private static final Color TEXT_COLOR = new Color(31, 41, 55);
    private static final Color ERROR_COLOR = new Color(239, 68, 68);
    private static final Color BORDER_COLOR = new Color(191, 219, 254);

    public SettingsView(SettingsViewModel settingsViewModel, ViewManagerModel viewManagerModel, HomeViewModel homeViewModel) {
        this.settingsViewModel = settingsViewModel;
        this.viewManagerModel = viewManagerModel;
        this.homeViewModel = homeViewModel;
        this.settingsViewModel.addPropertyChangeListener(this);

        setLayout(new GridBagLayout());
        setBackground(BACKGROUND_COLOR);

        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(CARD_COLOR);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, BORDER_THICKNESS, true),
                new EmptyBorder(CARD_PADDING_TOP, CARD_PADDING_SIDE, CARD_PADDING_BOTTOM, CARD_PADDING_SIDE)
        ));
        cardPanel.setMaximumSize(new Dimension(CARD_MAX_WIDTH, CARD_MAX_HEIGHT));

        JLabel title = new JLabel("Settings");
        title.setFont(new Font("Segoe UI", Font.BOLD, TITLE_FONT_SIZE));
        title.setForeground(TEXT_COLOR);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        styleTextField(ageField);
        styleTextField(heightField);
        styleTextField(weightField);
        stylePasswordField(newPasswordField);

        JPanel agePanel = createInputPanel("Age", ageField);
        JPanel heightPanel = createInputPanel("Height (cm)", heightField);
        JPanel weightPanel = createInputPanel("Weight (kg)", weightField);
        JPanel passwordPanel = createInputPanel("New Password", newPasswordField);

        saveButton = createStyledButton("Save", true);
        cancelButton = createStyledButton("Back to Home", false);
        changePasswordButton = createStyledButton("Change Password", true);
        logoutButton = createStyledButton("Logout", false);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setBackground(CARD_COLOR);
        buttonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonsPanel.add(saveButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, SPACING_MEDIUM)));
        buttonsPanel.add(passwordPanel); // Moved password field here
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, SPACING_MEDIUM)));
        buttonsPanel.add(changePasswordButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, SPACING_MEDIUM)));
        buttonsPanel.add(logoutButton);

        errorMessageLabel.setFont(new Font("Segoe UI", Font.PLAIN, SPACING_MEDIUM));
        errorMessageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorMessageLabel.setBorder(new EmptyBorder(SPACING_SMALL, 0, SPACING_SMALL, 0));
        errorMessageLabel.setForeground(ERROR_COLOR);

        addButtonListeners();

        cardPanel.add(title);
        cardPanel.add(Box.createRigidArea(new Dimension(0, SPACING_LARGE)));
        cardPanel.add(agePanel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, SPACING_SMALL)));
        cardPanel.add(heightPanel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, SPACING_SMALL)));
        cardPanel.add(weightPanel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, SPACING_LARGE)));
        cardPanel.add(buttonsPanel);
        cardPanel.add(Box.createVerticalGlue());

        JPanel bottomBackPanel = new JPanel();
        bottomBackPanel.setBackground(CARD_COLOR);
        bottomBackPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottomBackPanel.add(cancelButton);

        cardPanel.add(bottomBackPanel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, SPACING_MEDIUM)));
        cardPanel.add(errorMessageLabel);

        add(cardPanel);
    }


    private void addButtonListeners() {
        saveButton.addActionListener(evt -> {
            if (settingsController != null) {
                // Read directly from fields when saving - proper Clean Architecture
                settingsController.execute(
                    ageField.getText(),
                    heightField.getText(),
                    weightField.getText()
                );
            }
        });

        changePasswordButton.addActionListener(evt -> {
            if (changePasswordController != null) {
                String newPassword = new String(newPasswordField.getPassword());
                if (!newPassword.isEmpty()) {
                    SettingsState state = settingsViewModel.getState();
                    changePasswordController.execute(state.getUsername(), newPassword);
                    newPasswordField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Password cannot be empty");
                }
            }
        });

        logoutButton.addActionListener(evt -> {
            if (logoutController != null) {
                logoutController.execute();
            }
        });

        cancelButton.addActionListener(evt -> {
            viewManagerModel.setState(homeViewModel.getViewName());
            viewManagerModel.firePropertyChange();
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            SettingsState state = (SettingsState) evt.getNewValue();
            ageField.setText(state.getAge());
            heightField.setText(state.getHeight());
            weightField.setText(state.getWeight());
            // Display settings error if present, otherwise clear
            if (state.getSettingsError() != null && !state.getSettingsError().isEmpty()) {
                errorMessageLabel.setText(state.getSettingsError());
            } else {
                errorMessageLabel.setText("");
            }
        } else if ("settingsSaved".equals(evt.getPropertyName())) {
            SettingsState state = settingsViewModel.getState();
            String message = state.getSuccessMessage();
            if (message == null || message.isEmpty()) {
                message = "Settings saved successfully!";
            }
            JOptionPane.showMessageDialog(this, message);
        } else if ("passwordChanged".equals(evt.getPropertyName())) {
            JOptionPane.showMessageDialog(this, "Password changed successfully!");
        } else if ("passwordError".equals(evt.getPropertyName())) {
            JOptionPane.showMessageDialog(this, settingsViewModel.getState().getPasswordError());
        } else if ("error".equals(evt.getPropertyName())) {
            SettingsState s = settingsViewModel.getState();
            errorMessageLabel.setText(s.getPasswordError() != null ? s.getPasswordError() : "An error occurred");
        }
    }


    public String getViewName() {
        return viewName;
    }

    public void setSettingsController(SettingsController controller) {
        this.settingsController = controller;
    }

    public void setLogoutController(LogoutController controller) {
        this.logoutController = controller;
    }

    public void setChangePasswordController(ChangePasswordController controller) {
        this.changePasswordController = controller;
    }

    private JPanel createInputPanel(String labelText, JComponent input) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CARD_COLOR);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(TEXT_COLOR);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        input.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(input);
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
        textField.setOpaque(true);
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

    private void stylePasswordField(JPasswordField field) {
        styleTextField(field);
        field.setEchoChar('•');
    }

    private JButton createStyledButton(String text, boolean isPrimary) {
        JButton button = new JButton(text);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(300, 45));
        button.setPreferredSize(new Dimension(300, 45));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (isPrimary) {
            button.setBackground(SECONDARY_COLOR);
            button.setForeground(Color.WHITE);
            button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) { button.setBackground(SECONDARY_HOVER); }
                public void mouseExited(java.awt.event.MouseEvent evt) { button.setBackground(SECONDARY_COLOR); }
            });
        } else {
            button.setBackground(PRIMARY_COLOR);
            button.setForeground(Color.WHITE);
            button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) { button.setBackground(PRIMARY_HOVER); }
                public void mouseExited(java.awt.event.MouseEvent evt) { button.setBackground(PRIMARY_COLOR); }
            });
        }
        return button;
    }
}
