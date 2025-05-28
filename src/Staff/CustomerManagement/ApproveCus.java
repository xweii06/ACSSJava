package Staff.CustomerManagement;

import repositories.PendingCustomerRepository;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import javax.swing.*;
import navigation.FrameManager;
import utils.DataIO;

public class ApproveCus {
    private JFrame frame;
    private JPanel contentPanel;
    private PendingCustomerRepository repository;

    public ApproveCus() {
        this.repository = new PendingCustomerRepository();
    }

    public void handlePendingCus() {
        try {
            if (!repository.hasPendingCustomers()) {
                JOptionPane.showMessageDialog(frame,
                        "No pending customers currently.",
                        "No requests", JOptionPane.INFORMATION_MESSAGE);
            } else {
                FrameManager.showFrame(createApproveCusPage());
            }
        } catch (IOException ex) {
            showError("Error checking pending customers: " + ex.getMessage());
        }
    }

    private JFrame createApproveCusPage() {
        frame = new JFrame("Pending Customers");
        frame.setLayout(new BorderLayout());
        frame.setSize(800, 400);
        frame.setResizable(false);

        JPanel topPanel = createTopPanel();
        contentPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 23, 10, 23));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        refreshPendingCustomers();

        frame.setVisible(true);
        return frame;
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.lightGray);

        JPanel btnPanel = new JPanel(new BorderLayout());
        btnPanel.setPreferredSize(new Dimension(130, 70));
        btnPanel.setOpaque(false);

        JButton backBtn = new JButton(DataIO.loadIcon("backIcon.png"));
        backBtn.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 5));
        backBtn.setOpaque(false);
        backBtn.setContentAreaFilled(false);
        backBtn.addActionListener(e -> FrameManager.goBack());

        JButton refreshBtn = new JButton(DataIO.loadIcon("refreshIcon.png"));
        refreshBtn.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 10));
        refreshBtn.setOpaque(false);
        refreshBtn.setContentAreaFilled(false);
        refreshBtn.addActionListener(e -> refreshSuccessfully());

        btnPanel.add(backBtn, BorderLayout.WEST);
        btnPanel.add(refreshBtn, BorderLayout.EAST);
        topPanel.add(btnPanel, BorderLayout.WEST);

        return topPanel;
    }

    private void refreshPendingCustomers() {
        contentPanel.removeAll();
        try {
            List<PendingCustomer> customers = repository.getAllPendingCustomers();
            if (customers.isEmpty()) {
                FrameManager.goBack();
                return;
            }

            int number = 0;
            for (PendingCustomer customer : customers) {
                number++;
                PendingCustomerCard card = new PendingCustomerCard(customer, number, this::refreshPendingCustomers);
                contentPanel.add(card);
            }
        } catch (IOException ex) {
            showError("Error loading pending customers: " + ex.getMessage());
        }
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void refreshSuccessfully() {
        refreshPendingCustomers();
        JOptionPane.showMessageDialog(frame, "Refresh completed.",
                "Page Refreshed", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(frame, message,
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}