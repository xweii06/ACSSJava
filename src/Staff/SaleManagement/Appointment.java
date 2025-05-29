package Staff.SaleManagement;

import java.time.LocalDate;

public class Appointment {
    private String orderID;
    private String cusID;
    private String carID;
    private String model;
    private double price;
    private LocalDate dueDate;
    private String status;

    // Constructor
    public Appointment(String orderID, String cusID, String carID, String model,
                       double price, LocalDate dueDate, String status) {
        this.orderID = orderID;
        this.cusID = cusID;
        this.carID = carID;
        this.model = model;
        this.price = price;
        this.dueDate = dueDate;
        this.status = status;
    }

    // Getters and Setters
    public String getOrderID() { return orderID; }
    public void setOrderID(String orderID) { this.orderID = orderID; }

    public String getCustomerID() { return cusID; }
    public void setCustomerID(String cusID) { this.cusID = cusID; }

    public String getCarID() { return carID; }
    public void setCarID(String carId) { this.carID = carID; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // toString method for file writing
    @Override
    public String toString() {
        return orderID + "," + cusID + "," + carID + "," + model + "," +
               price + "," + dueDate + "," + status;
    }
}
