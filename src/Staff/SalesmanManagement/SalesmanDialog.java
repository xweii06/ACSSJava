package Staff.SalesmanManagement;

import Salesman.Salesman;
import utils.InputValidator;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import utils.InvalidInputException;

public class SalesmanDialog extends JDialog {
    public static final int OK_OPTION = 1;
    public static final int CANCEL_OPTION = 0;
    
    private int option = CANCEL_OPTION;
    private final JTextField idField;
    private final JTextField nameField;
    private final JTextField phoneField;
    private final JTextField emailField;
    private final JPasswordField passwordField;
    
    public SalesmanDialog(JFrame parent, String title, String newID) {
        this(parent, title, new Salesman(newID, "", "", "", ""));
    }
    
    public SalesmanDialog(JFrame parent, String title, Salesman salesman) {
        super(parent, title, true);
        this.setSize(400, 250);
        this.setLocationRelativeTo(null);
        
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // ID Field
        formPanel.add(new JLabel("Salesman ID:"));
        idField = new JTextField(salesman.getId());
        idField.setEditable(false);
        formPanel.add(idField);
        
        // Name Field
        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField(salesman.getName());
        formPanel.add(nameField);
        
        // Phone Field
        formPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField(salesman.getPhone());
        formPanel.add(phoneField);
        
        // Email Field
        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField(salesman.getEmail());
        formPanel.add(emailField);
        
        // Password Field
        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField(salesman.getPassword());
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
            getSalesman(); // Validate inputs
            option = OK_OPTION;
            dispose();
        } catch (InvalidInputException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), 
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public Salesman getSalesman() throws InvalidInputException {
        Salesman salesman = new Salesman(
            idField.getText().trim(),
            nameField.getText().trim(),
            phoneField.getText().trim(),
            emailField.getText().trim(),
            new String(passwordField.getPassword())
        );
        
        InputValidator.validateName(salesman.getName());
        InputValidator.validatePhone(salesman.getPhone());
        InputValidator.validateEmail(salesman.getEmail());
        InputValidator.validatePW(salesman.getPassword());
        
        return salesman;
    }
    
    public int showDialog() {
        this.setVisible(true);
        return option;
    }
}