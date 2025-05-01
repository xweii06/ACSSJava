package Customer;

import javax.swing.*;
import navigation.FrameManager;

public class FeedbackPage extends JFrame {
    private Customer customer;

    public FeedbackPage(Customer customer) {
        this.customer = customer;

        setTitle("Purchase Feedback");
        setSize(400, 350);
        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel feedbackLabel = new JLabel("Write your feedback:");
        feedbackLabel.setBounds(100, 30, 200, 30);
        add(feedbackLabel);

        JTextArea feedbackArea = new JTextArea();
        feedbackArea.setBounds(50, 70, 300, 150);
        add(feedbackArea);

        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(140, 240, 120, 30);
        add(submitButton);

        submitButton.addActionListener(e -> {
            String feedback = feedbackArea.getText().trim();
            if (!feedback.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Thank you for your feedback!");
            } else {
                JOptionPane.showMessageDialog(this, "Feedback cannot be empty.");
            }
            FrameManager.showFrame(new CustomerMainPage(customer));
        });
    }
}
