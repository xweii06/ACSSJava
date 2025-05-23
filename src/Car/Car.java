package Car;

public class Car {
    private String carID;
    private String model;
    private String year;
    private String color;
    private String price;
    private String status;
    private String assignedSalesmanID;
    private String imagePath;
    
    public Car(String carID, String model, String year, String color, String price,
            String status, String assignedSalesmanID, String imagePath) {
        this.carID = carID;
        this.model = model;
        this.year = year;
        this.color = color;
        this.price = price;
        this.status = status;
        this.assignedSalesmanID = assignedSalesmanID;
        this.imagePath = imagePath;
    }

    public String getCarID() {
        return carID;
    }

    public String getModel() {
        return model;
    }

    public String getYear() {
        return year;
    }
    
    public String getColor() {
        return color;
    }
    
    public String getPrice() {
        return price;
    }
    
    public String getStatus() {
        return status;
    }
    
    public String getAssignedSalesmanID() {
        return assignedSalesmanID;
    }
    
    public String getImagePath() {
        return imagePath;
    }
    
    public String toDataString() {
        return String.join(",",carID,model,year,color,
                price,status, assignedSalesmanID,imagePath);
    }
}
