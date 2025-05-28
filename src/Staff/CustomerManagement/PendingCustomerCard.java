package Staff.CustomerManagement;

import repositories.PendingCustomerRepository;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class PendingCustomerCard extends JPanel {
    private final PendingCustomer customer;
    private final int requestNumber;
    private Runnable refreshCallback;

    public PendingCustomerCard(PendingCustomer customer, int requestNumber, Runnable refreshCallback) {
        this.customer = customer;
        this.requestNumber = requestNumber;
        this.refreshCallback = refreshCallback;
        initializeUI();
    }

    private void initializeUI() {
        this.setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.gray, 1));
        setBackground(Color.white);
        setPreferredSize(new Dimension(220, 260));

        JPanel customerPanel = new JPanel();
        customerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        customerPanel.setLayout(new BoxLayout(customerPanel, BoxLayout.Y_AXIS));
        customerPanel.setOpaque(false);

        JLabel numbering = new JLabel("Request #" + requestNumber);
        numbering.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel idLabel = new JLabel("ID: " + customer.getId());
        idLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel usernameLabel = new JLabel("Username: " + customer.getName());
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel nameLabel = new JLabel("Name: " + customer.getName());
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel phoneLabel = new JLabel("Phone: " + customer.getPhone());
        phoneLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel emailLabel = new JLabel("Email: " + customer.getEmail());
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton rejectBtn = new JButton("Reject");
        rejectBtn.setBackground(new Color(0xE31A3E));
        rejectBtn.setForeground(Color.white);
        rejectBtn.setPreferredSize(new Dimension(100, 25));

        JButton approveBtn = new JButton("Approve");
        approveBtn.setBackground(new Color(0x08A045));
        approveBtn.setForeground(Color.white);
        approveBtn.setPreferredSize(new Dimension(100, 25));

        approveBtn.addActionListener(e -> handleApprove());
        rejectBtn.addActionListener(e -> handleReject());

        customerPanel.add(numbering);
        customerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        customerPanel.add(idLabel);
        customerPanel.add(usernameLabel);
        customerPanel.add(nameLabel);
        customerPanel.add(phoneLabel);
        customerPanel.add(emailLabel);
        customerPanel.add(Box.createVerticalGlue());
        customerPanel.add(rejectBtn);
        customerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        customerPanel.add(approveBtn);

        add(customerPanel, BorderLayout.CENTER);
    }

    private void handleApprove() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to approve customer [" + customer.getId() + "]?",
                "Confirm Approval", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                PendingCustomerRepository repo = new PendingCustomerRepository();
                repo.approveCustomer(customer);
                JOptionPane.showMessageDialog(this,
                        "Customer approved successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshCallback.run();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error approving customer: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleReject() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to reject this customer [" + customer.getId() + "]?",
                "Confirm Rejection", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                PendingCustomerRepository repo = new PendingCustomerRepository();
                repo.rejectCustomer(customer.getId());
                JOptionPane.showMessageDialog(this,
                        "Customer rejected successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshCallback.run();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error rejecting customer: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}