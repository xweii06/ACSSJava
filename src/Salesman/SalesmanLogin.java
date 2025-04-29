package Salesman;

import java.awt.*;
import java.io.*;
import java.util.Arrays;
import javax.swing.*;
import navigation.FrameManager;
import utils.MainMenuButton;

public class SalesmanLogin extends JFrame {
    private JButton loginButton;
    private JLabel instructionText, idLabel, pwLabel;
    private JTextField salesmanIDField;
    private JPasswordField salesmanPWField;
    private static int loginAttempts = 0;
    private static long lockoutEndTime = 0;

    public SalesmanLogin() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Salesman Login");
        setSize(500, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);
        String filePath = getClass().getResource("/resources/salesman.txt").getPath(); 

        instructionText = new JLabel("Enter your SalesmanID and Password");
        instructionText.setBounds(50, 30, 400, 20);
        instructionText.setFont(new Font("Arial", Font.BOLD, 14));
        instructionText.setHorizontalAlignment(JLabel.CENTER);

        idLabel = new JLabel("Salesman ID:");
        idLabel.setBounds(100, 80, 100, 20);
        idLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        salesmanIDField = new JTextField();
        salesmanIDField.setBounds(200, 80, 200, 25);
        salesmanIDField.setFont(new Font("Arial", Font.PLAIN, 14));

        pwLabel = new JLabel("Password:");
        pwLabel.setBounds(100, 120, 100, 20);
        pwLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        salesmanPWField = new JPasswordField();
        salesmanPWField.setBounds(200, 120, 200, 25);
        salesmanPWField.setFont(new Font("Arial", Font.PLAIN, 14));

        loginButton = new JButton("Login");
        loginButton.setBounds(180, 160, 90, 30);
        loginButton.setFocusable(false);
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(new Color(0x08A045));
        loginButton.addActionListener(e -> attemptLogin());

        add(instructionText);
        add(idLabel);
        add(salesmanIDField);
        add(pwLabel);
        add(salesmanPWField);
        add(loginButton);
        
        MainMenuButton.addToFrame(this);
    }

    private void attemptLogin() {
        String salesmanID = salesmanIDField.getText().trim();
        char[] salesmanPW = salesmanPWField.getPassword();

        if (isAccountLocked()) return;

        try {
            String salesmanName = validateCredentials(salesmanID, salesmanPW);
            if (salesmanName != null) {
                handleSuccessfulLogin(salesmanID, salesmanName);
            } else {
                handleFailedLogin(salesmanID);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                "Error accessing salesman records",
                "System Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            Arrays.fill(salesmanPW, '0');
            salesmanPWField.setText("");
        }
    }

    private boolean isAccountLocked() {
        long currentTime = System.currentTimeMillis();
        if (currentTime < lockoutEndTime) {
            long remainingTime = (lockoutEndTime - currentTime) / 1000;
            long minutes = remainingTime / 60;
            long seconds = remainingTime % 60;
            
            JOptionPane.showMessageDialog(this,
                String.format("Account locked. Please try again in %d minutes and %d seconds.", minutes, seconds),
                "Account Locked",
                JOptionPane.WARNING_MESSAGE);
            return true;
        }
        return false;
    }

    private String validateCredentials(String salesmanID, char[] salesmanPW) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("resources/salesman.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String savedID = parts[0].trim();
                    String savedPW = parts[1].trim();
                    String name = parts[2].trim();

                    if (savedID.equalsIgnoreCase(salesmanID) && savedPW.equals(new String(salesmanPW))) {
                        return name;
                    }
                }
            }
        }
        return null;
    }

    private void handleSuccessfulLogin(String salesmanID, String salesmanName) {
        JOptionPane.showMessageDialog(this,
            "Welcome back, " + salesmanName,
            "Login Successful",
            JOptionPane.INFORMATION_MESSAGE);
        
        System.out.println("SalesmanID [" + salesmanID + "] logged in successfully.");
       
    }

    private void handleFailedLogin(String salesmanID) {
        loginAttempts++;
        if (loginAttempts >= 3) {
            lockoutEndTime = System.currentTimeMillis() + (5 * 60 * 1000);
            System.out.println("SalesmanID [" + salesmanID + "] locked out for 5 minutes.");
            JOptionPane.showMessageDialog(this,
                "Too many failed attempts. Account locked for 5 minutes.",
                "Account Locked",
                JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                "Invalid Salesman ID or Password. Attempts remaining: " + (3 - loginAttempts),
                "Login Failed",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}