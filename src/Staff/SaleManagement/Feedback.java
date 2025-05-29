package Staff.SaleManagement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import utils.DataIO;

public class Feedback {
    private String feedbackID;
    private String saleID;
    private String userID; // CusID or SalesmanID
    private int rating;
    private String comments;
    private static final String FILE_PATH = "feedback.txt";

    // Constructor
    public Feedback(String feedbackID, String saleID, String userID, int rating, String comments) {
        this.feedbackID = feedbackID;
        this.saleID = saleID;
        this.userID = userID;
        this.rating = rating;
        this.comments = comments;
    }

    // Getters and Setters
    public String getFeedbackID() {
        return feedbackID;
    }

    public void setFeedbackID(String feedbackID) {
        this.feedbackID = feedbackID;
    }

    public String getSaleID() {
        return saleID;
    }

    public void setSaleID(String saleID) {
        this.saleID = saleID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
    
    private Feedback fromLine(String line) {
        // Regex to split on commas not inside quotes
        String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

        if (parts.length != 5) {
            System.err.println("Invalid feedback record: " + line);
            return null;
        }

        try {
            // Unescape comments
            String comments = parts[4].trim();
            if (comments.startsWith("\"") && comments.endsWith("\"")) {
                comments = comments.substring(1, comments.length()-1)
                                 .replace("\"\"", "\"");
            }

            return new Feedback(
                parts[0].trim(),
                parts[1].trim(),
                parts[2].trim(),
                Integer.parseInt(parts[3].trim()),
                comments.isEmpty() ? null : comments
            );
        } catch (Exception ex) {
            System.err.println("Error parsing feedback: " + line);
            ex.printStackTrace();
            return null;
        }
    }
    
    public static List<Feedback> getAllFeedback() throws IOException {
        List<Feedback> feedbackList = new ArrayList<>();
        String data = DataIO.readFile(FILE_PATH);

        if (data == null || data.trim().isEmpty()) {
            return feedbackList;
        }

        for (String line : data.split("\n")) {
            line = line.trim();
            if (line.isEmpty()) continue;

            // Use regex to split on commas not inside quotes
            String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

            if (parts.length != 5) {
                System.err.println("Skipping malformed feedback record: " + line);
                continue;
            }

            try {
                // Unescape comments
                String comments = parts[4].trim();
                if (comments.startsWith("\"") && comments.endsWith("\"")) {
                    comments = comments.substring(1, comments.length()-1)
                                     .replace("\"\"", "\"");
                }

                Feedback feedback = new Feedback(
                    parts[0].trim(), // feedbackID
                    parts[1].trim(), // saleID
                    parts[2].trim(), // userID
                    Integer.parseInt(parts[3].trim()), // rating
                    comments.isEmpty() ? null : comments  // comments
                );
                feedbackList.add(feedback);
            } catch (Exception ex) {
                System.err.println("Error parsing feedback record: " + line);
                ex.printStackTrace();
            }
        }
        return feedbackList;
    }
    
    @Override
    public String toString() {
        // Handle null comments
        String safeComments = comments != null ? 
            "\"" + comments.replace("\"", "\"\"") + "\"" : "";

        return String.join(",",
            feedbackID,
            saleID,
            userID,
            String.valueOf(rating),
            safeComments
        );
    }
}
