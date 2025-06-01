package utils;

import java.io.*;
import java.util.*;

public class FeedbackManager {
    public static Map<String, Integer> getFeedbackRatings() {
        Map<String, Integer> map = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("data/feedback.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 4);
                if (parts.length >= 3) {
                    String orderId = parts[0];
                    int rating = Integer.parseInt(parts[2]);
                    map.put(orderId, rating);
                }
            }
        } catch (IOException | NumberFormatException e) {
        }
        return map;
    }
}
