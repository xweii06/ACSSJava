package Staff.SaleManagement;

import repositories.AppointmentRepository;
import repositories.SaleRepository;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import navigation.FrameManager;

public class AnalysisPanel extends JPanel {
    private final SaleRepository saleRepo;
    private final AppointmentRepository appointmentRepo;
    
    public AnalysisPanel() {
        this.saleRepo = new SaleRepository();
        this.appointmentRepo = new AppointmentRepository();
        initializeUI();
        loadAnalysisData();
    }
    
    private void initializeUI() {
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Top panel with back button
        JPanel topPanel = new JPanel(new BorderLayout());
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> FrameManager.goBack());
        topPanel.add(backButton, BorderLayout.WEST);
        
        // Stats panel
        JPanel statsPanel = new JPanel(new GridLayout(0, 2, 15, 15));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Sales Analysis"));
        
        // Add stat cards to a separate panel so we can reference them easily
        JPanel cardsPanel = new JPanel(new GridLayout(0, 2, 15, 15));
        cardsPanel.add(createStatCard("Most Popular Payment Method", "Loading..."));
        cardsPanel.add(createStatCard("Cancelled Payments", "Loading..."));
        cardsPanel.add(createStatCard("Average Rating", "Loading..."));
        cardsPanel.add(createStatCard("High Ratings (4-5)", "Loading..."));
        cardsPanel.add(createStatCard("Low Ratings (1-3)", "Loading..."));
        
        // Main layout
        this.add(topPanel, BorderLayout.NORTH);
        this.add(new JScrollPane(cardsPanel), BorderLayout.CENTER);
    }
    
    private JPanel createStatCard(String title, String value) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        valueLabel.setForeground(new Color(0, 100, 0));
        valueLabel.setName(title.replaceAll("\\s+", "_"));
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
    
    private void loadAnalysisData() {
        try {
            // Payment Method Analysis
            String popularPayment = getMostPopularPaymentMethod();
            
            // Cancelled Appointments
            int cancelledCount = getCancelledAppointmentsCount();
            
            // Feedback Analysis
            double avgRating = getAverageRating();
            int[] ratingCounts = getRatingCounts();
            
            // Update UI by finding components by name
            updateCardValue("Most_Popular_Payment_Method", popularPayment);
            updateCardValue("Cancelled_Payments", String.valueOf(cancelledCount));
            updateCardValue("Average_Rating", String.format("%.1f", avgRating));
            updateCardValue("High_Ratings_(4-5)", String.valueOf(ratingCounts[0]));
            updateCardValue("Low_Ratings_(1-3)", String.valueOf(ratingCounts[1]));
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error loading analysis data: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateCardValue(String cardName, String value) {
        Component[] cards = ((JPanel)((JScrollPane)this.getComponent(1)).getViewport().getView()).getComponents();
        for (Component card : cards) {
            Component[] labels = ((JPanel)card).getComponents();
            JLabel valueLabel = (JLabel) labels[1]; 
            if (valueLabel.getName() != null && valueLabel.getName().equals(cardName)) {
                valueLabel.setText(value);
                break;
            }
        }
    }
    
    private String getMostPopularPaymentMethod() throws Exception {
        List<Sale> sales = saleRepo.getAllSales();
        Map<String, Integer> paymentCounts = new HashMap<>();
        
        for (Sale sale : sales) {
            String method = sale.getPaymentMethod();
            paymentCounts.put(method, paymentCounts.getOrDefault(method, 0) + 1);
        }
        
        return paymentCounts.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("No data");
    }
    
    private int getCancelledAppointmentsCount() throws Exception {
        List<Appointment> appointments = appointmentRepo.getAllAppointments();
        int count = 0;
        
        for (Appointment appt : appointments) {
            if ("cancelled".equalsIgnoreCase(appt.getStatus())) {
                count++;
            }
        }
        return count;
    }
    
    private double getAverageRating() throws Exception {
        List<Feedback> feedbacks = Feedback.getAllFeedback();
        if (feedbacks.isEmpty()) return 0;
        
        double sum = 0;
        for (Feedback fb : feedbacks) {
            sum += fb.getRating();
        }
        return sum / feedbacks.size();
    }
    
    private int[] getRatingCounts() throws Exception {
        List<Feedback> feedbacks = Feedback.getAllFeedback();
        int high = 0;
        int low = 0;
        
        for (Feedback fb : feedbacks) {
            if (fb.getRating() >= 4) {
                high++;
            } else {
                low++;
            }
        }
        return new int[]{high, low};
    }
}