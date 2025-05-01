package Customer;

import javax.swing.*;
import navigation.FrameManager;

public class EditProfilePage extends JFrame {
    private Customer customer;

    public EditProfilePage(Customer customer) {
        this.customer = customer;

        setTitle("Edit Profile");
        setSize(400, 400);
        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(50, 50, 100, 30);
        add(nameLabel);

        JTextField nameField = new JTextField(customer.getName());
        nameField.setBounds(150, 50, 180, 30);
        add(nameField);

        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setBounds(50, 100, 100, 30);
        add(phoneLabel);

        JTextField phoneField = new JTextField(customer.getPhone());
        phoneField.setBounds(150, 100, 180, 30);
        add(phoneField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 150, 100, 30);
        add(emailLabel);

        JTextField emailField = new JTextField(customer.getEmail());
        emailField.setBounds(150, 150, 180, 30);
        add(emailField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 200, 100, 30);
        add(passwordLabel);

        JPasswordField passwordField = new JPasswordField(customer.getPassword());
        passwordField.setBounds(150, 200, 180, 30);
        add(passwordField);

        JButton saveButton = new JButton("Save");
        saveButton.setBounds(140, 270, 120, 30);
        add(saveButton);

        saveButton.addActionListener(e -> {
            customer.setName(nameField.getText().trim());
            customer.setPhone(phoneField.getText().trim());
            customer.setEmail(emailField.getText().trim());
            customer.setPassword(new String(passwordField.getPassword()).trim());

            CustomerManager.saveToFile();
            JOptionPane.showMessageDialog(this, "Profile Updated!");
            FrameManager.showFrame(new CustomerMainPage(customer));
        });
    }
}
