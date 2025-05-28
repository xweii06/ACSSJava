package Staff.StaffManagement;

import utils.InputValidator;
import Staff.Staff;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import utils.InvalidInputException;

public class StaffDialog extends JDialog {
    public static final int OK_OPTION = 1;
    public static final int CANCEL_OPTION = 0;
    
    private int option = CANCEL_OPTION;
    private final JTextField idField;
    private final JTextField nameField;
    private final JPasswordField passwordField;
    private final JLabel roleLabel;
    
    public StaffDialog(JFrame parent, String title, String newID) {
        this(parent, title, new Staff(newID, "", "Staff", ""));
    }
    
    public StaffDialog(JFrame parent, String title, Staff staff) {
        super(parent, title, true);
        this.setSize(400, 250);
        this.setLocationRelativeTo(parent);
        
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // ID Field
        formPanel.add(new JLabel("Staff ID:"));
        idField = new JTextField(staff.getId());
        idField.setEditable(false);
        formPanel.add(idField);
        
        // Name Field
        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField(staff.getName());
        formPanel.add(nameField);
        
        // Role can only be staff
        formPanel.add(new JLabel("Role:"));
        roleLabel = new JLabel("Staff");
        formPanel.add(roleLabel);
        
        // Password Field
        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField(staff.getPassword());
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
            getStaff();
            option = OK_OPTION;
            dispose();
        } catch (InvalidInputException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), 
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public Staff getStaff() throws InvalidInputException {
        Staff staff = new Staff(
            idField.getText().trim(),
            nameField.getText().trim(),
            "Staff",
            new String(passwordField.getPassword())
        );
        
        InputValidator.validateName(staff.getName());
        InputValidator.validatePW(staff.getPassword());
        
        return staff;
    }
    
    public int showDialog() {
        this.setVisible(true);
        return option;
    }
}