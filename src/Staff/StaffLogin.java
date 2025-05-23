package Staff;

import java.awt.*;
import java.util.Arrays;
import javax.swing.*;
import navigation.FrameManager;

public class StaffLogin extends JFrame {
    
    private static final String STAFF_FILE = "staff.txt";
    
    private JButton loginButton, backButton;
    private JLabel instructionText, idLabel, pwLabel;
    private JTextField idField;
    private static JPasswordField pwField;
    private static JCheckBox showPW;
    private static int loginAttempts = 0;
    private static long lockoutEndTime = 0;    
    
    public StaffLogin() {
        this.setTitle("Staff Menu");
        this.setSize(500, 250);
        this.setLayout(null);
        this.setResizable(false);
        
        instructionText = new JLabel("Enter your StaffID and StaffPW");
        instructionText.setBounds(50, 20, 400, 20);
        instructionText.setFont(new Font("Arial", Font.BOLD, 14));
        instructionText.setHorizontalAlignment(JLabel.CENTER); 

        idLabel = new JLabel("Staff ID:");
        idLabel.setBounds(100, 60, 100, 20);
        idLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        idField = new JTextField();
        idField.setBounds(200, 60, 200, 25);
        idField.setFont(new Font("Arial", Font.PLAIN, 14));
        
        pwLabel = new JLabel("Password:");
        pwLabel.setBounds(100, 100, 100, 20);
        pwLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        pwField = new JPasswordField();
        pwField.setBounds(200, 100, 200, 25);
        pwField.setFont(new Font("Arial", Font.PLAIN, 14));
        
        showPW = new JCheckBox("Show Password");
        showPW.setBounds(200,125,150,20);
        showPW.setFont(new Font("Arial", Font.PLAIN, 12));
        showPW.setFocusPainted(false);
        showPW.addActionListener(e -> passwordVisibility());
        
        loginButton = new JButton("Login");
        loginButton.setBounds(260, 160, 100, 30);
        loginButton.setFocusable(false);
        loginButton.setForeground(Color.white);
        loginButton.setBackground(new Color(0x08A045));
        loginButton.addActionListener(e -> attemptLogin());
        
        backButton = new JButton("Back");
        backButton.setBounds(140, 160, 100, 30);
        backButton.setFocusable(false);
        backButton.setForeground(Color.white);
        backButton.setBackground(new Color(0x999999));
        backButton.addActionListener(e -> FrameManager.goBack());
        
        this.add(instructionText);
        this.add(idLabel);
        this.add(idField);
        this.add(pwLabel);
        this.add(pwField);
        this.add(showPW);
        this.add(loginButton);
        this.add(backButton);
        this.setVisible(true);
    }
    
    public static void passwordVisibility() {
        if (showPW.isSelected()) {
            pwField.setEchoChar((char)0);
        } else {
            pwField.setEchoChar('•'); 
        }
    }
    
    private void attemptLogin() {
        String staffID = idField.getText().trim().toUpperCase();
        char[] staffPW = pwField.getPassword();
        
        long currentTime = System.currentTimeMillis();
    
        if (currentTime < lockoutEndTime) {
            JOptionPane.showMessageDialog(this,
                "Too many failed attempts. Please try again later.",
                "Locked Out",
                JOptionPane.WARNING_MESSAGE);
            Arrays.fill(staffPW, '0');
            pwField.setText("");
            return;
        }
        
        try {
            Staff staff = validate(staffID, staffPW);
            if (staff != null) {
                handleSuccessfulLogin(staff);
            } else {
                handleFailedLogin(staffID);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error during login: " + ex.getMessage(),
                "Login Error",
                JOptionPane.ERROR_MESSAGE);
        } finally {
            Arrays.fill(staffPW, '0');
            pwField.setText("");
        }
    }   
    
    private Staff validate(String staffID, char[] staffPW) {
        String[] lines = FileManager.getLines(STAFF_FILE);
        if (lines == null) {
            return null;
        }
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length >= 4) {
                String savedID = parts[0];
                String savedPW = parts[3];
                if (savedID.equals(staffID.toUpperCase())) {
                    if (savedPW.equals(new String(staffPW))) {
                        Staff staff = new Staff(parts[0],parts[1],parts[2],parts[3]);
                        return staff; // Login successful!
                    }
                }
            }
        }
        return null;
    }
    
    private void handleSuccessfulLogin(Staff staff) {
        JOptionPane.showMessageDialog(this, 
                    "Welcome back, " + staff.getName());
        System.out.println("StaffID [" + staff.getStaffID() + "] logged in successfully.");
        FrameManager.showFrame(new StaffMenu(staff));
        this.dispose();
    }
    
    private void handleFailedLogin(String staffID) {
        loginAttempts++;
        if (loginAttempts >= 3) {
            lockoutEndTime = System.currentTimeMillis() + (5 * 60 * 1000);
            System.out.println("StaffID [" + staffID + "] locked out for 5 minutes.");
            JOptionPane.showMessageDialog(this,
                "Too many failed attempts. Account locked for 5 minutes.",
                "Account Locked",
                JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Invalid Staff ID or Password.",
                "Login Failed",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}