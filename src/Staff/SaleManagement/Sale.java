package Staff.SaleManagement;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Sale {
    private String saleID;
    private String customerID;
    private String carID;
    private String salesmanID;
    private double price;
    private String paymentMethod;
    private Date saleDate;

    public Sale(String saleID, String customerID, String carID, String salesmanID,
                double price, String paymentMethod, Date saleDate) {
        this.saleID = saleID;
        this.customerID = customerID;
        this.carID = carID;
        this.salesmanID = salesmanID;
        this.price = price;
        this.paymentMethod = paymentMethod;
        this.saleDate = saleDate;
    }

    // Getters and Setters
    public String getSaleID() { return saleID; }
    public void setSaleID(String saleID) { this.saleID = saleID; }

    public String getCustomerID() { return customerID; }
    public void setCustomerID(String customerID) { this.customerID = customerID; }

    public String getCarID() { return carID; }
    public void setCarID(String carID) { this.carID = carID; }

    public String getSalesmanID() { return salesmanID; }
    public void setSalesmanID(String salesmanID) { this.salesmanID = salesmanID; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public Date getSaleDate() { return saleDate; }
    public void setSaleDate(Date saleDate) { this.saleDate = saleDate; }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return String.join(",",
            saleID,
            customerID,
            carID,
            salesmanID,
            String.valueOf(price),
            paymentMethod,
            dateFormat.format(saleDate)
        );
    }
}