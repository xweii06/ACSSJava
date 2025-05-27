package Main;

public class Car {
    private String carID, model, year, color, price, status, assignedSMID, imagePath;
    
    public Car(String carID, String model, String year, String color, String price,
            String status, String assignedSMID, String imagePath) {
        this.carID = carID;
        this.model = model;
        this.year = year;
        this.color = color;
        this.price = price;
        this.status = status;
        this.assignedSMID = assignedSMID;
        this.imagePath = imagePath;
    }

    public String getCarID() { return carID; }
    public String getModel() { return model; }
    public String getYear() { return year; }
    public String getColor() { return color; }
    public String getPrice() { return price; }
    public String getStatus() { return status; } 
    public String getAssignedSalesmanID() { return assignedSMID; }
    public String getImagePath() { return imagePath; }
    
    public String toDataString() {
        return String.join(",",carID,model,year,color,
                price,status, assignedSMID,imagePath);
    }
}
