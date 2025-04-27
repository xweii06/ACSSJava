package Staff;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import navigation.FrameManager;

public class StaffMenu extends JFrame {
    
    public StaffMenu() {
        // Frame setup
        this.setTitle("Staff Menu");
        this.setSize(1000, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        // side bar
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(250, getHeight()));
        sidebar.setBackground(Color.black);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        
        // Header
        JLabel pageTitle = new JLabel("Staff Dashboard");
        pageTitle.setFont(new Font("Arial", Font.BOLD, 18));
        pageTitle.setForeground(new Color(0x2A74FF));
        pageTitle.setBorder(BorderFactory.createEmptyBorder(20, 20, 30, 20));
        sidebar.add(pageTitle);
        
        // Menu Items
        String[] menuItems = {"Staff Management", "Salesman Management", 
            "Customers Management", "Car Management", "Payment & Feedback Analysis", "Reports"};
        for (String menuItem : menuItems) {
            JButton menuButton = new JButton(menuItem);
            styleMenuButton(menuButton);
            sidebar.add(menuButton);
            
            // Action
            menuButton.addActionListener(e -> {
                // Switch content based on button clicked
                JPanel contentPanel = (JPanel) ((BorderLayout) getContentPane().getLayout()).getLayoutComponent(BorderLayout.CENTER);
                
                // Simple content switching - replace with actual components
                contentPanel.add(new JLabel(menuItem + " Content", SwingConstants.CENTER));
                
                contentPanel.revalidate();
                contentPanel.repaint();
            });
            
        sidebar.add(menuButton);

        // content
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.white);
        contentPanel.add(new JLabel("Select a menu option", SwingConstants.CENTER));

        // Add components to main panel
        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
        }
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
}