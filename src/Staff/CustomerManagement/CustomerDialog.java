package Staff.CustomerManagement;

import Customer.Customer;
import utils.InvalidInputException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class CustomerDialog extends JDialog {
    public static final int OK_OPTION = 1;
    public static final int CANCEL_OPTION = 0;
    
    private int option = CANCEL_OPTION;
    private final JTextField idField;
    private final JTextField nameField;
    private final JTextField phoneField;
    private final JTextField emailField;
    private final JPasswordField passwordField;
    
    public CustomerDialog(JFrame parent, String title, Customer customer) {
        super(parent, title, true);
        this.setSize(450, 300);
        this.setLocationRelativeTo(null);
        
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // ID Field
        formPanel.add(new JLabel("Customer ID:"));
        idField = new JTextField(customer != null ? customer.getId() : "");
        idField.setEditable(false);
        formPanel.add(idField);
        
        // Name Field
        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField(customer != null ? customer.getName() : "");
        formPanel.add(nameField);
        
        // Phone Field
        formPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField(customer != null ? customer.getPhone() : "");
        formPanel.add(phoneField);
        
        // Email Field
        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField(customer != null ? customer.getEmail() : "");
        formPanel.add(emailField);
        
        // Password Field
        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        if (customer != null) {
            passwordField.setText(customer.getPassword());
        }
        formPanel.add(passwordField);
        
        JCheckBox showPW = new JCheckBox("Show Password");
        showPW.setFocusPainted(false);
        showPW.addActionListener(e -> {
            passwordField.setEchoChar(showPW.isSelected() ? (char)0 : '•');
        });
        formPanel.add(new JLabel(""));
        formPanel.add(showPW);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        
        okButton.addActionListener(this::handleOk);
        cancelButton.addActionListener(e -> {
            option = CANCEL_OPTION;
            dispose();
        });
        
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        
        this.add(formPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void handleOk(ActionEvent e) {
        try {
            getCustomer();
            option = OK_OPTION;
            this.dispose();
        } catch (InvalidInputException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), 
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public Customer getCustomer() throws InvalidInputException {
        return new Customer(
            idField.getText().trim(),
            nameField.getText().trim(),
            phoneField.getText().trim(),
            emailField.getText().trim(),
            new String(passwordField.getPassword())
        );
    }
    
    public int showDialog() {
        this.setVisible(true);
        return option;
    }
}