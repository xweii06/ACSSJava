package Customer;

import javax.swing.*;
import navigation.FrameManager;

public class PaymentMethodPage extends JFrame {
    private Customer customer;

    public PaymentMethodPage(Customer customer) {
        this.customer = customer;

        setTitle("Payment Method Setting");
        setSize(400, 300);
        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel paymentLabel = new JLabel("Choose Payment Method:");
        paymentLabel.setBounds(100, 30, 200, 30);
        add(paymentLabel);

        String[] methods = {"Visa", "Mastercard", "Online Banking", "E-Wallet"};
        JComboBox<String> paymentComboBox = new JComboBox<>(methods);
        paymentComboBox.setBounds(100, 80, 200, 30);
        add(paymentComboBox);

        JButton saveButton = new JButton("Save");
        saveButton.setBounds(140, 150, 120, 30);
        add(saveButton);

        saveButton.addActionListener(e -> {
            String selectedMethod = (String) paymentComboBox.getSelectedItem();
            JOptionPane.showMessageDialog(this, "Payment Method Set: " + selectedMethod);
            FrameManager.showFrame(new CustomerMainPage(customer));
        });
    }
}
