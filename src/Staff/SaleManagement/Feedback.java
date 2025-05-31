package Staff.SaleManagement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import utils.DataIO;

public class Feedback {
    private String feedbackID;
    private String userID; // CusID or SalesmanID
    private int rating;
    private String comments;
    private static final String FILE_PATH = "feedback.txt";

    // Constructor
    public Feedback(String feedbackID, String userID, int rating, String comments) {
        this.feedbackID = feedbackID;
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
            String[] parts = line.split(",");

            if (parts.length != 4) {
                System.err.println("Skipping malformed feedback record: " + line);
                continue;
            }

            try {
                // Unescape comments
                String comments = parts[3].trim();
                if (comments.startsWith("\"") && comments.endsWith("\"")) {
                    comments = comments.substring(1, comments.length()-1)
                                     .replace("\"\"", "\"");
                }

                Feedback feedback = new Feedback(
                    parts[0].trim(), // feedbackID
                    parts[1].trim(), // userID
                    Integer.parseInt(parts[2].trim()), // rating
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
        String escapedComments = comments != null ? 
            "\"" + comments.replace("\"", "\"\"") + "\"" : "\"\"";

        return String.join(",",
            feedbackID,
            userID,
            String.valueOf(rating),
            escapedComments
        );
    }
}
