package Customer;

import javax.swing.*;
import java.awt.*;
import navigation.FrameManager;

public class PaymentMethodPage extends JFrame {
    private final Customer customer;

    public PaymentMethodPage(Customer customer) {
        this.customer = customer;

        setTitle("Payment Method");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Use vertical layout
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Select Your Payment Method");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        String[] methods = {"Visa", "Mastercard", "Online Banking", "E-Wallet"};
        JComboBox<String> paymentComboBox = new JComboBox<>(methods);
        paymentComboBox.setMaximumSize(new Dimension(250, 30));
        paymentComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        paymentComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton saveButton = new JButton("Save");
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveButton.setBackground(new Color(0x2A74FF));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveButton.setMaximumSize(new Dimension(150, 35));
        saveButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        saveButton.addActionListener(e -> {
            String selectedMethod = (String) paymentComboBox.getSelectedItem();
            customer.setPaymentMethod(selectedMethod);  // Save in Customer object
            CustomerManager.saveToFile(); // Persist to file
            JOptionPane.showMessageDialog(this, "Payment Method Set: " + selectedMethod);
            FrameManager.showFrame(new CustomerMainPage(customer));
            this.dispose();
        });

        mainPanel.add(titleLabel);
        mainPanel.add(paymentComboBox);
        mainPanel.add(Box.createVerticalStrut(30));
        mainPanel.add(saveButton);

        add(mainPanel);
        setVisible(true);
    }
}