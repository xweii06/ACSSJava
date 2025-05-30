package Customer;

import utils.CustomerManager;
import javax.swing.*;
import java.awt.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import navigation.FrameManager;
import utils.MainMenuButton;

public class CustomerLogin extends JFrame {
    private JTextField inputField;
    private JPasswordField passwordField;
    private JComboBox<String> loginMethodBox;
    private JLabel inputLabel;
    private JCheckBox showPW;
    private JButton loginButton, registerButton;

    private static final int MAX_ATTEMPTS = 3;
    private static final long LOCKOUT_DURATION = 5 * 60 * 1000;
    private static int loginAttempts = 0;
    private static long lockoutEndTime = 0;

    public CustomerLogin() {
        setTitle("Customer Login");
        setSize(500, 300);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JLabel instructionLabel = new JLabel("Select login method and enter your credentials");
        instructionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        instructionLabel.setBounds(50, 20, 400, 25);
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(instructionLabel);

        JLabel methodLabel = new JLabel("Login with:");
        methodLabel.setBounds(100, 60, 100, 25);
        methodLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        add(methodLabel);

        loginMethodBox = new JComboBox<>(new String[]{"Customer ID", "Username", "Email"});
        loginMethodBox.setBounds(200, 60, 200, 25);
        loginMethodBox.setFont(new Font("Arial", Font.PLAIN, 14));
        loginMethodBox.addActionListener(e -> updateInputLabel());
        add(loginMethodBox);

        inputLabel = new JLabel("Enter your Customer ID:");
        inputLabel.setBounds(100, 100, 200, 25);
        inputLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        add(inputLabel);

        inputField = new JTextField();
        inputField.setBounds(100, 125, 300, 25);
        inputField.setFont(new Font("Arial", Font.PLAIN, 14));
        add(inputField);

        JLabel pwLabel = new JLabel("Password:");
        pwLabel.setBounds(100, 160, 100, 25);
        pwLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        add(pwLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(200, 160, 200, 25);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        add(passwordField);

        showPW = new JCheckBox("Show Password");
        showPW.setBounds(200, 190, 150, 20);
        showPW.setFont(new Font("Arial", Font.PLAIN, 12));
        showPW.setFocusPainted(false);
        showPW.addActionListener(e -> {
            passwordField.setEchoChar(showPW.isSelected() ? (char) 0 : '•');
        });
        add(showPW);

        // BACK Button
        JButton BACKButton = new JButton("BACK");
        BACKButton.setBounds(80, 220, 100, 30);
        BACKButton.setFont(new Font("Arial", Font.BOLD, 14));
        BACKButton.setBackground(new Color(0x999999));
        BACKButton.setForeground(Color.WHITE);
        BACKButton.setFocusable(false);
        BACKButton.addActionListener(e -> FrameManager.showFrame(new Main.MainMenu()));
        add(BACKButton);
        this.dispose();
        
        // Login Button
        loginButton = new JButton("Login");
        loginButton.setBounds(190, 220, 100, 30);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(new Color(0x08A045));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusable(false);
        loginButton.addActionListener(e -> handleLogin());
        add(loginButton);

        // Register Button
        registerButton = new JButton("Register");
        registerButton.setBounds(300, 220, 100, 30);
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(new Color(0x3465A4));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusable(false);
        registerButton.addActionListener(e -> FrameManager.showFrame(new CustomerRegister()));
        add(registerButton);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void updateInputLabel() {
        String method = (String) loginMethodBox.getSelectedItem();
        if (method != null) {
            switch (method) {
                case "Username" -> inputLabel.setText("Enter your Username:");
                case "Email" -> inputLabel.setText("Enter your Email Address:");
                default -> inputLabel.setText("Enter your Customer ID:");
            }
        }
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

        String method = (String) loginMethodBox.getSelectedItem();
        String input = inputField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        Customer customer = CustomerManager.authenticateBy(method, input, password);
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
                        "Invalid credentials. Attempts left: " + (MAX_ATTEMPTS - loginAttempts),
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        passwordField.setText("");
    }
}
