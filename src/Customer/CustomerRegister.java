package Customer;

import javax.swing.*;
import java.awt.*;
import navigation.FrameManager;
import utils.MainMenuButton;

public class CustomerRegister extends JFrame {
    public CustomerRegister() {
        setTitle("Customer Register");
        setSize(500, 400);
        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel instruction = new JLabel("Fill in your details to register");
        instruction.setBounds(50, 20, 400, 25);
        instruction.setFont(new Font("Arial", Font.BOLD, 14));
        instruction.setHorizontalAlignment(SwingConstants.CENTER);
        add(instruction);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(100, 60, 100, 25);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(200, 60, 200, 25);
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));
        add(nameField);

        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setBounds(100, 100, 100, 25);
        phoneLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        add(phoneLabel);

        JTextField phoneField = new JTextField();
        phoneField.setBounds(200, 100, 200, 25);
        phoneField.setFont(new Font("Arial", Font.PLAIN, 14));
        add(phoneField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(100, 140, 100, 25);
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        add(emailLabel);

        JTextField emailField = new JTextField();
        emailField.setBounds(200, 140, 200, 25);
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        add(emailField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(100, 180, 100, 25);
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(200, 180, 200, 25);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        add(passwordField);

        JLabel confirmLabel = new JLabel("Confirm PW:");
        confirmLabel.setBounds(100, 220, 100, 25);
        confirmLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        add(confirmLabel);

        JPasswordField confirmField = new JPasswordField();
        confirmField.setBounds(200, 220, 200, 25);
        confirmField.setFont(new Font("Arial", Font.PLAIN, 14));
        add(confirmField);

        JCheckBox showPW = new JCheckBox("Show Password");
        showPW.setBounds(200, 250, 150, 20);
        showPW.setFont(new Font("Arial", Font.PLAIN, 12));
        showPW.addActionListener(e -> {
            char echo = showPW.isSelected() ? (char) 0 : '•';
            passwordField.setEchoChar(echo);
            confirmField.setEchoChar(echo);
        });
        add(showPW);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(130, 290, 100, 30);
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(new Color(0x3465A4));
        registerButton.setForeground(Color.WHITE);
        add(registerButton);

        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(250, 290, 100, 30);
        exitButton.setFont(new Font("Arial", Font.BOLD, 14));
        exitButton.setBackground(new Color(0xA40000));
        exitButton.setForeground(Color.WHITE);
        exitButton.addActionListener(e -> FrameManager.showFrame(new CustomerLogin()));
        add(exitButton);

        registerButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String confirmPW = new String(confirmField.getPassword()).trim();

            if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPW.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.");
                return;
            }

            if (!password.equals(confirmPW)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match.");
                return;
            }

            // Optionally validate phone and email format here

            String id = CustomerManager.generateCustomerId();
            Customer customer = new Customer(id, name, phone, email, password);
            CustomerManager.addCustomer(customer);

            JOptionPane.showMessageDialog(this, "Registration successful! Your ID is: " + id);
            FrameManager.showFrame(new CustomerLogin());
        });

        MainMenuButton.addToFrame(this);
        setVisible(true);
    }
}
