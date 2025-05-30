package Customer;

import utils.AppointmentManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class ViewAppointmentsPage extends JPanel {
    private final Customer customer;

    public ViewAppointmentsPage(Customer customer) {
        this.customer = customer;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

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
                // a = {orderId, customerId, carId, model, price, dueDate, status}
                JPanel card = createAppointmentCard(
                        a[0], // order ID
                        a[3], // model
                        a[4], // price
                        a[5], // due date
                        a[6].toLowerCase() // status (force lowercase)
                );
                cardContainer.add(card);
                cardContainer.add(Box.createVerticalStrut(15));
            }
        }

        // Scroll Pane
        JScrollPane scrollPane = new JScrollPane(cardContainer);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createAppointmentCard(String orderId, String model, String price, String dueDate, String status) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(245, 245, 255));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 255), 1),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel title = new JLabel(model);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JLabel priceLabel = new JLabel("Price: " + price);
        priceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel dueLabel = new JLabel("Due Date: " + dueDate);
        dueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel orderIdLabel = new JLabel("Order ID: " + orderId);
        orderIdLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        orderIdLabel.setForeground(Color.GRAY);

        JLabel statusLabel = new JLabel(status); // now shows lowercase
        statusLabel.setOpaque(true);
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setHorizontalAlignment(JLabel.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        statusLabel.setPreferredSize(new Dimension(100, 30));

        switch (status) {
            case "paid" -> statusLabel.setBackground(new Color(0, 170, 70));
            case "pending" -> statusLabel.setBackground(new Color(255, 140, 0));
            case "cancelled" -> statusLabel.setBackground(new Color(200, 0, 0));
            default -> statusLabel.setBackground(Color.GRAY);
        }

        // Info Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        infoPanel.add(title);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(priceLabel);
        infoPanel.add(dueLabel);
        infoPanel.add(orderIdLabel);

        // Status Panel
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setOpaque(false);
        statusPanel.add(statusLabel, BorderLayout.NORTH);

        card.add(infoPanel, BorderLayout.CENTER);
        card.add(statusPanel, BorderLayout.EAST);

        return card;
    }
}
