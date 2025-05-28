package repositories;

import Main.Car;
import utils.DataIO;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CarRepository {
    private static final String FILE_PATH = "car.txt";

    public List<Car> getAllCars() throws IOException {
        List<Car> cars = new ArrayList<>();
        String data = DataIO.readFile(FILE_PATH);
        
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
                            parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], imagePath
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
        DataIO.appendToFile(FILE_PATH, car.toDataString());
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
        DataIO.writeFile(FILE_PATH, newData.toString().trim());
    }

    public void deleteCar(String carID) throws IOException {
        List<Car> cars = getAllCars();
        StringBuilder newData = new StringBuilder();
        String imagePathToDelete = null;
        
        for (Car car : cars) {
            if (car.getCarID().equals(carID)) {
                imagePathToDelete = car.getImagePath();
            } else {
                newData.append(car.toDataString()).append("\n");
            }
        }
        
        DataIO.writeFile(FILE_PATH, newData.toString().trim());
        
        // Delete the associated image file
        if (imagePathToDelete != null) {
            deleteCarImage(imagePathToDelete);
        }
    }
    
    private void deleteCarImage(String imagePath) {
        if (imagePath == null || imagePath.trim().isEmpty()) {
            return;
        }
        try {
            Path imageFile = Paths.get(imagePath).normalize();
            if (Files.exists(imageFile)) {
                Files.delete(imageFile);
                System.out.println("Successfully deleted image: " + imageFile.toAbsolutePath());
            } else {
                System.out.println("Image file not found: " + imageFile.toAbsolutePath());
            }
        } catch (Exception ex) {
            System.err.println("Exception when deleting image: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    public boolean isCarAvailable(String carID) throws IOException {
        List<Car> cars = getAllCars();
        for (Car car : cars) {
            if (car.getCarID().equals(carID)) {
                return "Available".equals(car.getStatus());
            }
        }
        return false;
    }
}
   