package Customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FeedbackPage extends JFrame {
    private Customer customer;
    private String orderId;

    public FeedbackPage(Customer customer, String orderId) {
        this.customer = customer;
        this.orderId = orderId;

        setTitle("Purchase Feedback");
        setSize(450, 400);
        setLocationRelativeTo(null); // 居中窗口
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 主面板
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // 评分标签
        JLabel ratingLabel = new JLabel("Rate your experience (1-5):");
        ratingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(ratingLabel);

        Integer[] ratings = {1, 2, 3, 4, 5};
        JComboBox<Integer> ratingCombo = new JComboBox<>(ratings);
        ratingCombo.setMaximumSize(new Dimension(100, 30));
        ratingCombo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createVerticalStrut(5));
        panel.add(ratingCombo);

        // 留言标签
        panel.add(Box.createVerticalStrut(20));
        JLabel feedbackLabel = new JLabel("Your comments:");
        feedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(feedbackLabel);

        JTextArea feedbackArea = new JTextArea(5, 30);
        feedbackArea.setLineWrap(true);
        feedbackArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(feedbackArea);
        panel.add(Box.createVerticalStrut(5));
        panel.add(scrollPane);

        // 提交按钮
        panel.add(Box.createVerticalStrut(20));
        JButton submitButton = new JButton("Submit");
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(submitButton);

        add(panel);

        // 提交事件
        submitButton.addActionListener(e -> {
            int rating = (Integer) ratingCombo.getSelectedItem();
            String comment = feedbackArea.getText().trim();

            if (comment.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Comment cannot be empty.");
                return;
            }

            // 写入文件
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/feedback.txt", true))) {
                writer.write(String.join(",",
                        orderId,
                        customer.getId(),
                        String.valueOf(rating),
                        comment.replaceAll(",", " ")  // 防止CSV错误
                ));
                writer.newLine();
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error saving feedback.");
                return;
            }

            // 显示星级评价
            String stars = "★".repeat(rating) + "☆".repeat(5 - rating);
            JOptionPane.showMessageDialog(this,
                    "Thank you for your feedback!\n\nRating: " + stars + " (" + rating + "/5)\nComment: " + comment);
            dispose();
        });
    }
}
