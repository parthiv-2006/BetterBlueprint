package view;

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

    private final JTextField ageField = new JTextField(15);
    private final JTextField heightField = new JTextField(15);
    private final JTextField weightField = new JTextField(15);
    private final JButton saveButton;
    private final JButton cancelButton;

    public SettingsView(SettingsViewModel settingsViewModel) {
        this.settingsViewModel = settingsViewModel;
        this.settingsViewModel.addPropertyChangeListener(this);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        final JLabel title = new JLabel("Settings");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final LabelTextPanel ageInfo = new LabelTextPanel(new JLabel("Age"), ageField);
        final LabelTextPanel heightInfo = new LabelTextPanel(new JLabel("Height (cm)"), heightField);
        final LabelTextPanel weightInfo = new LabelTextPanel(new JLabel("Weight (kg)"), weightField);

        final JPanel buttons = new JPanel();
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");
        buttons.add(saveButton);
        buttons.add(cancelButton);

        addFieldListeners();
        addButtonListeners();

        this.add(title);
        this.add(ageInfo);
        this.add(heightInfo);
        this.add(weightInfo);
        this.add(buttons);
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
        }
    }

    public String getViewName() {
        return viewName;
    }

    public void setSettingsController(SettingsController controller) {
        this.settingsController = controller;
    }
}