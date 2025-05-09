package Customer;

import utils.CustomerManager;
import javax.swing.*;
import java.awt.*;
import navigation.FrameManager;

public class EditProfilePage extends JPanel {
    private JTextField nameField, phoneField, emailField;
    private JPasswordField passwordField, confirmPasswordField;
    private JCheckBox showPasswordCheckbox;
    private Customer customer;

    public EditProfilePage(Customer customer) {
        this.customer = customer;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Top section with avatar and title
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        // Avatar Icon
        JLabel avatarLabel = new JLabel(new ImageIcon(
                new ImageIcon("src/resources/avatar.png")
                        .getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
        avatarLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(avatarLabel);

        // Title
        JLabel titleLabel = new JLabel("Edit Your Profile");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(new Color(0x2A74FF));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(titleLabel);
        topPanel.add(Box.createVerticalStrut(20));

        add(topPanel, BorderLayout.NORTH);

        // === Form Panel ===
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);

        nameField = new JTextField(customer.getName());
        phoneField = new JTextField(customer.getPhone());
        emailField = new JTextField(customer.getEmail());
        passwordField = new JPasswordField(customer.getPassword());
        confirmPasswordField = new JPasswordField(customer.getPassword());

        JTextField[] fields = {
                nameField, phoneField, emailField, passwordField, confirmPasswordField
        };
        String[] labels = {
                "Full Name", "Phone Number", "Email Address", "Password", "Confirm Password"
        };

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;

            JLabel label = new JLabel(labels[i]);
            label.setFont(labelFont);
            formPanel.add(label, gbc);

            gbc.gridx = 1;
            fields[i].setFont(fieldFont);
            fields[i].setPreferredSize(new Dimension(300, 35));
            formPanel.add(fields[i], gbc);
        }

        // Show password checkbox
        showPasswordCheckbox = new JCheckBox("Show Password");
        showPasswordCheckbox.setBackground(Color.WHITE);
        showPasswordCheckbox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridx = 1;
        gbc.gridy = labels.length;
        formPanel.add(showPasswordCheckbox, gbc);

        showPasswordCheckbox.addActionListener(e -> {
            char echo = showPasswordCheckbox.isSelected() ? (char) 0 : '•';
            passwordField.setEchoChar(echo);
            confirmPasswordField.setEchoChar(echo);
        });

        // Save Button
        JButton saveButton = new JButton("Save Changes");
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        saveButton.setBackground(new Color(0x2A74FF));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setPreferredSize(new Dimension(200, 40));

        gbc.gridx = 0;
        gbc.gridy = labels.length + 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(saveButton, gbc);

        saveButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String confirmPassword = new String(confirmPasswordField.getPassword()).trim();

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            customer.setName(name);
            customer.setPhone(phone);
            customer.setEmail(email);
            if (!password.isEmpty()) {
                customer.setPassword(password);
            }

            CustomerManager.saveToFile();
            JOptionPane.showMessageDialog(this, "Profile updated successfully!");
            FrameManager.showFrame(new CustomerMainPage(customer));
        });

        // === Scroll Pane ===
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // smoother scrolling

        add(scrollPane, BorderLayout.CENTER);
    }
}
