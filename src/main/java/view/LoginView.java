package view;

import interface_adapter.login.LoginController;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The View for when the user is logging into the program.
 */
public class LoginView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "log in";
    private final LoginViewModel loginViewModel;

    private final JTextField usernameInputField = new JTextField(20);
    private final JLabel usernameErrorField = new JLabel();

    private final JPasswordField passwordInputField = new JPasswordField(20);
    private final JLabel passwordErrorField = new JLabel();

    private final JButton logIn;
    private final JButton signUp;
    private LoginController loginController = null;

    // Modern color scheme - Blue and Green theme
    private static final Color PRIMARY_COLOR = new Color(37, 99, 235); // Blue-600
    private static final Color PRIMARY_HOVER = new Color(29, 78, 216); // Blue-700
    private static final Color SECONDARY_COLOR = new Color(34, 197, 94); // Green-500
    private static final Color SECONDARY_HOVER = new Color(22, 163, 74); // Green-600
    private static final Color BACKGROUND_COLOR = new Color(239, 246, 255); // Light blue tint
    private static final Color CARD_COLOR = new Color(255, 255, 255);
    private static final Color TEXT_COLOR = new Color(31, 41, 55);
    private static final Color ERROR_COLOR = new Color(239, 68, 68);
    private static final Color BORDER_COLOR = new Color(191, 219, 254); // Light blue border

    public LoginView(LoginViewModel loginViewModel) {
        this.loginViewModel = loginViewModel;
        this.loginViewModel.addPropertyChangeListener(this);

        // Set up the main panel with gradient background
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

        // Title
        final JLabel title = new JLabel("Welcome Back");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(TEXT_COLOR);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtitle
        final JLabel subtitle = new JLabel("Please login to your account");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(107, 114, 128));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Style input fields
        styleTextField(usernameInputField);
        styleTextField(passwordInputField);

        // Username section
        JPanel usernamePanel = createInputPanel("Username", usernameInputField);

        // Error label styling
        usernameErrorField.setForeground(ERROR_COLOR);
        usernameErrorField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        usernameErrorField.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameErrorField.setBorder(new EmptyBorder(0, 0, 5, 0));

        // Password section
        JPanel passwordPanel = createInputPanel("Password", passwordInputField);

        // Buttons
        logIn = createStyledButton(LoginViewModel.LOG_IN_BUTTON_LABEL, true);
        signUp = createStyledButton(LoginViewModel.SIGN_UP_BUTTON_LABEL, false);

        final JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
        buttons.setBackground(CARD_COLOR);
        buttons.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttons.add(logIn);
        buttons.add(Box.createRigidArea(new Dimension(0, 10)));
        buttons.add(signUp);

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

        signUp.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        loginController.switchToSignupView();
                    }
                }
        );

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

        // Add components to card panel
        cardPanel.add(title);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        cardPanel.add(subtitle);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        cardPanel.add(usernamePanel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        cardPanel.add(usernameErrorField);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        cardPanel.add(passwordPanel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        cardPanel.add(buttons);

        // Add card to main panel
        this.add(cardPanel);
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
            button.setBackground(PRIMARY_COLOR);
            button.setForeground(Color.WHITE);
            button.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent evt) {
                    button.setBackground(PRIMARY_HOVER);
                }
                public void mouseExited(MouseEvent evt) {
                    button.setBackground(PRIMARY_COLOR);
                }
            });
        } else {
            button.setBackground(SECONDARY_COLOR);
            button.setForeground(Color.WHITE);
            button.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent evt) {
                    button.setBackground(SECONDARY_HOVER);
                }
                public void mouseExited(MouseEvent evt) {
                    button.setBackground(SECONDARY_COLOR);
                }
            });
        }

        return button;
    }

    /**
     * React to a button click that results in evt.
     * @param evt the ActionEvent to react to
     */
    public void actionPerformed(ActionEvent evt) {
        System.out.println("Click " + evt.getActionCommand());
    }

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
