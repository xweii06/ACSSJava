package Staff;

import Staff.CarManagement.CarPanel;
import Staff.CarManagement.CarService;
import Staff.CustomerManagement.CustomerPanel;
import Staff.CustomerManagement.CustomerService;
import Staff.SaleManagement.AnalysisPanel;
import Staff.SalesmanManagement.SalesmanPanel;
import Staff.SalesmanManagement.SalesmanService;
import Staff.StaffManagement.StaffPanel;
import Staff.StaffManagement.StaffService;
import Staff.SaleManagement.AppointmentPanel;
import Staff.SaleManagement.FeedbackPanel;
import Staff.SaleManagement.ReportsPanel;
import Staff.SaleManagement.SalePanel;
import navigation.FrameManager;
import repositories.AppointmentRepository;
import repositories.CarRepository;
import repositories.CustomerRepository;
import repositories.SalesmanRepository;
import repositories.StaffRepository;
import repositories.SaleRepository;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.Border;

import utils.DataIO;

public class StaffMenu extends JFrame {
    
    private StaffService staffService;
    private SalesmanService salesmanService;
    private CustomerService customerService;
    private CarService carService;
    
    private Staff currentStaff;
    private static final String EXITPIN_FILE = "exitPIN.txt";
    private static final String PAYMENT_PNG = "payment.png", PENDING_PNG = "pending.png", 
            FEEDBACK_PNG = "feedback.png", ANALYSIS_PNG = "analysis.png";
    
    private JPanel mainPanel, sidebar, contentPanel, titlePanel, subMenuPanel;
    private JLabel pageTitle, titleLabel;
    private JTable recordsTable;
    private JButton currentlySelectedButton = null; 
    
    public StaffMenu(Staff staff, StaffRepository staffRepo, SalesmanRepository salesmanRepo,
                     CustomerRepository customerRepo, CarRepository carRepo) {
        this.staffService = new StaffService(staffRepo);
        this.salesmanService = new SalesmanService(salesmanRepo);
        this.customerService = new CustomerService(customerRepo);
        this.carService = new CarService(carRepo);
        
        this.currentStaff = staff;
        this.setTitle("Staff Menu");
        this.setSize(1000, 600);
        this.setLocationRelativeTo(null);
        
        mainPanel = new JPanel(new BorderLayout());
        
        JPanel sidebarContent = createSidebar();
        JScrollPane sidebarScroll = new JScrollPane(sidebarContent);
        sidebarScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sidebarScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        sidebarScroll.setBorder(null);
        mainPanel.add(sidebarScroll, BorderLayout.WEST);
        
        contentPanel = new JPanel();
        contentPanel.setBackground(Color.white);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        this.add(mainPanel);
        showDefaultContent(contentPanel,staff.getName());
        this.setVisible(true);
    }
    
    private JPanel createSidebar() {
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
        if (currentStaff.isSuperAdmin()) {
            menuItems.add("Staff Management"); 
        } else {
            menuItems.add("Salesman Management");
            menuItems.add("Customer Management"); 
            menuItems.add("Car Management");
            menuItems.add("Payment & Feedback");
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
            currentStaff = null;
            Window[] windows = Window.getWindows();
            for (Window window : windows) {
                if (window != this) {
                    window.dispose();
                }
            }
            FrameManager.goBack();
        });
        sidebar.add(logoutBtn);
        return sidebar;
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
    
    public void switchContent(String menuItem) {
        if (menuItem.equals("End Program")) {
            handleExitProgram();
            return;
        }
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        
        titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));
        titlePanel.setBackground(Color.lightGray);

        titleLabel = new JLabel(menuItem);
        titleLabel.setFont(new Font("MV Boli", Font.BOLD, 30));
        titleLabel.setForeground(Color.black);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        titlePanel.add(titleLabel, BorderLayout.CENTER);
        contentPanel.add(titlePanel, BorderLayout.NORTH);
        
        subMenuPanel = new JPanel();
        subMenuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        subMenuPanel.setBackground(Color.white);
        if (menuItem.equals("Payment & Feedback") || menuItem.equals("Reports")) {
            subMenuPanel.setLayout(new GridLayout(0, 4, 15, 15));
        } 
        
        switch(menuItem) {
            case "Staff Management":
                contentPanel.add(new StaffPanel(staffService));
                break;
            case "Salesman Management":
                contentPanel.add(new SalesmanPanel(salesmanService));
                break;
            case "Car Management":
                contentPanel.add(new CarPanel(carService));
                break;
            case "Customer Management":
                contentPanel.add(new CustomerPanel(customerService));
                break;
            case "Payment & Feedback":
                addSubMenuButton(subMenuPanel, "Booking Records",
                        DataIO.loadIcon(PENDING_PNG),
                        e -> showPanelInFrame(
                                "Booking Records", 
                                new AppointmentPanel(new AppointmentRepository())
                        ));
                addSubMenuButton(subMenuPanel, "Payment Records",
                        DataIO.loadIcon(PAYMENT_PNG), 
                        e -> showPanelInFrame(
                                "Payment Records", 
                                new SalePanel(new SaleRepository())
                        ));
                addSubMenuButton(subMenuPanel, "All Feedbacks",
                        DataIO.loadIcon(FEEDBACK_PNG),
                        e -> showPanelInFrame(
                                "All Feedbacks", 
                                new FeedbackPanel())
                        );
                addSubMenuButton(subMenuPanel, "Analysis",
                        DataIO.loadIcon(ANALYSIS_PNG),
                        e -> showPanelInFrame(
                                "Analysis", 
                                new AnalysisPanel())
                        );
                contentPanel.add(subMenuPanel);
                break;
            case "Reports":
                contentPanel.add(new ReportsPanel(), BorderLayout.CENTER);
                break;
        }
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
        
        button.setFont(new Font("Arial Narrow", Font.BOLD, 12));
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
    
    private void handleExitProgram() {
        String exitPIN = JOptionPane.showInputDialog("Enter Exit PIN:");
        if (exitPIN != null){
            try {
                if (checkExitPIN(exitPIN)) {
                    int confirmation= JOptionPane.showConfirmDialog(this,"Are you sure?",
                                    "Confirmation",JOptionPane.YES_NO_OPTION);
                    if (confirmation == 0) {
                        System.exit(0);
                    }
                } else {
                    JOptionPane.showMessageDialog(this,"Invalid PIN.",
                            "Error",JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                    "Error accessing records",
                    "System Error",
                    JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
    
    private boolean checkExitPIN(String exitPIN) throws IOException{
        String savedExitPIN = DataIO.readFile(EXITPIN_FILE);
            if (savedExitPIN != null){
                if (savedExitPIN.trim().equals(exitPIN)){
                    return true;
                }
            }
        return false;
    }
    
    private void showPanelInFrame(String title, JPanel panel) {
        JFrame frame = new JFrame(title);
        frame.setLayout(new BorderLayout());
        frame.setBackground(Color.white);
        frame.setPreferredSize(new Dimension(800, 600));
        frame.add(panel, BorderLayout.CENTER);
        frame.pack();
        FrameManager.showFrame(frame);
    }
}