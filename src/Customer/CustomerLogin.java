package Customer;

import javax.swing.*;
import java.awt.*;
import utils.MainMenuButton;
import navigation.FrameManager;

public class CustomerLogin extends JFrame {
    private JTextField idField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;

    public CustomerLogin() {
        setTitle("Customer Login");
        setSize(500, 250);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // Title label
        JLabel titleLabel = new JLabel("Enter your Customer ID and Password");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        titleLabel.setBounds(120, 10, 300, 25);
        add(titleLabel);

        // Customer ID label & field
        JLabel idLabel = new JLabel("Customer ID:");
        idLabel.setBounds(50, 50, 100, 25);
        add(idLabel);

        idField = new JTextField();
        idField.setBounds(150, 50, 200, 25);
        add(idField);

        // Password label & field
        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 90, 100, 25);
        add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 90, 200, 25);
        add(passwordField);

        // Login button (green)
        loginButton = new JButton("Login");
        loginButton.setBounds(100, 140, 100, 30);
        loginButton.setBackground(new Color(0, 153, 0));
        loginButton.setForeground(Color.WHITE);
        loginButton.addActionListener(e -> handleLogin());
        add(loginButton);

        // Register button
        registerButton = new JButton("Register");
        registerButton.setBounds(220, 140, 100, 30);
        registerButton.addActionListener(e -> FrameManager.showFrame(new CustomerRegister()));
        add(registerButton);

        // Main Menu button from utils
        MainMenuButton.addToFrame(this);

        setVisible(true);
    }

    private void handleLogin() {
        String id = idField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        Customer customer = CustomerManager.authenticate(id, password);
        if (customer != null) {
            JOptionPane.showMessageDialog(this, "Login successful!");
            FrameManager.showFrame(new CustomerMainPage(customer));
        } else {
            JOptionPane.showMessageDialog(this, "Invalid ID or Password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
