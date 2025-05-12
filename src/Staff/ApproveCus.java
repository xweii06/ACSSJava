package Staff;

import java.awt.*;
import java.io.*;
import javax.swing.*;
import navigation.FrameManager;
import utils.DataIO;

public class ApproveCus {
    
    private static final String CUSTOMER_FILE = "customers.txt", 
            PENDING_CUS_FILE = "pendingCustomers.txt";
    
    private static JFrame frame;
    private static JPanel topPanel, btnPanel, contentPanel;
    private static JButton backBtn, refreshBtn;
    
    public static void handlePendingCus() {
        if (!hasPendingCustomers()) {
            JOptionPane.showMessageDialog(frame, 
                    "No pending customers currently.",
                    "No requests", JOptionPane.INFORMATION_MESSAGE);
        } else {
            FrameManager.showFrame(viewPendingCus());
        }
    }
    
    private static boolean hasPendingCustomers() {
        try {
            String pendingCustomers = DataIO.readFile(PENDING_CUS_FILE);
            
            if (pendingCustomers == null || pendingCustomers.isEmpty() || 
                    !pendingCustomers.contains(",")) {
                return false;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, 
                    "Error loading pending customers: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    
    private static JFrame viewPendingCus() {
        frame = new JFrame("Pending Customers");
        frame.setLayout(new BorderLayout());
        frame.setSize(800,400);
        frame.setResizable(false);
        
        topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.lightGray);
        
        btnPanel = new JPanel(new BorderLayout());
        btnPanel.setPreferredSize(new Dimension(130,70));
        btnPanel.setOpaque(false);
        
        backBtn = new JButton(DataIO.loadIcon("backIcon.png"));
        backBtn.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 5));
        backBtn.setOpaque(false);
        backBtn.setContentAreaFilled(false);
        backBtn.addActionListener(e -> FrameManager.goBack());
        
        refreshBtn = new JButton(DataIO.loadIcon("refreshIcon.png"));
        refreshBtn.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 10));
        refreshBtn.setOpaque(false);
        refreshBtn.setContentAreaFilled(false);
        refreshBtn.addActionListener(e -> refreshSuccessfully());
        
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
        
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        
        refreshPendingCustomers();
        
        frame.setVisible(true);
        return frame;
    }
    
    private static void refreshPendingCustomers() {
        contentPanel.removeAll();
        int number = 0;
        try {
            String data = DataIO.readFile(PENDING_CUS_FILE);
            String[] lines = data.split("\n");
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    String[] details = line.split(",");
                    number++;
                    if (details.length >= 4) {
                        addCustomerCard(number,details[0],details[1],details[2],details[3],details[4]);
                    }
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, 
                    "Error loading pending customers: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private static void refreshSuccessfully() {
        refreshPendingCustomers();
        JOptionPane.showMessageDialog(frame, "Refresh completed.",
                "Page Refreshed", JOptionPane.INFORMATION_MESSAGE);
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
        int confirm = JOptionPane.showConfirmDialog(frame,
             "Are you sure you want to approve customer [" + id + "]?",
            "Confirm Approval", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String customerData = findCustomerData(id);
                if (customerData != null) {
                    DataIO.appendToFile(CUSTOMER_FILE, customerData);
                    removeFromPendingFile(id);
                    JOptionPane.showMessageDialog(frame,
                        "Customer approved successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    if (!hasPendingCustomers()) {
                        FrameManager.goBack();
                    } else {
                        refreshPendingCustomers();
                    }
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame,
                    "Error approving customer: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private static void handleReject(String id) {
        int confirm = JOptionPane.showConfirmDialog(frame,
            "Are you sure you want to reject this customer [" + id + "]?",
            "Confirm Rejection", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                removeFromPendingFile(id);
                JOptionPane.showMessageDialog(frame,
                    "Customer rejected successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshPendingCustomers();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame,
                    "Error rejecting customer: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private static String findCustomerData(String id) throws IOException {
        String data = DataIO.readFile(PENDING_CUS_FILE);
        if (data == null) return null;
        
        String[] lines = data.split("\n");
        for (String line : lines) {
            if (line.startsWith(id + ",")) {
                return line;
            }
        }
        return null;
    }
    
    private static void removeFromPendingFile(String id) throws IOException {
        String filename = PENDING_CUS_FILE;
        StringBuilder newContent = new StringBuilder();
        boolean found = false;

        String data = DataIO.readFile(filename);
        if (data == null || data.isEmpty()) {
            return;
        }

        String[] lines = data.split("\n");
        for (String line : lines) {
            if (!line.trim().isEmpty() && !line.startsWith(id + ",")) {
                newContent.append(line).append("\n");
            } else if (line.startsWith(id + ",")) {
                found = true;
            }
        }

        if (found) {
            DataIO.writeFile(filename, newContent.toString().trim());
        }
    }
}