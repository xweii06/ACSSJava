package Customer;

import javax.swing.*;
import java.awt.*;
import navigation.FrameManager;
import utils.MainMenuButton;

public class CustomerLogin extends JFrame {
    private JTextField idField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;

    private static final int MAX_ATTEMPTS = 3;
    private static final long LOCKOUT_DURATION = 5 * 60 * 1000; // 5 minutes in ms
    private static int loginAttempts = 0;
    private static long lockoutEndTime = 0;

    public CustomerLogin() {
        setTitle("Customer Login");
        setSize(500, 250);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JLabel titleLabel = new JLabel("Enter your Customer ID and Password");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        titleLabel.setBounds(120, 10, 300, 25);
        add(titleLabel);

        JLabel idLabel = new JLabel("Customer ID:");
        idLabel.setBounds(50, 50, 100, 25);
        add(idLabel);

        idField = new JTextField();
        idField.setBounds(150, 50, 200, 25);
        add(idField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 90, 100, 25);
        add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 90, 200, 25);
        add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setBounds(100, 140, 100, 30);
        loginButton.setBackground(new Color(0, 153, 0));
        loginButton.setForeground(Color.WHITE);
        loginButton.addActionListener(e -> handleLogin());
        add(loginButton);

        registerButton = new JButton("Register");
        registerButton.setBounds(220, 140, 100, 30);
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
            loginAttempts = 0; // reset on success
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

        // Clear the password field after failed login to avoid reuse or exposure
        passwordField.setText("");
    }
}
