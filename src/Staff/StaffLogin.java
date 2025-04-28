package Staff;

import java.awt.*;
import java.io.*;
import java.util.Arrays;
import javax.swing.*;
import navigation.FrameManager;
import utils.*;

public class StaffLogin extends JFrame{
    
    private JButton loginButton;
    private JLabel instructionText, idLabel, pwLabel;
    private JTextField staffIDField;
    private JPasswordField staffPWField;
    private static int loginAttempts = 0;
    private static long lockoutEndTime = 0;    
  
    public StaffLogin(){
        // Frame setup
        this.setTitle("Staff Login");
        this.setSize(500, 250);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE); // temporarily
        this.setLayout(null);
        this.setResizable(false);
        
        instructionText = new JLabel("Enter your StaffID and StaffPW");
        instructionText.setBounds(50, 30, 400, 20);
        instructionText.setFont(new Font("Arial", Font.BOLD, 14)); // font can change
        instructionText.setHorizontalAlignment(JLabel.CENTER); 
        
        // Staff ID Label
        idLabel = new JLabel("Staff ID:");
        idLabel.setBounds(100, 80, 100, 20);
        idLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Staff ID Text Field
        staffIDField = new JTextField();
        staffIDField.setBounds(200, 80, 200, 25);
        staffIDField.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Password Label
        pwLabel = new JLabel("Password:");
        pwLabel.setBounds(100, 120, 100, 20);
        pwLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Password Field
        staffPWField = new JPasswordField();
        staffPWField.setBounds(200, 120, 200, 25);
        staffPWField.setFont(new Font("Arial", Font.PLAIN, 14));
        
        loginButton = new JButton("Login");
        loginButton.setBounds(180, 160, 90, 30);
        loginButton.setFocusable(false);
        loginButton.setForeground(Color.white);
        loginButton.setBackground(new Color(0x08A045));
        
        loginButton.addActionListener(e -> attemptLogin());
        
        this.add(instructionText);
        this.add(idLabel);
        this.add(staffIDField);
        this.add(pwLabel);
        this.add(staffPWField);
        this.add(loginButton);
        MainMenuButton.addToFrame(this);
        this.setVisible(true);
    }
    
    private void attemptLogin() {
        String staffID = staffIDField.getText().trim().toUpperCase();
        char[] staffPW = staffPWField.getPassword();
        
        long currentTime = System.currentTimeMillis();
    
        if (currentTime < lockoutEndTime) {
            JOptionPane.showMessageDialog(this,
                "Too many failed attempts. Please try again later.",
                "Locked Out",
                JOptionPane.WARNING_MESSAGE);
            Arrays.fill(staffPW, '0');
            staffPWField.setText("");
            return;
        }
        
        try {
            String staffName = validate(staffID, staffPW);
            if (staffName != null) {
                JOptionPane.showMessageDialog(this, 
                        "Welocome back, " + staffName);
                System.out.println("StaffID [" + staffID + "] login successfully.");
                FrameManager.showFrame(new StaffMenu());
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
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                "Error accessing staff records",
                "System Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            Arrays.fill(staffPW, '0');
            staffPWField.setText("");
        }
    }
    
    private String validate(String staffID, char[] staffPW) throws IOException {
        String filePath = getClass().getResource("/resources/staff.txt").getPath();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String savedStaffID = parts[0].trim();
                    String savedStaffPW = parts[1].trim();
                    String staffName = parts[2].trim();
                    
                    if (savedStaffID.equals(staffID) && 
                        savedStaffPW.equals(new String(staffPW))) {
                        return staffName;   
                    }
                }
            }
        }
        return null;
    }
}
