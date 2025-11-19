package view;

import interface_adapter.login.LoginController;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The View for when the user is logging into the program.
 */
// Removed unused ActionListener interface since actionPerformed is deleted
public class LoginView extends JPanel implements PropertyChangeListener {

    private final String viewName = "log in";
    private final LoginViewModel loginViewModel;

    private final JTextField usernameInputField = new JTextField(15);
    private final JLabel usernameErrorField = new JLabel();

    private final JPasswordField passwordInputField = new JPasswordField(15);
    private final JLabel passwordErrorField = new JLabel();

    private final JButton logIn;
    // Renamed 'cancel' to 'signUp'
    private final JButton signUp;
    private LoginController loginController = null;


    public LoginView(LoginViewModel loginViewModel) {

        this.loginViewModel = loginViewModel;
        this.loginViewModel.addPropertyChangeListener(this);
        final JLabel title = new JLabel("Login Screen");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final LabelTextPanel usernameInfo = new LabelTextPanel(
                new JLabel("Username"), usernameInputField);
        final LabelTextPanel passwordInfo = new LabelTextPanel(
                new JLabel("Password"), passwordInputField);

        final JPanel buttons = new JPanel();
        logIn = new JButton(LoginViewModel.LOG_IN_BUTTON_LABEL);
        // Renamed button and used a generic label for the Sign Up button
        signUp = new JButton("Sign Up");
        buttons.add(logIn);
        buttons.add(signUp); // Added the renamed button to the panel

        // Action Listener for Log In button
        logIn.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(logIn)) {
                            final LoginState currentState = loginViewModel.getState();

                            loginController.execute(
                                    currentState.getUsername(),
                                    currentState.getPassword()
                            );
                        }
                    }
                }
        );

        // Action Listener for Sign Up button (previously 'cancel')
        signUp.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        // This uses the existing controller method to switch views
                        loginController.switchToSignupView();
                    }
                }
        );

        // Document Listener for Username input
        usernameInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final LoginState currentState = loginViewModel.getState();
                currentState.setUsername(usernameInputField.getText());
                loginViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
        });

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Document Listener for Password input
        passwordInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final LoginState currentState = loginViewModel.getState();
                currentState.setPassword(new String(passwordInputField.getPassword()));
                loginViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
        });

        this.add(title);
        this.add(usernameInfo);
        this.add(usernameErrorField);
        this.add(passwordInfo);
        this.add(buttons);
    }

    // Removed unused actionPerformed method

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final LoginState state = (LoginState) evt.getNewValue();
        setFields(state);
        usernameErrorField.setText(state.getLoginError());
    }

    private void setFields(LoginState state) {
        usernameInputField.setText(state.getUsername());
    }

    public String getViewName() {
        return viewName;
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }
}