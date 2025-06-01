package Staff.SaleManagement;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import repositories.SaleRepository;

public class ReportsPanel extends JPanel {
    private final SaleRepository saleRepo;
    
    public ReportsPanel() {
        this.saleRepo = new SaleRepository();
        initializeUI();
        loadReportData();
    }

    private void initializeUI() {
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel reportPanel = new JPanel(new GridLayout(0, 2, 15, 15));
        reportPanel.setBorder(BorderFactory.createTitledBorder("Sales Reports"));
        
        reportPanel.add(createReportCard("Total Cars Sold", "Loading..."));
        reportPanel.add(createReportCard("Total Revenue", "Loading..."));
        reportPanel.add(createReportCard("Top Salesman", "Loading..."));
        reportPanel.add(createReportCard("Most Active Customer", "Loading..."));
        
        this.add(new JScrollPane(reportPanel), BorderLayout.CENTER);
    }

    private void loadReportData() {
        try {
            Component[] cards = ((JPanel)((JScrollPane)this.getComponent(0)).getViewport().getView()).getComponents();
            
            ((JLabel)((JPanel)cards[0]).getComponent(1)).setText(
                    String.valueOf(getTotalCarsSold()));
            ((JLabel)((JPanel)cards[1]).getComponent(1)).setText(
                    String.format("RM%.2f", getTotalRevenue()));
            
            String topSalesman = getTopSalesman();
            ((JLabel)((JPanel)cards[2]).getComponent(1)).setText(
                    topSalesman != null ? topSalesman : "No data");
            
            String activeCustomer = getMostActiveCustomer();
            ((JLabel)((JPanel)cards[3]).getComponent(1)).setText(
                    activeCustomer != null ? activeCustomer : "No data");
            
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, 
                "Error loading report data: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createReportCard(String title, String value) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        valueLabel.setForeground(new Color(0, 100, 0));
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
    
    public int getTotalCarsSold() throws IOException {
        return saleRepo.getAllSales().size();
    }

    public double getTotalRevenue() throws IOException {
        double total = 0;
        for (Sale sale : saleRepo.getAllSales()) {
            total += sale.getPrice();
        }
        return total;
    }

    public String getTopSalesman() throws IOException {
        Map<String, Double> salesmanRevenue = new HashMap<>();
        
        for (Sale sale : saleRepo.getAllSales()) {
            String salesmanId = sale.getSalesmanID();
            salesmanRevenue.put(salesmanId, 
                salesmanRevenue.getOrDefault(salesmanId, 0.0) + sale.getPrice());
        }
        
        return salesmanRevenue.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);
    }

    public String getMostActiveCustomer() throws IOException {
        Map<String, Integer> customerPurchases = new HashMap<>();
        
        for (Sale sale : saleRepo.getAllSales()) {
            String customerID = sale.getCustomerID();
            customerPurchases.put(customerID, 
                customerPurchases.getOrDefault(customerID, 0) + 1);
        }
        
        return customerPurchases.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);
    }
}