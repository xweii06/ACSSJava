package Staff.CarManagement;

import repositories.CarRepository;
import repositories.SalesmanRepository;
import repositories.AppointmentRepository;
import Main.Car;
import Staff.CustomerManagement.CustomerService;
import Staff.SaleManagement.Appointment;
import Staff.SaleManagement.Sale;
import java.io.File;
import utils.InvalidInputException;
import java.io.IOException;
import java.time.Year;
import java.util.List;
import repositories.CustomerRepository;
import repositories.SaleRepository;


public class CarService {
    private final CarRepository repository;
    private final SalesmanRepository salesmanRepo;
    private final AppointmentRepository apptRepo;
    private final SaleRepository saleRepo;

    public CarService(CarRepository repository) {
        this.repository = repository;
        this.salesmanRepo = new SalesmanRepository();
        this.apptRepo = new AppointmentRepository();
        this.saleRepo = new SaleRepository();
    }

    public List<Car> getAllCars() throws IOException {
        return repository.getAllCars();
    }
    
    public List<String> getAllSalesmanIDs() throws IOException {
        return salesmanRepo.getAllSalesmanIDs();
    }

    public Car getCarByID(String carID) throws IOException {
        return repository.getCarByID(carID);
    }

    public void addCar(Car car) throws IOException, InvalidInputException {
        validateCar(car);
        if (car.getImagePath() != null && !car.getImagePath().isEmpty()) {
            CarRepository.validateImageFile(new File(car.getImagePath()));
        }
        repository.addCar(car);
    }

    public void updateCar(Car car) throws IOException, InvalidInputException {
        validateCar(car);
        if (car.getImagePath() != null && !car.getImagePath().isEmpty()) {
            CarRepository.validateImageFile(new File(car.getImagePath()));
        }
        // Validate salesman ID exists
        if (car.getAssignedSalesmanID() != null && !car.getAssignedSalesmanID().isEmpty()) {
            if (!salesmanRepo.salesmanExists(car.getAssignedSalesmanID())) {
                throw new InvalidInputException("Salesman with ID " + car.getAssignedSalesmanID() + " does not exist");
            }
        }
        repository.updateCar(car);
    }

    public void deleteCar(String carID) throws IOException {
        repository.deleteCar(carID);
    }
    
    public void validateCar(Car car) throws InvalidInputException {
        // check ID
        if (car.getCarID() == null || car.getCarID().trim().isEmpty()) {
            throw new InvalidInputException("Car ID is required");
        }
        // check model
        if (car.getModel() == null || car.getModel().trim().isEmpty()) {
            throw new InvalidInputException("Model is required");
        }
        if (car.getModel().length() < 2 || car.getModel().length() > 30) {
            throw new InvalidInputException("Model name must be between 2 and 30 characters");
        }
        // check year
        if (car.getYear() == null || car.getYear().trim().isEmpty()) {
            throw new InvalidInputException("Year is required");
        }
        try {
            int year = Integer.parseInt(car.getYear());
            if (year < 1960 || year > Year.now().getValue()) {
                throw new InvalidInputException("Invalid year");
            }
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Year must be a number");
        }
        // check color
        if (!car.getColor().matches("^[a-zA-Z ]+$")) {
            throw new InvalidInputException("Color can only contain letters and spaces");
        }
        if (car.getColor().length() < 2 || car.getColor().length() > 10) {
            throw new InvalidInputException("Color must be between 2 and 10 characters");
        }
        // check price
        if (car.getPrice() == null || car.getPrice().trim().isEmpty()) {
            throw new InvalidInputException("Price is required");
        }
        try {
            double priceValue = Double.parseDouble(car.getPrice());
            if (priceValue <= 0) {
                throw new InvalidInputException("Price must be greater than 0");
            } 
            if (priceValue > 90000000 || priceValue < 1000) {
                throw new InvalidInputException("Price amount is too big or too small");
            }
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Price must be a valid number without letters");
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
    
    public boolean isCarAvailable(String carID) throws IOException {
        return repository.isCarAvailable(carID);
    }
    
    public CustomerService getCustomerService() {
        return new CustomerService(new CustomerRepository());
    }
    
    public void addAppointment(Appointment appointment) throws IOException {
        apptRepo.addAppointment(appointment);
    }
    
    public void updateAppointment(Appointment appointment) throws IOException {
        apptRepo.updateAppointment(appointment);
    }

    public Appointment findAppointmentByCarID(String carID) throws IOException {
        return apptRepo.findAppointmentByCarID(carID);
    }

    public String generateSaleID() {
        return saleRepo.generateSaleID();
    }
    
    public void addSale(Sale sale) throws IOException {
        saleRepo.addSale(sale);
    }
}