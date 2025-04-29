package Staff;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import navigation.FrameManager;

public class StaffMenu extends JFrame {
    
    private JPanel mainPanel, sidebar, contentPanel, subMenuPanel;
    private JLabel pageTitle, instructionText;
    
    public StaffMenu(String staffName) {
        // Frame setup
        this.setTitle("Staff Menu");
        this.setSize(1000, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        
        // Main panel with BorderLayout
        mainPanel = new JPanel(new BorderLayout());
        
        // Create sidebar
        sidebar = createSidebar();
        mainPanel.add(sidebar, BorderLayout.WEST);
        
        // Create content area
        contentPanel = new JPanel();
        contentPanel.setBackground(Color.white);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Set default content
        showDefaultContent(contentPanel,staffName);
        
        this.add(mainPanel);
        this.setVisible(true);
    }
    
    private JPanel createSidebar() {
        sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(250, getHeight()));
        sidebar.setBackground(Color.black);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        
        // Header
        pageTitle = new JLabel("Staff Dashboard");
        pageTitle.setFont(new Font("Arial", Font.BOLD, 18));
        pageTitle.setForeground(new Color(0x2A74FF));
        pageTitle.setBorder(BorderFactory.createEmptyBorder(20, 20, 30, 20));
        sidebar.add(pageTitle);
        
        // Menu Items
        String[] menuItems = {"Staff Management", "Salesman Management", "Customers Management", "Car Management", 
        "Payment & Feedback Analysis", "Reports", "End Program"};
        
        for (String menuItem : menuItems) {
            JButton menuButton = new JButton(menuItem);
            styleMenuButton(menuButton);
            sidebar.add(menuButton);
            menuButton.addActionListener(e -> switchContent(menuItem));
        }
        
        sidebar.add(Box.createVerticalGlue()); 
        JButton logoutBtn = new JButton("Logout");
        styleMenuButton(logoutBtn);
        logoutBtn.addActionListener(e -> {
            FrameManager.goBack();
            dispose();
        });
        sidebar.add(logoutBtn);
        return sidebar;
    }
    
    private void switchContent(String menuItem) {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        
        subMenuPanel = new JPanel(new GridLayout(0,1,0,10));
        subMenuPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        
        switch(menuItem) {
            
            case "Staff Management":
                addSubMenuButton(subMenuPanel, "Add New Staff"); // +
                addSubMenuButton(subMenuPanel, "Delete Staff"); // X
                addSubMenuButton(subMenuPanel, "Search Staff"); // magnifying glass
                addSubMenuButton(subMenuPanel, "Update Staff"); // idk
                break;
            case "Salesman Management":
                contentPanel.add(new JLabel("Salesman Management Content", SwingConstants.CENTER));
                break;
            case "Customers Management":
                contentPanel.add(new JLabel("Customers Management Content", SwingConstants.CENTER));
                break;
            case "Car Management":
                contentPanel.add(new JLabel("Car Management Content", SwingConstants.CENTER));
                break;
            case "Payment & Feedback Analysis":
                contentPanel.add(new JLabel("Payment Analysis Content", SwingConstants.CENTER));
                break;
            case "Reports":
                contentPanel.add(new JLabel("Reports Content", SwingConstants.CENTER));
                break;
            case "End Program":
                String exitPIN = JOptionPane.showInputDialog("Enter Exit PIN:");
                
                if (exitPIN != null){
                    try {
                        boolean validation = checkExitPIN(exitPIN);
                        if (validation) {
                            int confirmation 
                                    = JOptionPane.showConfirmDialog(null,"Are you sure?",
                                            "Confirmation",JOptionPane.YES_NO_OPTION);
                            if (confirmation == 0) {
                                System.exit(0);
                            }
                        } else if (!validation) {
                            JOptionPane.showMessageDialog(null,"Invalid PIN.","Error",JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(this,
                            "Error accessing records",
                            "System Error",
                            JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
                break;
        }
        
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.add(subMenuPanel);
        contentPanel.add(centerPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showDefaultContent(JPanel contentPanel, String staffName) {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());

        JLabel instructionText = new JLabel(
                "Welcome back " + staffName +"! Please select a menu option", 
                SwingConstants.CENTER);
        instructionText.setFont(new Font("Arial", Font.PLAIN, 18));
        instructionText.setForeground(new Color(150,150,150));
        
        contentPanel.add(instructionText, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void styleMenuButton(JButton button) {
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(250, 50));
        button.setPreferredSize(new Dimension(250, 50));
        button.setBackground(Color.black);
        button.setForeground(Color.white);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFocusPainted(false);
        
        // Hover effects
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(30, 30, 40));
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.black);
            }
        });
    }
    
    private void addSubMenuButton(JPanel panel, String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 40));
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        //button.addActionListener(action);
        panel.add(button);
    }
    
    private boolean checkExitPIN(String exitPIN) throws IOException{
        String filePath = getClass().getResource("/resources/exitPIN.txt").getPath(); 
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            String savedExitPIN = reader.readLine();
            if (savedExitPIN != null){
                if (savedExitPIN.equals(exitPIN)){
                    return true;
                }
            }
        }
        return false;
    }
}