package Main;

import navigation.*;
import Customer.CustomerLogin;
import Staff.StaffLogin;
import Salesman.SalesmanLogin;

import java.awt.*;
import javax.swing.*;

public class MainMenu extends JFrame {
    
    // Create buttons
    private JButton customerButton;
    private JButton salesmanButton;
    private JButton staffButton;
    
    // Create labels
    private JLabel welcomeText;
    private JLabel instructionText;
    
    public MainMenu(){
        // Frame setup
        this.setTitle("APU Car Sales System");
        this.setSize(500, 250); // size can change
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);  // temporarily 
        this.setLayout(null);
        this.setResizable(false);
        
        // Welcome message
        welcomeText = new JLabel("Welcome to APU Car Sales System");
        welcomeText.setBounds(50, 40, 400, 30);
        welcomeText.setFont(new Font("Arial", Font.BOLD, 18)); // font can change
        welcomeText.setHorizontalAlignment(JLabel.CENTER); 
        
        // Instructions
        instructionText = new JLabel("Please select your role:");
        instructionText.setBounds(50, 70, 400, 20);
        instructionText.setFont(new Font("Arial", Font.PLAIN, 14));
        instructionText.setHorizontalAlignment(JLabel.CENTER);
        instructionText.setForeground(new Color(150, 150, 150));
        
        // Initialize buttons
        customerButton = new JButton("Customer");
        salesmanButton = new JButton("Salesman");
        staffButton = new JButton("Managing Staff");
        
        // Set button positions and sizes
        customerButton.setBounds(40, 100, 120, 40);
        customerButton.setFocusable(false);
        salesmanButton.setBounds(180, 100, 120, 40);
        salesmanButton.setFocusable(false);
        staffButton.setBounds(320, 100, 120, 40);
        staffButton.setFocusable(false);
        
        // Actions
        customerButton.addActionListener(e -> {
            FrameManager.showFrame(new CustomerLogin());
        });
        
        salesmanButton.addActionListener(e -> {
            FrameManager.showFrame(new SalesmanLogin());
        });
        
        staffButton.addActionListener(e -> {
            FrameManager.showFrame(new StaffLogin());
        });
        
        // Add labels
        this.add(welcomeText);
        this.add(instructionText);
        
        // Add buttons to frame
        this.add(customerButton);
        this.add(salesmanButton);
        this.add(staffButton);
        
        this.setVisible(true);
    }
    
    public static void main(String[] args) {
        FrameManager.showFrame(new MainMenu()); 
    }
}