package Customer;

import utils.AppointmentManager;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class BookAppointmentPage extends JFrame {
    private Customer customer;
    private String[] car; // car[0]=id, [1]=model, [2]=year, [3]=price

    public BookAppointmentPage(Customer customer, String[] car) {
        this.customer = customer;
        this.car = car;

        setTitle("Book Car - " + car[1]);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel carLabel = new JLabel("Car: " + car[1] + " (" + car[2] + ")");
        JLabel priceLabel = new JLabel("Price: " + car[3]);

        JTextField dateField = new JTextField(LocalDate.now().plusDays(3).toString());
        dateField.setEditable(false);

        JButton confirmBtn = new JButton("Confirm Booking");

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Selected Car:"), gbc);
        gbc.gridx = 1;
        formPanel.add(carLabel, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Price:"), gbc);
        gbc.gridx = 1;
        formPanel.add(priceLabel, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Pickup Due Date:"), gbc);
        gbc.gridx = 1;
        formPanel.add(dateField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        formPanel.add(confirmBtn, gbc);
        
        confirmBtn.addActionListener(e -> {
            String orderId = UUID.randomUUID().toString().substring(0, 8);
            String dueDate = dateField.getText();

            AppointmentManager.saveAppointment(
                orderId, customer.getId(), car[0], car[1], car[3], dueDate
            );

            JOptionPane.showMessageDialog(this,
                    "Booking confirmed! Order ID: " + orderId + "\nPlease pay before: " + dueDate);
            dispose();
        });

        add(formPanel, BorderLayout.CENTER);
        setVisible(true);
    }
}
