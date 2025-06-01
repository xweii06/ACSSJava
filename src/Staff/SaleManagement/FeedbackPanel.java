package Staff.SaleManagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import javax.swing.border.Border;
import navigation.FrameManager;

public class FeedbackPanel extends JPanel {
    
    private List<Feedback> allFeedbacks = new ArrayList<>();
    private int currentIndex = -1;
    
    // UI Components
    private JTextField searchField;
    private JButton searchButton;
    private JButton prevButton;
    private JButton nextButton;
    private JLabel feedbackIDLabel;
    private JLabel userIDLabel;
    private JLabel ratingLabel;
    private JTextArea commentsArea;
    private JPanel navigationPanel;

    public FeedbackPanel() {
        initializeUI();
        loadFeedbackData();
    }
    
    private void initializeUI() {
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> FrameManager.goBack());
        
        // Search Panel
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 0));
        
        searchField = new JTextField(15);
        searchButton = new JButton("Search");
        searchButton.addActionListener(this::performSearch);
        
        searchPanel.add(new JLabel("Feedback ID or User:"), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        
        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.CENTER);
        
        JPanel displayPanel = new JPanel(new BorderLayout());
        Border innerBorder = BorderFactory.createTitledBorder("Feedback Details");
        Border outerBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        displayPanel.setBorder(BorderFactory.createCompoundBorder(
                outerBorder, innerBorder));
        
        JPanel detailsPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        detailsPanel.add(new JLabel("Feedback ID:"));
        feedbackIDLabel = new JLabel();
        detailsPanel.add(feedbackIDLabel);
        
        detailsPanel.add(new JLabel("User ID:"));
        userIDLabel = new JLabel();
        detailsPanel.add(userIDLabel);
        
        detailsPanel.add(new JLabel("Rating:"));
        ratingLabel = new JLabel();
        detailsPanel.add(ratingLabel);
        
        commentsArea = new JTextArea();
        commentsArea.setEditable(false);
        commentsArea.setLineWrap(true);
        commentsArea.setWrapStyleWord(true);
        commentsArea.setBorder(BorderFactory.createTitledBorder("Comments"));
        
        displayPanel.add(detailsPanel, BorderLayout.NORTH);
        displayPanel.add(new JScrollPane(commentsArea), BorderLayout.CENTER);
        
        // Navigation Panel
        navigationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        prevButton = new JButton("Previous");
        prevButton.addActionListener(e -> showPreviousFeedback());
        nextButton = new JButton("Next");
        nextButton.addActionListener(e -> showNextFeedback());
        
        navigationPanel.add(prevButton);
        navigationPanel.add(nextButton);
        
        this.add(topPanel, BorderLayout.NORTH);
        this.add(displayPanel, BorderLayout.CENTER);
        this.add(navigationPanel, BorderLayout.SOUTH);
        
        updateNavigationButtons();
    }

    private void loadFeedbackData() {
        try {
            allFeedbacks = Feedback.getAllFeedback();
            if (allFeedbacks.isEmpty()) {
                clearDisplay();
            } else {
                currentIndex = 0;
                displayCurrentFeedback();
            }
        } catch (IOException ex) {
            allFeedbacks = new ArrayList<>(); // Ensure list is never null
            JOptionPane.showMessageDialog(this, 
                "Error loading feedback data: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            clearDisplay();
        }
        updateNavigationButtons();
    }

    private void performSearch(ActionEvent e) {
        String searchTerm = searchField.getText().trim().toLowerCase();
        if (searchTerm.isEmpty()) {
            loadFeedbackData();
            return;
        }

        List<Feedback> results = new ArrayList<>();
        for (Feedback fb : allFeedbacks) {
            if ((fb.getFeedbackID() != null && fb.getFeedbackID().toLowerCase().contains(searchTerm)) ||
                (fb.getUserID() != null && fb.getUserID().toLowerCase().contains(searchTerm))) {
                results.add(fb);
            }
        }

        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No matching feedback found", 
                "Search Results", JOptionPane.INFORMATION_MESSAGE);
        } else {
            allFeedbacks = results;
            currentIndex = 0;
            displayCurrentFeedback();
        }
    }

    private void displayCurrentFeedback() {
        if (allFeedbacks.isEmpty() || currentIndex < 0 || currentIndex >= allFeedbacks.size()) {
            clearDisplay();
            return;
        }

        Feedback fb = allFeedbacks.get(currentIndex);
        feedbackIDLabel.setText(fb.getFeedbackID() != null ? fb.getFeedbackID() : "N/A");
        userIDLabel.setText(fb.getUserID() != null ? fb.getUserID() : "N/A");
        ratingLabel.setText(getStarRating(fb.getRating()));
        commentsArea.setText(fb.getComments() != null ? fb.getComments() : "No comments");

        updateNavigationButtons();
    }
    
    private String getStarRating(int rating) {
        return "⭐".repeat(rating) + " (" + rating + "/5)";
    }

    private void showPreviousFeedback() {
        if (currentIndex > 0) {
            currentIndex--;
            displayCurrentFeedback();
        }
    }

    private void showNextFeedback() {
        if (currentIndex < allFeedbacks.size() - 1) {
            currentIndex++;
            displayCurrentFeedback();
        }
    }

    private void updateNavigationButtons() {
        prevButton.setEnabled(currentIndex > 0);
        nextButton.setEnabled(currentIndex < allFeedbacks.size() - 1);
        
        if (allFeedbacks.isEmpty()) {
            navigationPanel.setVisible(false);
        } else {
            navigationPanel.setVisible(true);
            navigationPanel.revalidate();
        }
    }

    private void clearDisplay() {
        feedbackIDLabel.setText("");
        userIDLabel.setText("");
        ratingLabel.setText("");
        commentsArea.setText("");
        navigationPanel.setVisible(false);
    }
}