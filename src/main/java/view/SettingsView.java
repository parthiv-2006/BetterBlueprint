package view;

import interface_adapter.change_password.ChangePasswordController;
import interface_adapter.logout.LogoutController;
import interface_adapter.settings.SettingsController;
import interface_adapter.settings.SettingsState;
import interface_adapter.settings.SettingsViewModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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

    public SettingsView(SettingsViewModel settingsViewModel) {
        this.settingsViewModel = settingsViewModel;
        this.settingsViewModel.addPropertyChangeListener(this);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        final JLabel title = new JLabel("Settings");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final LabelTextPanel ageInfo = new LabelTextPanel(new JLabel("Age"), ageField);
        final LabelTextPanel heightInfo = new LabelTextPanel(new JLabel("Height (cm)"), heightField);
        final LabelTextPanel weightInfo = new LabelTextPanel(new JLabel("Weight (kg)"), weightField);

        // Save and Cancel buttons panel
        final JPanel topButtons = new JPanel();
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");
        topButtons.add(saveButton);
        topButtons.add(cancelButton);

        // Password section
        final LabelTextPanel passwordInfo = new LabelTextPanel(new JLabel("New Password"), newPasswordField);

        final JPanel passwordButtonPanel = new JPanel();
        changePasswordButton = new JButton("Change Password");
        passwordButtonPanel.add(changePasswordButton);

        // Logout button panel
        final JPanel logoutPanel = new JPanel();
        logoutButton = new JButton("Logout");
        logoutPanel.add(logoutButton);

        addFieldListeners();
        addButtonListeners();

        this.add(title);
        this.add(ageInfo);
        this.add(heightInfo);
        this.add(weightInfo);
        this.add(topButtons);
        this.add(Box.createRigidArea(new Dimension(0, 20)));
        this.add(passwordInfo);
        this.add(passwordButtonPanel);
        this.add(Box.createRigidArea(new Dimension(0, 20)));
        this.add(logoutPanel);
    }

    private void addFieldListeners() {
        ageField.getDocument().addDocumentListener(new DocumentListener() {
            private void update() {
                final SettingsState state = settingsViewModel.getState();
                state.setAge(ageField.getText());
                settingsViewModel.setState(state);
            }

            @Override
            public void insertUpdate(DocumentEvent e) { update(); }

            @Override
            public void removeUpdate(DocumentEvent e) { update(); }

            @Override
            public void changedUpdate(DocumentEvent e) { update(); }
        });

        heightField.getDocument().addDocumentListener(new DocumentListener() {
            private void update() {
                final SettingsState state = settingsViewModel.getState();
                state.setHeight(heightField.getText());
                settingsViewModel.setState(state);
            }

            @Override
            public void insertUpdate(DocumentEvent e) { update(); }

            @Override
            public void removeUpdate(DocumentEvent e) { update(); }

            @Override
            public void changedUpdate(DocumentEvent e) { update(); }
        });

        weightField.getDocument().addDocumentListener(new DocumentListener() {
            private void update() {
                final SettingsState state = settingsViewModel.getState();
                state.setWeight(weightField.getText());
                settingsViewModel.setState(state);
            }

            @Override
            public void insertUpdate(DocumentEvent e) { update(); }

            @Override
            public void removeUpdate(DocumentEvent e) { update(); }

            @Override
            public void changedUpdate(DocumentEvent e) { update(); }
        });
    }

    private void addButtonListeners() {
        saveButton.addActionListener(evt -> {
            if (settingsController != null) {
                final SettingsState state = settingsViewModel.getState();
                settingsController.execute(state.getAge(), state.getHeight(), state.getWeight());
            }
        });

        changePasswordButton.addActionListener(evt -> {
            if (changePasswordController != null) {
                String newPassword = new String(newPasswordField.getPassword());
                if (!newPassword.isEmpty()) {
                    final SettingsState state = settingsViewModel.getState();
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
            final SettingsState state = (SettingsState) evt.getNewValue();
            ageField.setText(state.getAge());
            heightField.setText(state.getHeight());
            weightField.setText(state.getWeight());
        } else if ("passwordChanged".equals(evt.getPropertyName())) {
            JOptionPane.showMessageDialog(this, "Password changed successfully!");
        } else if ("passwordError".equals(evt.getPropertyName())) {
            JOptionPane.showMessageDialog(this, settingsViewModel.getState().getPasswordError());
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
}
