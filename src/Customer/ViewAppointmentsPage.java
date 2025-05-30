package Customer;

import utils.AppointmentManager;
import utils.FeedbackManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
                String orderId = a[0];
                JPanel card = createAppointmentCard(
                        orderId,
                        a[3], // model
                        a[4], // price
                        a[5], // due date
                        a[6].toLowerCase() // status
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

        JLabel statusLabel = new JLabel(status);
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

        // Read feedback
        String[] feedback = readFeedback(orderId);
        if (status.equals("paid")) {
            infoPanel.add(Box.createVerticalStrut(10));
            if (feedback != null) {
                int rating = Integer.parseInt(feedback[0]);
                String comment = feedback[1];
                JLabel ratedLabel = new JLabel("★".repeat(rating) + "☆".repeat(5 - rating));
                ratedLabel.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 14));
                ratedLabel.setForeground(Color.ORANGE);
                infoPanel.add(ratedLabel);

                if (!comment.isEmpty()) {
                    JButton commentButton = new JButton("View Comment");
                    commentButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    commentButton.setBackground(new Color(230, 230, 250));
                    commentButton.setForeground(new Color(60, 60, 80));
                    commentButton.setFocusPainted(false);
                    commentButton.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 220)));
                    commentButton.addActionListener(e -> {
                        JOptionPane.showMessageDialog(this,
                                "Your Comment:\n" + comment,
                                "Feedback for Order " + orderId,
                                JOptionPane.INFORMATION_MESSAGE);
                    });
                    infoPanel.add(Box.createVerticalStrut(5));
                    infoPanel.add(commentButton);
                }
            } else {
                JButton feedbackButton = new JButton("Leave Feedback");
                feedbackButton.setFocusPainted(false);
                feedbackButton.setBackground(new Color(0x3465A4));
                feedbackButton.setForeground(Color.WHITE);
                feedbackButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                feedbackButton.setAlignmentX(Component.LEFT_ALIGNMENT);
                feedbackButton.addActionListener(e -> {
                    new FeedbackPage(customer, orderId).setVisible(true);
                });
                infoPanel.add(feedbackButton);
            }
        }

        // Status Panel
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setOpaque(false);
        statusPanel.add(statusLabel, BorderLayout.NORTH);

        card.add(infoPanel, BorderLayout.CENTER);
        card.add(statusPanel, BorderLayout.EAST);

        return card;
    }

    private String[] readFeedback(String orderId) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("data/feedback.txt"));
            for (String line : lines) {
                String[] parts = line.split(",", 4);
                if (parts.length == 4 && parts[0].equals(orderId) && parts[1].equals(customer.getId())) {
                    return new String[]{parts[2], parts[3]};
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
