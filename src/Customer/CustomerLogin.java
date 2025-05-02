package Customer;

import javax.swing.*;
import java.awt.*;
import navigation.FrameManager;
import utils.MainMenuButton;

public class CustomerLogin extends JFrame {
    private JTextField idField;
    private JPasswordField passwordField;
    private JCheckBox showPW;
    private JButton loginButton, registerButton;

    private static final int MAX_ATTEMPTS = 3;
    private static final long LOCKOUT_DURATION = 5 * 60 * 1000;
    private static int loginAttempts = 0;
    private static long lockoutEndTime = 0;

    public CustomerLogin() {
        setTitle("Customer Login");
        setSize(500, 250);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // Title Label
        JLabel titleLabel = new JLabel("Enter your Customer ID and Password");
        titleLabel.setBounds(50, 20, 400, 20);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        add(titleLabel);

        // Customer ID Label
        JLabel idLabel = new JLabel("Customer ID:");
        idLabel.setBounds(100, 60, 100, 20);
        idLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        add(idLabel);

        // Customer ID Field
        idField = new JTextField();
        idField.setBounds(200, 60, 200, 25);
        idField.setFont(new Font("Arial", Font.PLAIN, 14));
        add(idField);

        // Password Label
        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(100, 100, 100, 20);
        passLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        add(passLabel);

        // Password Field
        passwordField = new JPasswordField();
        passwordField.setBounds(200, 100, 200, 25);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        add(passwordField);

        // Show Password Checkbox
        showPW = new JCheckBox("Show Password");
        showPW.setBounds(200, 125, 150, 20);
        showPW.setFont(new Font("Arial", Font.PLAIN, 12));
        showPW.setFocusPainted(false);
        showPW.addActionListener(e -> {
            if (showPW.isSelected()) {
                passwordField.setEchoChar((char) 0); // Show
            } else {
                passwordField.setEchoChar('•');      // Hide
            }
        });
        add(showPW);

        // Login Button
        loginButton = new JButton("Login");
        loginButton.setBounds(180, 160, 90, 30);
        loginButton.setFocusable(false);
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(new Color(0x08A045)); // Green
        loginButton.addActionListener(e -> handleLogin());
        add(loginButton);

        // Register Button (Styled Blue-Grey)
        registerButton = new JButton("Register");
        registerButton.setBounds(280, 160, 90, 30);
        registerButton.setFocusable(false);
        registerButton.setForeground(Color.WHITE);
        registerButton.setBackground(new Color(0x3465A4)); // Blue-grey
        registerButton.addActionListener(e -> FrameManager.showFrame(new CustomerRegister()));
        add(registerButton);

        MainMenuButton.addToFrame(this);
        setVisible(true);
    }

    private void handleLogin() {
        long currentTime = System.currentTimeMillis();
        if (currentTime < lockoutEndTime) {
            long secondsLeft = (lockoutEndTime - currentTime) / 1000;
            JOptionPane.showMessageDialog(this,
                    "Too many failed attempts. Try again in " + secondsLeft + " seconds.",
                    "Locked Out",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = idField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        Customer customer = CustomerManager.authenticate(id, password);
        if (customer != null) {
            JOptionPane.showMessageDialog(this, "Login successful!");
            loginAttempts = 0;
            FrameManager.showFrame(new CustomerMainPage(customer));
            this.dispose();
        } else {
            loginAttempts++;
            if (loginAttempts >= MAX_ATTEMPTS) {
                lockoutEndTime = currentTime + LOCKOUT_DURATION;
                JOptionPane.showMessageDialog(this,
                        "Too many failed attempts. Locked for 5 minutes.",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Invalid ID or Password. Attempts left: " + (MAX_ATTEMPTS - loginAttempts),
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        // Clear password field to avoid reuse or exposure
        passwordField.setText("");
    }
}
