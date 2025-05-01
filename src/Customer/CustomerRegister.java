package Customer;

import javax.swing.*;
import navigation.FrameManager;

public class CustomerRegister extends JFrame {
    public CustomerRegister() {
        setTitle("Customer Register");
        setSize(400, 400);
        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(50, 50, 100, 30);
        add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(150, 50, 180, 30);
        add(nameField);

        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setBounds(50, 100, 100, 30);
        add(phoneLabel);

        JTextField phoneField = new JTextField();
        phoneField.setBounds(150, 100, 180, 30);
        add(phoneField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 150, 100, 30);
        add(emailLabel);

        JTextField emailField = new JTextField();
        emailField.setBounds(150, 150, 180, 30);
        add(emailField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 200, 100, 30);
        add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(150, 200, 180, 30);
        add(passwordField);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(140, 270, 120, 30);
        add(registerButton);

        registerButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.");
                return;
            }

            String id = CustomerManager.generateCustomerId();
            Customer customer = new Customer(id, name, phone, email, password);
            CustomerManager.addCustomer(customer);

            JOptionPane.showMessageDialog(this, "Register successful! Your ID is: " + id);
            FrameManager.showFrame(new CustomerLogin());
        });
    }
}
