package Staff;

import java.awt.*;
import java.util.Arrays;
import javax.swing.*;
import navigation.FrameManager;
import utils.*;

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
        
        String staffName = validate(staffID, staffPW);
        if (staffName != null) {
            JOptionPane.showMessageDialog(this, 
                    "Welcome back, " + staffName);
            System.out.println("StaffID [" + staffID + "] login successfully.");
            FrameManager.showFrame(new StaffMenu(staffName));
            this.dispose();
        } else {
            loginAttempts++;
            if (loginAttempts >= 3) {
                lockoutEndTime = currentTime + (5 * 60 * 1000); 
                System.out.println("StaffID [" + staffID + "] locked out for 5 minutes.");
                JOptionPane.showMessageDialog(this,
                    "Too many failed attempts. Locked for 5 minutes.",
                    "Locked Out",
                    JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Invalid Staff ID or Password.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
        Arrays.fill(staffPW, '0');
        pwField.setText("");
    }
    
    private String validate(String staffID, char[] staffPW) {
        String staffData = DataIO.readFile(STAFF_FILE);

        if (staffData != null) {
            String[] lines = staffData.split("\n");
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String savedID = parts[0].trim();
                    String savedpw = parts[1].trim();
                    String staffName = parts[2].trim();
                    
                    if (savedID.equals(staffID)) {
                        if (savedpw.equals(new String(staffPW))) {
                            return staffName; // Login successful!
                        }
                    }
                }
            }
        }
        return null;
    }
}