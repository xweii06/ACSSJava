package repositories;

import Staff.SaleManagement.Sale;
import utils.DataIO;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class SaleRepository {
    private static final String FILE_PATH = "sales.txt";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    
    public List<Sale> getAllSales() throws IOException {
        List<Sale> salesList = new ArrayList<>();
        String data = DataIO.readFile(FILE_PATH);
        
        if (data == null || data.trim().isEmpty()) {
            return salesList;
        }
        
        for (String line : data.split("\n")) {
            line = line.trim();
            if (line.isEmpty()) continue;
            
            String[] parts = line.split(",", -1); 
            if (parts.length != 7) {  // Changed from 6 to 7
                System.err.println("Skipping malformed sale record: " + line);
                continue;
            }
            
            try {
                Date saleDate = DATE_FORMAT.parse(parts[6].trim());
                Sale sale = new Sale(
                    parts[0].trim(), // saleID
                    parts[1].trim(), // customerID
                    parts[2].trim(), // carID
                    parts[3].trim(), // salesmanID
                    Double.parseDouble(parts[4].trim()), // price
                    parts[5].trim(),  // paymentMethod
                    saleDate          // saleDate
                );
                salesList.add(sale);
            } catch (Exception ex) {
                System.err.println("Error parsing sale record: " + line);
                ex.printStackTrace();
            }
        }
        return salesList;
    }
    
    
    public void addSale(Sale sale) throws IOException {
        if (sale == null) {
            throw new IllegalArgumentException("Sale cannot be null");
        }
        DataIO.appendToFile(FILE_PATH, sale.toString() + "\n");
    }
    
    public void updateSale(Sale updated) throws IOException {
        List<Sale> sales = getAllSales();
        List<String> updatedLines = new ArrayList<>();
        
        for (Sale sale : sales) {
            if (sale.getSaleID().equals(updated.getSaleID())) {
                updatedLines.add(toDataString(updated));
            } else {
                updatedLines.add(toDataString(sale));
            }
        }
        DataIO.writeFile(FILE_PATH, String.join("\n", updatedLines));
    }
    
    public Sale findSaleByID(String saleID) throws IOException {
        if (saleID == null || saleID.trim().isEmpty()) {
            throw new IllegalArgumentException("Sale ID cannot be null or empty");
        }
        
        for (Sale sale : getAllSales()) {
            if (saleID.equals(sale.getSaleID())) {
                return sale;
            }
        }
        return null;
    }
    
    public List<Sale> findSalesBySalesmanID(String salesmanID) throws IOException {
        List<Sale> records = new ArrayList<>();
        for (Sale sale : getAllSales()) {
            if (salesmanID.equals(sale.getSalesmanID())) {
                records.add(sale);
            }
        }
        return records;
    }
    
    public List<Sale> findSalesByCustomerID(String customerID) throws IOException {
        List<Sale> sales = new ArrayList<>();
        String data = DataIO.readFile(FILE_PATH);

        if (data != null && !data.isEmpty()) {
            for (String line : data.split("\n")) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length >= 7 && parts[1].equals(customerID)) {
                    try {
                        Date saleDate = DATE_FORMAT.parse(parts[6].trim());
                        Sale sale = new Sale(
                            parts[0], // saleID
                            parts[1], // customerID
                            parts[2], // carID
                            parts[3], // salesmanID
                            Double.parseDouble(parts[4]), // price
                            parts[5],  // paymentMethod
                            saleDate   // saleDate
                        );
                        sales.add(sale);
                    } catch (Exception ex) {
                        System.err.println("Error parsing sale record: " + line);
                    }
                }
            }
        }
        return sales;
    }
    
    public String generateSaleID() {
        String uuidPart = UUID.randomUUID().toString().replace("-", "").substring(0, 4).toUpperCase();
        return "SA" + uuidPart;
    }
    
    private String toDataString(Sale sale) {
        return sale.toString();
    }

}