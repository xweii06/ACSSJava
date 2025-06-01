package repositories;

import Main.Car;
import Staff.CarManagement.CarDialog;
import java.io.File;
import utils.DataIO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CarRepository {
    private static final String CAR_FILE = "car.txt";
    private static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final String[] ALLOWED_EXTENSIONS = {"jpg", "jpeg", "png"};
    
    public List<Car> getAllCars() throws IOException {
        List<Car> cars = new ArrayList<>();
        String data = DataIO.readFile(CAR_FILE);
        
        if (data != null) {
            for (String line : data.split("\n")) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    String imagePath = "";
                    if (parts.length >= 8) {
                        imagePath = parts[7];
                    }
                    if (parts.length >= 7) {
                        Car car = new Car(
                                parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], 
                                imagePath.isEmpty() ? null : imagePath
                        );
                        cars.add(car);
                    }
                }
            }
        }
        return cars;
    }

    public Car getCarByID(String carID) throws IOException {
        List<Car> cars = getAllCars();
        for (Car car : cars) {
            if (car.getCarID().equals(carID)) {
                return car;
            }
        }
        return null;
    }

    public void addCar(Car car) throws IOException {
        DataIO.appendToFile(CAR_FILE, car.toDataString());
    }

    public void updateCar(Car updatedCar) throws IOException {
        List<Car> cars = getAllCars();
        StringBuilder newData = new StringBuilder();
        
        for (Car car : cars) {
            if (car.getCarID().equals(updatedCar.getCarID())) {
                newData.append(updatedCar.toDataString()).append("\n");
            } else {
                newData.append(car.toDataString()).append("\n");
            }
        }
        DataIO.writeFile(CAR_FILE, newData.toString().trim());
    }

    public void deleteCar(String carID) throws IOException {
        Car car = getCarByID(carID);
        if (car != null && car.getImagePath() != null) {
            DataIO.deleteCarImage(car.getImagePath());
        }
        
        List<Car> cars = getAllCars();
        StringBuilder newData = new StringBuilder();
        String imagePathToDelete = null;
        
        for (Car c : cars) {
            if (!c.getCarID().equals(carID)) {
                newData.append(c.toDataString()).append("\n");
            }
        }
        DataIO.writeFile(CAR_FILE, newData.toString().trim());
        
        // Delete the associated image file
        if (imagePathToDelete != null) {
            DataIO.deleteCarImage(imagePathToDelete);
        }
    }
    
    public boolean isCarAvailable(String carID) throws IOException {
        List<Car> cars = getAllCars();
        for (Car car : cars) {
            if (car.getCarID().equals(carID)) {
                return "available".equals(car.getStatus());
            }
        }
        return false;
    }
    
    public static void validateImageFile(File file) throws IOException {
        if (file == null) return;
        if (!file.exists()) {
            throw new IOException("Selected file does not exist");
        }
        if (file.length() > MAX_IMAGE_SIZE) {
            throw new IOException("Image exceeds maximum size of " + 
                (MAX_IMAGE_SIZE/1024/1024) + "MB");
        }
        String extension = CarDialog.getFileExtension(file.getName());
        boolean valid = false;
        for (String ext : ALLOWED_EXTENSIONS) {
            if (ext.equalsIgnoreCase(extension)) {
                valid = true;
                break;
            }
        }
        if (!valid) {
            throw new IOException("Only JPEG, JPG, PNG images are allowed");
        }
    }
    
    public boolean carIDExists(String carID) throws IOException {
        List<Car> cars = getAllCars();
        for (Car car : cars) {
            if (car.getCarID().equalsIgnoreCase(carID)) {
                return true;
            }
        }
        return false;
    }
}
   