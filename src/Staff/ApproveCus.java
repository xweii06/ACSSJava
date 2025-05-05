package Staff;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.*;
import java.util.List;
import navigation.FrameManager;
import utils.DataIO;

public class ApproveCus {
    
    private static String CUSTOMER_FILE = "customers.txt", PENDING_CUS_FILE = "pendingCustomers.txt";
    
    private static JFrame pendingCusFrame;
    private static JPanel topPanel, btnPanel, contentPanel;
    private static JButton backBtn, refreshBtn;
    
    public static ActionListener approveCus() {
        return e -> handlePendingCustomers();
    }
    
    private static void handlePendingCustomers() {
        if (!checkPendingCustomers()) {
            JOptionPane.showMessageDialog(pendingCusFrame, 
                    "No pending customers currently.",
                    "No requests", JOptionPane.INFORMATION_MESSAGE);
        } else {
            FrameManager.showFrame(viewPendingCus());
        }
    }
    
    private static boolean checkPendingCustomers() {
        
        try {
            String pendingCustomers = DataIO.readFile(PENDING_CUS_FILE);
            
            if (pendingCustomers == null || pendingCustomers.isEmpty() || 
                    !pendingCustomers.contains(",")) {
                return false;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(pendingCusFrame, 
                    "Error loading pending customers: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    
    private static void refreshPendingCustomers() {
        
        contentPanel.removeAll();
        int number = 0;
        try {
            String pendingCustomers = DataIO.readFile(PENDING_CUS_FILE);
            String[] customersData = pendingCustomers.split("\n");
            for (String customerData : customersData) {
                if (!customerData.trim().isEmpty()) {
                    String[] details = customerData.split(",");
                    number++;
                    if (details.length >= 4) {
                        addCustomerCard(number,details[0],details[1],details[2],details[3],details[4]);
                    }
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(pendingCusFrame, 
                    "Error loading pending customers: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private static JFrame viewPendingCus() {
        
        pendingCusFrame = new JFrame("Pending Customers");
        pendingCusFrame.setLayout(new BorderLayout());
        pendingCusFrame.setSize(800,400);
        
        topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.lightGray);
        
        btnPanel = new JPanel(new BorderLayout());
        btnPanel.setPreferredSize(new Dimension(200,80));
        btnPanel.setOpaque(false);
        
        backBtn = new JButton(DataIO.loadIcon("backIcon.png"));
        backBtn.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        backBtn.setOpaque(false);
        backBtn.setContentAreaFilled(false);
        backBtn.addActionListener(e -> FrameManager.goBack());
        
        refreshBtn = new JButton(DataIO.loadIcon("refreshIcon.png"));
        refreshBtn.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        refreshBtn.setOpaque(false);
        refreshBtn.setContentAreaFilled(false);
        refreshBtn.addActionListener(e -> refreshPendingCustomers());
        
        btnPanel.add(backBtn,BorderLayout.WEST);
        btnPanel.add(refreshBtn,BorderLayout.EAST);
        topPanel.add(btnPanel,BorderLayout.WEST);
        
        contentPanel = new JPanel(new GridLayout(0,3,10,10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10,23,10,23));
        
        JPanel wrapperPanel = new JPanel(new BorderLayout());        
        wrapperPanel.setPreferredSize(new Dimension(900,contentPanel.getHeight()));
        wrapperPanel.setMaximumSize(new Dimension(900,contentPanel.getHeight()));
        
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        pendingCusFrame.add(topPanel, BorderLayout.NORTH);
        pendingCusFrame.add(scrollPane, BorderLayout.CENTER);
        
        refreshPendingCustomers();
        
        pendingCusFrame.setVisible(true);
        return pendingCusFrame;
    }
    
    private static void addCustomerCard(int number, String id, String username, String name, 
            String phone, String email) {
        
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setBorder(BorderFactory.createLineBorder(Color.gray,1));
        cardPanel.setBackground(Color.white);
        cardPanel.setPreferredSize(new Dimension(220,260));
        
        JPanel customerPanel = new JPanel();
        customerPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        customerPanel.setLayout(new BoxLayout(customerPanel, BoxLayout.Y_AXIS));
        customerPanel.setOpaque(false);
        
        JLabel numbering = new JLabel("Request #" + number);
        numbering.setFont(new Font("Arial", Font.BOLD, 18));
        
        JLabel idLabel = new JLabel("ID: " + id);
        idLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JLabel usernameLabel = new JLabel("Username: " + username);
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JLabel nameLabel = new JLabel("Name: " + name);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JLabel phoneLabel = new JLabel("Phone: " + phone);
        phoneLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JLabel emailLabel = new JLabel("Email: " + email);
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JButton rejectBtn = new JButton("Reject");
        rejectBtn.setBackground(new Color(0xE31A3E));
        rejectBtn.setForeground(Color.white);
        rejectBtn.setSize(new Dimension(100,25));
        
        JButton approveBtn = new JButton("Approve");
        approveBtn.setBackground(new Color(0x08A045));
        approveBtn.setForeground(Color.white);
        approveBtn.setSize(new Dimension(100,25));
        
        approveBtn.addActionListener(e -> handleApprove(id));
        rejectBtn.addActionListener(e -> handleReject(id));
        
        customerPanel.add(numbering);
        customerPanel.add(Box.createRigidArea(new Dimension(0,10)));
        customerPanel.add(idLabel);
        customerPanel.add(usernameLabel);
        customerPanel.add(nameLabel);
        customerPanel.add(phoneLabel);
        customerPanel.add(emailLabel);
        
        customerPanel.add(Box.createVerticalGlue()); 
        customerPanel.add(rejectBtn);
        customerPanel.add(Box.createRigidArea(new Dimension(0,10)));
        customerPanel.add(approveBtn);
        
        cardPanel.add(customerPanel, BorderLayout.CENTER);
        contentPanel.add(cardPanel);
    }
    
    private static void handleApprove(String id) {
        
        int confirm = JOptionPane.showConfirmDialog(pendingCusFrame,
             "Are you sure you want to approve customer [" + id + "]?",
            "Confirm Approval", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String customerData = findCustomerData(id);
                if (customerData != null) {
                    DataIO.appendToFile(CUSTOMER_FILE, customerData);
                    removeFromPendingFile(id);
                    JOptionPane.showMessageDialog(pendingCusFrame,
                        "Customer approved successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshPendingCustomers();
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(pendingCusFrame,
                    "Error approving customer: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private static void handleReject(String id) {
        
        int confirm = JOptionPane.showConfirmDialog(pendingCusFrame,
            "Are you sure you want to reject this customer [" + id + "]?",
            "Confirm Rejection", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                removeFromPendingFile(id);
                JOptionPane.showMessageDialog(pendingCusFrame,
                    "Customer rejected successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshPendingCustomers();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(pendingCusFrame,
                    "Error rejecting customer: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private static String findCustomerData(String customerId) throws IOException {
        Path path = Paths.get("data", PENDING_CUS_FILE);
        if (Files.exists(path)) {
            for (String line : Files.readAllLines(path)) {
                if (line.startsWith(customerId + ",")) {
                    return line;
                }
            }
        }
        return null;
    }
    
    private static void removeFromPendingFile(String customerId) throws IOException {
        Path path = Paths.get("data", PENDING_CUS_FILE);
        if (Files.exists(path)) {
            List<String> lines = Files.readAllLines(path);
            lines.removeIf(line -> line.startsWith(customerId + ","));
            Files.write(path, lines);
        }
    }
}