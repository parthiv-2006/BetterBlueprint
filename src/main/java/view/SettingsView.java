package view;

import interface_adapter.change_password.ChangePasswordController;
import interface_adapter.logout.LogoutController;
import interface_adapter.settings.SettingsController;
import interface_adapter.settings.SettingsState;
import interface_adapter.settings.SettingsViewModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class SettingsView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "settings";
    private final SettingsViewModel settingsViewModel;
    private SettingsController settingsController;
    private LogoutController logoutController;
    private ChangePasswordController changePasswordController;

    private final JTextField ageField = new JTextField(15);
    private final JTextField heightField = new JTextField(15);
    private final JTextField weightField = new JTextField(15);
    private final JPasswordField newPasswordField = new JPasswordField(15);
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

    public SettingsView(SettingsViewModel settingsViewModel) {
        this.settingsViewModel = settingsViewModel;
        this.settingsViewModel.addPropertyChangeListener(this);

        setLayout(new GridBagLayout());
        setBackground(BACKGROUND_COLOR);

        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(CARD_COLOR);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(30, 40, 30, 40)
        ));
        cardPanel.setMaximumSize(new Dimension(520, 600));

        JLabel title = new JLabel("Settings");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
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
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        buttonsPanel.add(passwordPanel); // Moved password field here
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        buttonsPanel.add(changePasswordButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        buttonsPanel.add(logoutButton);

        errorMessageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        errorMessageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorMessageLabel.setBorder(new EmptyBorder(8, 0, 8, 0));
        errorMessageLabel.setForeground(ERROR_COLOR);

        addButtonListeners();

        cardPanel.add(title);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 16)));
        cardPanel.add(agePanel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        cardPanel.add(heightPanel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        cardPanel.add(weightPanel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 16)));
        cardPanel.add(buttonsPanel);
        cardPanel.add(Box.createVerticalGlue());

        JPanel bottomBackPanel = new JPanel();
        bottomBackPanel.setBackground(CARD_COLOR);
        bottomBackPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottomBackPanel.add(cancelButton);

        cardPanel.add(bottomBackPanel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 12)));
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

        cancelButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(cancelButton) && settingsController != null) {
            settingsController.switchToHomeView();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            SettingsState state = (SettingsState) evt.getNewValue();
            ageField.setText(state.getAge());
            heightField.setText(state.getHeight());
            weightField.setText(state.getWeight());
            errorMessageLabel.setText("");
        } else if ("settingsSaved".equals(evt.getPropertyName())) {
            JOptionPane.showMessageDialog(this, "Age/Height/Weight Saved Successfully!");
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
