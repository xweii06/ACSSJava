package Customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import navigation.FrameManager;

public class FeedbackPage extends JFrame {
    private Customer customer;
    private String orderId;

    public FeedbackPage(Customer customer, String orderId) {
        this.customer = customer;
        this.orderId = orderId;

        setTitle("Purchase Feedback");
        setSize(400, 400);
        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel ratingLabel = new JLabel("Rate your experience (1-5):");
        ratingLabel.setBounds(100, 20, 250, 30);
        add(ratingLabel);

        Integer[] ratings = {1, 2, 3, 4, 5};
        JComboBox<Integer> ratingCombo = new JComboBox<>(ratings);
        ratingCombo.setBounds(130, 55, 120, 30);
        add(ratingCombo);

        JLabel feedbackLabel = new JLabel("Your comments:");
        feedbackLabel.setBounds(130, 100, 200, 30);
        add(feedbackLabel);

        JTextArea feedbackArea = new JTextArea();
        feedbackArea.setBounds(50, 130, 300, 120);
        feedbackArea.setLineWrap(true);
        feedbackArea.setWrapStyleWord(true);
        add(feedbackArea);

        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(140, 270, 120, 30);
        add(submitButton);

        submitButton.addActionListener(e -> {
            int rating = (Integer) ratingCombo.getSelectedItem();
            String comment = feedbackArea.getText().trim();

            if (comment.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Comment cannot be empty.");
                return;
            }

            JOptionPane.showMessageDialog(this,
                    "Thank you! Your rating: " + rating + "\nComment: " + comment);

            // Optionally save feedback to file here
            dispose();
        });
    }
}
