package Staff.CarManagement;

import repositories.CarRepository;
import repositories.SalesmanRepository;
import Main.Car;
import utils.InvalidInputException;
import java.io.IOException;
import java.util.List;

public class CarService {
    private final CarRepository repository;
    private final SalesmanRepository salesmanRepository;

    public CarService(CarRepository repository) {
        this.repository = repository;
        this.salesmanRepository = new SalesmanRepository();
    }

    public List<Car> getAllCars() throws IOException {
        return repository.getAllCars();
    }
    
    public List<String> getAllSalesmanIDs() throws IOException {
        return salesmanRepository.getAllSalesmanIDs();
    }

    public Car getCarByID(String carID) throws IOException {
        return repository.getCarByID(carID);
    }

    public void addCar(Car car) throws IOException, InvalidInputException {
        validateCar(car);
        repository.addCar(car);
    }

    public void updateCar(Car car) throws IOException, InvalidInputException {
        validateCar(car);
        
        // Validate salesman ID exists
        if (car.getAssignedSalesmanID() != null && !car.getAssignedSalesmanID().isEmpty()) {
            if (!salesmanRepository.salesmanExists(car.getAssignedSalesmanID())) {
                throw new InvalidInputException("Salesman with ID " + car.getAssignedSalesmanID() + " does not exist");
            }
        }
        repository.updateCar(car);
    }

    public void deleteCar(String carID) throws IOException {  
        repository.deleteCar(carID);
    }
    
    private void validateCar(Car car) throws InvalidInputException {
        if (car.getCarID() == null || car.getCarID().trim().isEmpty()) {
            throw new InvalidInputException("Car ID is required");
        }
        if (car.getModel() == null || car.getModel().trim().isEmpty()) {
            throw new InvalidInputException("Model is required");
        }
        if (car.getYear() == null || car.getYear().trim().isEmpty()) {
            throw new InvalidInputException("Year is required");
        }
        if (car.getPrice() == null || car.getPrice().trim().isEmpty()) {
            throw new InvalidInputException("Price is required");
        }
    }
    
    public String generateNewCarID() throws IOException {
        List<Car> cars = repository.getAllCars();
        int maxID = 0;
        
        for (Car car : cars) {
            String idNum = car.getCarID().replaceAll("\\D+", "");
            try {
                int currentID = Integer.parseInt(idNum);
                if (currentID > maxID) {
                    maxID = currentID;
                }
            } catch (NumberFormatException ex) {
                continue;
            }
        }
        
        return "CAR" + String.format("%02d", maxID + 1);
    }
}