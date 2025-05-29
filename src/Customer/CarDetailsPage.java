package Customer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CarDetailsPage extends JFrame {

    public CarDetailsPage(String[] carData) {
        setTitle("Car Details");
        setSize(450, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(20, 30, 20, 30));
        content.setBackground(Color.WHITE);

        // Car Image
        JLabel imageLabel = new JLabel(new ImageIcon("caricon.png"));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(imageLabel);

        content.add(Box.createVerticalStrut(20));

        // Car Info Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(0, 1, 5, 10));
        infoPanel.setOpaque(false);

        JLabel modelLabel = new JLabel("Model: " + carData[1]);
        JLabel yearLabel = new JLabel("Year: " + carData[2]);
        JLabel priceLabel = new JLabel("Price: " + carData[3]);
        JLabel idLabel = new JLabel("Car ID: " + carData[0]);
        JLabel statusLabel = new JLabel("Status: Available");

        JLabel[] labels = {modelLabel, yearLabel, priceLabel, idLabel, statusLabel};
        for (JLabel lbl : labels) {
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            infoPanel.add(lbl);
        }

        content.add(infoPanel);

        content.add(Box.createVerticalStrut(30));

        // Book Now Button
        JButton bookBtn = new JButton("Book Now");
        bookBtn.setFocusPainted(false);
        bookBtn.setBackground(new Color(0x2A74FF));
        bookBtn.setForeground(Color.WHITE);
        bookBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        bookBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        bookBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        bookBtn.setMaximumSize(new Dimension(180, 40));

        bookBtn.addActionListener(e -> {
            dispose(); // Close current
            new BookAppointmentPage(null, carData); // You can pass the customer if needed
        });

        content.add(bookBtn);
        content.add(Box.createVerticalStrut(15));

        // Back Button
        JButton backBtn = new JButton("Back");
        backBtn.setFocusPainted(false);
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        backBtn.setBackground(Color.GRAY);
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        backBtn.setMaximumSize(new Dimension(150, 35));
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> dispose());

        content.add(backBtn);

        add(content, BorderLayout.CENTER);
        setVisible(true);
    }
}
