package Salesman;

import java.awt.*;
import java.io.*;
import java.util.Arrays;
import javax.swing.*;
import navigation.FrameManager;
import utils.DataIO;
import utils.MainMenuButton;

public class SalesmanLogin extends JFrame {

    private static final String SALESMAN_FILE = "salesman.txt";
    private static final String SECURITY_QA_FILE = "data/Salesman forgot password.txt"; // Correct path
    private JButton loginButton;
    private JLabel instructionText, idLabel, pwLabel;
    private JTextField salesmanIDField;
    private JPasswordField salesmanPWField;
    private JCheckBox showPassword;
    private JLabel forgotPassword;
    private static int loginAttempts = 0;
    private static long lockoutEndTime = 0;

    public SalesmanLogin() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Salesman Login");
        setSize(500, 280);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);

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

        showPassword = new JCheckBox("Show Password");
        showPassword.setBounds(200, 150, 150, 20);
        showPassword.setFont(new Font("Arial", Font.PLAIN, 12));
        showPassword.setOpaque(false);
        showPassword.addActionListener(e -> {
            if (showPassword.isSelected()) {
                salesmanPWField.setEchoChar((char) 0);
            } else {
                salesmanPWField.setEchoChar('•');
            }
        });

        forgotPassword = new JLabel("<HTML><U>Forgot Password?</U></HTML>");
        forgotPassword.setForeground(Color.BLUE.darker());
        forgotPassword.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        forgotPassword.setBounds(355, 150, 120, 20);
        forgotPassword.setFont(new Font("Arial", Font.PLAIN, 12));
        forgotPassword.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                handleForgotPassword();
            }
        });

        loginButton = new JButton("Login");
        loginButton.setBounds(200, 180, 90, 30);
        loginButton.setFocusable(false);
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(new Color(0x08A045));
        loginButton.addActionListener(e -> attemptLogin());

        add(instructionText);
        add(idLabel);
        add(salesmanIDField);
        add(pwLabel);
        add(salesmanPWField);
        add(showPassword);
        add(forgotPassword);
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
                String.format("Account locked. Try again in %d minutes and %d seconds.", minutes, seconds),
                "Account Locked", JOptionPane.WARNING_MESSAGE);
            return true;
        }
        return false;
    }

    private String validateCredentials(String salesmanID, char[] salesmanPW) throws IOException {
        String data = DataIO.readFile(SALESMAN_FILE);
        if (data != null) {
            String[] lines = data.split("\n");
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    if (parts[0].trim().equals(salesmanID) &&
                        parts[4].trim().equals(new String(salesmanPW))) {
                        return parts[1].trim(); // Return name
                    }
                }
            }
        }
        return null;
    }

    private void handleSuccessfulLogin(String salesmanID, String salesmanName) {
        JOptionPane.showMessageDialog(this,
            "Welcome back, " + salesmanName,
            "Login Successful", JOptionPane.INFORMATION_MESSAGE);
        System.out.println("SalesmanID [" + salesmanID + "] logged in.");
        FrameManager.showFrame(new SalesmanMenu());
    }

    private void handleFailedLogin(String salesmanID) {
        loginAttempts++;
        if (loginAttempts >= 3) {
            lockoutEndTime = System.currentTimeMillis() + (5 * 60 * 1000);
            JOptionPane.showMessageDialog(this,
                "Too many failed attempts. Locked for 5 minutes.",
                "Account Locked", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                "Invalid ID or Password. Attempts remaining: " + (3 - loginAttempts),
                "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleForgotPassword() {
        JTextField idField = new JTextField();
        JTextField answerField = new JTextField();
        JPasswordField newPwField = new JPasswordField();
        JPasswordField confirmPwField = new JPasswordField();

        String id = JOptionPane.showInputDialog(this, "Enter Salesman ID:");
        if (id == null || id.trim().isEmpty()) return;

        String[] qa = getSecurityQA(id);
        if (qa == null) {
            JOptionPane.showMessageDialog(this, "ID not found or error reading file.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.add(new JLabel("Security Question:"));
        panel.add(new JLabel(qa[0]));
        panel.add(new JLabel("Your Answer:"));
        panel.add(answerField);
        panel.add(new JLabel("New Password:"));
        panel.add(newPwField);
        panel.add(new JLabel("Confirm Password:"));
        panel.add(confirmPwField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Reset Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String answer = answerField.getText().trim();
            String newPass = new String(newPwField.getPassword()).trim();
            String confirmPass = new String(confirmPwField.getPassword()).trim();

            if (!answer.equalsIgnoreCase(qa[1].trim())) {
                JOptionPane.showMessageDialog(this, "Incorrect answer.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!newPass.equals(confirmPass)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                resetPassword(id, newPass);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error updating password.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private String[] getSecurityQA(String id) {
        File file = new File(SECURITY_QA_FILE);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 4);
                if (parts.length == 4 && parts[0].trim().equals(id)) {
                    return new String[]{parts[2].trim(), parts[3].trim()};
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading security question file: " + e.getMessage());
        }
        return null;
    }

    private void resetPassword(String id, String newPass) throws IOException {
        File file = new File(SALESMAN_FILE);
        StringBuilder updatedData = new StringBuilder();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5 && parts[0].trim().equals(id)) {
                    found = true;
                    parts[4] = newPass; // Update password
                    updatedData.append(String.join(",", parts)).append("\n");
                } else {
                    updatedData.append(line).append("\n");
                }
            }
        }

        if (found) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(updatedData.toString());
                JOptionPane.showMessageDialog(this, "Password updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Salesman ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SalesmanLogin::new);
    }
}
