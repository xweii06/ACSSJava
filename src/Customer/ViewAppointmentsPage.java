package Customer;

import utils.AppointmentManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class ViewAppointmentsPage extends JFrame {
    private final Customer customer;

    public ViewAppointmentsPage(Customer customer) {
        this.customer = customer;

        setTitle("My Appointments / Orders");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("My Appointments & Orders", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setBorder(new EmptyBorder(20, 10, 10, 10));
        add(titleLabel, BorderLayout.NORTH);

        // Card Container
        JPanel cardContainer = new JPanel();
        cardContainer.setLayout(new BoxLayout(cardContainer, BoxLayout.Y_AXIS));
        cardContainer.setBackground(Color.WHITE);
        cardContainer.setBorder(new EmptyBorder(10, 20, 10, 20));

        // Fetch appointments
        AppointmentManager.updateOverdueStatus();
        List<String[]> appointments = AppointmentManager.readAppointments(customer.getId());

        if (appointments.isEmpty()) {
            JLabel emptyLabel = new JLabel("No appointments found.", JLabel.CENTER);
            emptyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            cardContainer.add(emptyLabel);
        } else {
            for (String[] a : appointments) {
                JPanel card = createAppointmentCard(
                        a[2], // model
                        a[3], // year
                        a[4], // price
                        a[5], // due date
                        a[6], // status
                        a[7]  // order ID
                );
                cardContainer.add(card);
                cardContainer.add(Box.createVerticalStrut(15));
            }
        }

        // Scroll Pane
        JScrollPane scrollPane = new JScrollPane(cardContainer);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        // Back button (for embedded usage)
        JButton backBtn = new JButton("Back");
        backBtn.setPreferredSize(new Dimension(80, 35));
        backBtn.setFocusPainted(false);
        backBtn.setBackground(new Color(90, 0, 160));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> dispose());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        bottomPanel.add(backBtn);

        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createAppointmentCard(String model, String year, String price, String dueDate, String status, String orderId) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(new Color(245, 245, 255));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 255), 1),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel title = new JLabel(model + " (" + year + ")");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JLabel priceLabel = new JLabel("Price: RM " + price);
        priceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel dueLabel = new JLabel("Due Date: " + dueDate);
        dueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel orderIdLabel = new JLabel("Order ID: " + orderId);
        orderIdLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        orderIdLabel.setForeground(Color.GRAY);

        JLabel statusLabel = new JLabel(status.toUpperCase());
        statusLabel.setOpaque(true);
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setHorizontalAlignment(JLabel.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        statusLabel.setPreferredSize(new Dimension(100, 30));

        switch (status.toLowerCase()) {
            case "completed":
                statusLabel.setBackground(new Color(0, 170, 70));
                break;
            case "pending":
                statusLabel.setBackground(new Color(255, 140, 0));
                break;
            case "overdue":
                statusLabel.setBackground(new Color(200, 0, 0));
                break;
            default:
                statusLabel.setBackground(Color.GRAY);
        }

        // Left panel with info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        infoPanel.add(title);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(priceLabel);
        infoPanel.add(dueLabel);
        infoPanel.add(orderIdLabel);

        // Right panel with status
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setOpaque(false);
        statusPanel.add(statusLabel, BorderLayout.NORTH);

        card.add(infoPanel, BorderLayout.CENTER);
        card.add(statusPanel, BorderLayout.EAST);

        return card;
    }
}
