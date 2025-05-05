package Staff;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.Border;
import navigation.FrameManager;
import utils.*;

public class StaffMenu extends JFrame {
    
    private static final String EXITPIN_FILE = "/data/exitPIN.txt";
    
    private static final String ADDUSER_PNG = "add_user.png", DELUSER_PNG = "del_user.png",
            SEARCHUSER_PNG = "user_search.png", UPDATEUSER_PNG = "edit_user.png", 
            APPROVEUSER_PNG = "approve_user.png", ADDCAR_PNG = "add_car.png", DELCAR_PNG = "del_car.png", 
            UPDATECAR_PNG = "edit_car.png", SEARCHCAR_PNG = "search_car.png";
    
    private JPanel mainPanel, sidebar, contentPanel, titlePanel, subMenuPanel;
    private JLabel pageTitle, titleLabel;
    private JButton currentlySelectedButton = null; 
    
    public StaffMenu(String staffName) {
        this.setTitle("Staff Menu");
        this.setSize(1000, 600);
        this.setLocationRelativeTo(null);
        
        mainPanel = new JPanel(new BorderLayout());
        
        JPanel sidebarContent = createSidebar(staffName);
        JScrollPane sidebarScroll = new JScrollPane(sidebarContent);
        sidebarScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sidebarScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        sidebarScroll.setBorder(null);
        mainPanel.add(sidebarScroll, BorderLayout.WEST);
        
        contentPanel = new JPanel();
        contentPanel.setBackground(Color.white);
        JScrollPane contentScroll = new JScrollPane(contentPanel);
        contentScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        contentScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainPanel.add(contentScroll, BorderLayout.CENTER);
        
        this.add(mainPanel);
        showDefaultContent(contentPanel, staffName);
        this.setVisible(true);
    }
    
    private JPanel createSidebar(String staffName) {
        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Color.black);
        sidebar.setMaximumSize(new Dimension(180, 600));
    
        pageTitle = new JLabel("Staff Dashboard");
        pageTitle.setFont(new Font("Arial", Font.BOLD, 18));
        pageTitle.setForeground(new Color(0x2A74FF));
        pageTitle.setBorder(BorderFactory.createEmptyBorder(20, 20, 30, 20));
        sidebar.add(pageTitle);
        
        ArrayList<String> menuItems = new ArrayList<>();
        if (staffName.equals("SuperAdmin")){
            menuItems.add("Staff Management"); 
            menuItems.add("End Program");
        } else {
            menuItems.add("Salesman Management");
            menuItems.add("Customers Management"); 
            menuItems.add("Car Management");
            menuItems.add("Payment & Feedback Analysis");
            menuItems.add("Reports");
            menuItems.add("End Program");
        }
        
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
            this.dispose();
        });
        sidebar.add(logoutBtn);
        return sidebar;
    }
    
    private void switchContent(String menuItem) {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        
        titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20));
        titlePanel.setBackground(Color.lightGray);

        titleLabel = new JLabel(menuItem);
        titleLabel.setFont(new Font("MV Boli", Font.BOLD, 30));
        titleLabel.setForeground(Color.black);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        titlePanel.add(titleLabel, BorderLayout.CENTER);
        contentPanel.add(titlePanel, BorderLayout.NORTH);
        
        subMenuPanel = new JPanel();
        subMenuPanel.setLayout(new GridLayout(0, 4, 15, 15));
        subMenuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        subMenuPanel.setBackground(Color.white);
        
        switch(menuItem) {
            case "Staff Management":
            case "Salesman Management":
                addSubMenuButton(subMenuPanel, "Add New User",
                        DataIO.loadIcon(ADDUSER_PNG),AddNew.addNew(menuItem));
                addSubMenuButton(subMenuPanel, "Delete User",
                        DataIO.loadIcon(DELUSER_PNG),null);
                addSubMenuButton(subMenuPanel, "Search User",
                        DataIO.loadIcon(SEARCHUSER_PNG),null);
                addSubMenuButton(subMenuPanel, "Update User Info",
                        DataIO.loadIcon(UPDATEUSER_PNG),null);
                break;
            case "Customers Management":
                addSubMenuButton(subMenuPanel, "Approve User",
                        DataIO.loadIcon(APPROVEUSER_PNG),ApproveCus.approveCus());
                addSubMenuButton(subMenuPanel, "Delete User",
                        DataIO.loadIcon(DELUSER_PNG),null);
                addSubMenuButton(subMenuPanel, "Search User",
                        DataIO.loadIcon(SEARCHUSER_PNG),null);
                addSubMenuButton(subMenuPanel, "Update User Info",
                        DataIO.loadIcon(UPDATEUSER_PNG),null);
                break;
            case "Car Management":
                addSubMenuButton(subMenuPanel, "Add New Car",
                        DataIO.loadIcon(ADDCAR_PNG),AddNew.addNew(menuItem));
                addSubMenuButton(subMenuPanel, "Delete Car",
                        DataIO.loadIcon(DELCAR_PNG),null);
                addSubMenuButton(subMenuPanel, "Search Car",
                        DataIO.loadIcon(SEARCHCAR_PNG),null);
                addSubMenuButton(subMenuPanel, "Update Car Info",
                        DataIO.loadIcon(UPDATECAR_PNG),null);
                break;
            case "Payment & Feedback Analysis":
                addSubMenuButton(subMenuPanel, "Payment Records",
                        DataIO.loadIcon(ADDCAR_PNG),null);
                addSubMenuButton(subMenuPanel, "Pending Payments",
                        DataIO.loadIcon(DELCAR_PNG),null);
                addSubMenuButton(subMenuPanel, "User Feedback",
                        DataIO.loadIcon(SEARCHCAR_PNG),null);
                addSubMenuButton(subMenuPanel, "Rating Analysis",
                        DataIO.loadIcon(UPDATECAR_PNG),null);
                break;
            case "Reports":
                addSubMenuButton(subMenuPanel, "Sales Records",
                        DataIO.loadIcon(ADDCAR_PNG),null);
                addSubMenuButton(subMenuPanel, "Sales Analysis",
                        DataIO.loadIcon(DELCAR_PNG),null);
                addSubMenuButton(subMenuPanel, "Sales by Salesman",
                        DataIO.loadIcon(SEARCHCAR_PNG),null);
                addSubMenuButton(subMenuPanel, "Revenue Analysis",
                        DataIO.loadIcon(UPDATECAR_PNG),null);
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
        
        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        wrapperPanel.setBackground(Color.white);
        wrapperPanel.add(subMenuPanel);

        contentPanel.add(wrapperPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showDefaultContent(JPanel contentPanel, String staffName) {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        
        JPanel welcomePanel = new JPanel(new GridLayout(6,1));
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(200,10,100,10));
        
        JLabel welcomeText = new JLabel("Welcome "+ staffName+ "! \\(@^0^@)/",JLabel.CENTER);
        welcomeText.setFont(new Font("Arial", Font.PLAIN, 18));
        welcomeText.setForeground(new Color(150,150,150));
        
        JLabel instructionText = new JLabel("-Please select a menu option-",JLabel.CENTER);
        instructionText.setFont(new Font("Arial", Font.PLAIN, 18));
        instructionText.setForeground(new Color(150,150,150));
        
        welcomePanel.add(welcomeText);
        welcomePanel.add(instructionText);
        contentPanel.add(welcomePanel);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void styleMenuButton(JButton button) {
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(250, 50));
        button.setPreferredSize(new Dimension(250, 50));
        button.setBackground(Color.black);
        button.setForeground(Color.white)   ;
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setHorizontalAlignment(JLabel.LEFT);
        button.setFocusPainted(false);
        
        button.addActionListener(e -> {
        if (currentlySelectedButton != null) {
            currentlySelectedButton.setBackground(Color.black);
            currentlySelectedButton.setForeground(Color.white);
            }
            button.setBackground(Color.white);
            button.setForeground(Color.black);

            currentlySelectedButton = button;
        });
    }
    
    private void addSubMenuButton(JPanel panel, String text, ImageIcon icon, ActionListener action) {
        JButton button = new JButton(text);
        button.setIcon(icon);
        
        button.setVerticalTextPosition(JLabel.BOTTOM);
        button.setHorizontalTextPosition(JLabel.CENTER);
        
        button.setFont(new Font("Arial Narrow", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(160, 350));
        button.setMaximumSize(new Dimension(160, 350));
        button.setMinimumSize(new Dimension(160, 350));
        
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        
        Border btnBorder = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.black),
            BorderFactory.createEmptyBorder(100,20,100,20));
        button.setBorder(btnBorder);
        button.setIconTextGap(10); 
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setContentAreaFilled(true);
                button.setBackground(new Color(230, 240, 255));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setContentAreaFilled(false);
            }
        });
        
        if (action == null) {
            button.setEnabled(false);
        }
        button.addActionListener(action);
        panel.add(button);
    }
    
    private boolean checkExitPIN(String exitPIN) throws IOException{
        String savedExitPIN = DataIO.readFile(EXITPIN_FILE);
            if (savedExitPIN != null){
                if (savedExitPIN.equals(exitPIN)){
                    return true;
                }
            }
        return false;
    }
}