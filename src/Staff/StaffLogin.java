package Staff;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import javax.swing.*;
import navigation.FrameManager;
import repositories.CarRepository;
import repositories.CustomerRepository;
import repositories.SalesmanRepository;
import repositories.StaffRepository;
import utils.DataIO;

public class StaffLogin extends JFrame {
    
    private static final String STAFF_FILE = "staff.txt";
    
    private final JButton loginButton;
    private final JButton backButton;
    private final JTextField idField;
    private static JPasswordField pwField;
    private static JCheckBox showPW;
    private static int loginAttempts = 0;
    private static long lockoutEndTime = 0;    
    
    public StaffLogin() {
        // frame set up
        this.setTitle("Staff Login");
        this.setSize(500, 250);
        this.setLayout(null);
        this.setResizable(false);
        
        JLabel instructionText = new JLabel("Enter your StaffID and StaffPW");
        instructionText.setBounds(50, 20, 400, 20);
        instructionText.setFont(new Font("Arial", Font.BOLD, 14));
        instructionText.setHorizontalAlignment(JLabel.CENTER); 

        JLabel idLabel = new JLabel("Staff ID:");
        idLabel.setBounds(100, 60, 100, 20);
        idLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        idField = new JTextField();
        idField.setBounds(200, 60, 200, 25);
        idField.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JLabel pwLabel = new JLabel("Password:");
        pwLabel.setBounds(100, 100, 100, 20);
        pwLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        pwField = new JPasswordField();
        pwField.setBounds(200, 100, 200, 25);
        pwField.setFont(new Font("Arial", Font.PLAIN, 14));
        
        showPW = new JCheckBox("Show Password");
        showPW.setBounds(200,125,150,20);
        showPW.setFont(new Font("Arial", Font.PLAIN, 12));
        showPW.setFocusPainted(false);
        showPW.addActionListener(e -> togglePWVisibility());
        
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
    
    public static void togglePWVisibility() {
        pwField.setEchoChar(showPW.isSelected() ? (char)0 : '•');
    }
    
    private void attemptLogin() {
        String staffID = idField.getText().trim().toUpperCase();
        char[] staffPW = pwField.getPassword();
        
        long currentTime = System.currentTimeMillis();
        if (currentTime < lockoutEndTime) {
            long secondsLeft = (lockoutEndTime - currentTime) / 1000;
            JOptionPane.showMessageDialog(this,
                "Too many failed attempts. Please try again in " + secondsLeft + " seconds later.",
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
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                "Error during login: " + ex.getMessage(),
                "Login Error",
                JOptionPane.ERROR_MESSAGE);
        } finally {
            Arrays.fill(staffPW, '0');
            pwField.setText("");
        }
    }   
    
    private Staff validate(String staffID, char[] staffPW) throws IOException {
        String data = DataIO.readFile(STAFF_FILE);
        String[] lines = data.split("\n");
        if (lines == null) { return null; }
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length >= 4 && parts[0].equalsIgnoreCase(staffID)) {
                if (parts[3].equals(new String(staffPW))) {
                    return new Staff(parts[0], parts[1], parts[2], parts[3]);
                }
            }
        }
        return null;
    }
    
    private void handleSuccessfulLogin(Staff staff) {
        JOptionPane.showMessageDialog(this, 
                    "Welcome back, " + staff.getName());
        System.out.println("StaffID [" + staff.getId() + "] logged in successfully.");
        
        StaffRepository staffRepo = new StaffRepository();
        SalesmanRepository salesmanRepo = new SalesmanRepository();
        CustomerRepository customerRepo = new CustomerRepository();
        CarRepository carRepo = new CarRepository();
        
        FrameManager.showFrame(new StaffMenu(staff, staffRepo, salesmanRepo, customerRepo, carRepo));
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