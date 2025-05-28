package repositories;

import Salesman.Salesman;
import utils.DataIO;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.util.HashMap;

public class SalesmanRepository implements UserRepository<Salesman> {
    private static final String SALESMAN_FILE = "salesman.txt";
    private static final String CAR_FILE = "car.txt";
    
    @Override
    public List<Salesman> getAll() throws IOException {
        List<Salesman> salesmanList = new ArrayList<>();
        String data = DataIO.readFile(SALESMAN_FILE);
        
        if (data != null) {
            for (String line : data.split("\n")) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 4) {
                        salesmanList.add(new Salesman(parts[0], parts[1], parts[2], parts[3], parts[4]));
                    }
                }
            }
        }
        return salesmanList;
    }
    
    @Override
    public Salesman getByID(String id) throws IOException {
        List<Salesman> salesmanList = getAll();
        for (Salesman salesman : salesmanList) {
            if (salesman.getId().equals(id)) {
                return salesman;
            }
        }
        return null;
    }
    
    @Override
    public void add(Salesman salesman) throws IOException {
        DataIO.appendToFile(SALESMAN_FILE, salesman.toDataString());
    }
    
    @Override
    public void update(Salesman salesman) throws IOException {
        List<Salesman> salesmanList = getAll();
        StringBuilder newData = new StringBuilder();
        
        for (Salesman s : salesmanList) {
            if (s.getId().equals(salesman.getId())) {
                newData.append(salesman.toDataString()).append("\n");
            } else {
                newData.append(s.toDataString()).append("\n");
            }
        }
        DataIO.writeFile(SALESMAN_FILE, newData.toString().trim());
    }
    
    @Override
    public void delete(String id) throws IOException {
        List<Salesman> salesmanList = getAll();
        StringBuilder newData = new StringBuilder();
        
        for (Salesman salesman : salesmanList) {
            if (!salesman.getId().equals(id)) {
                newData.append(salesman.toDataString()).append("\n");
            }
        }
        DataIO.writeFile(SALESMAN_FILE, newData.toString().trim());
    }
    
    public List<String> getAllSalesmanIDs() throws IOException {
        List<String> ids = new ArrayList<>();
        String data = DataIO.readFile(SALESMAN_FILE);
        
        if (data != null) {
            for (String line : data.split("\n")) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length > 0) {
                        ids.add(parts[0]);
                    }
                }
            }
        }
        return ids;
    }
    
    public boolean salesmanExists(String salesmanID) throws IOException {
        List<String> ids = getAllSalesmanIDs();
        return ids.contains(salesmanID);
    }
    
    public ArrayList<String[]> getAssignedCars(String salesmanID) throws IOException {
        ArrayList<String[]> cars = new ArrayList<>();
        String carData = DataIO.readFile(CAR_FILE);

        for (String record : carData.split("\n")) {
            if (!record.trim().isEmpty()) {
                String[] parts = record.split(",");
                if (parts.length >= 7 && parts[6].trim().equals(salesmanID)) {
                    cars.add(parts);
                }
            }
        }
        return cars;
    }

    public HashMap<String, String> getAvailableSalesmen(String excludeSalesmanID) throws IOException {
        HashMap<String, String> salesmen = new HashMap<>();
        String data = DataIO.readFile(SALESMAN_FILE);
        String[] lines = data.split("\n");
        
        if (lines != null) {
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 4 && !parts[0].equals(excludeSalesmanID)) {
                        salesmen.put(parts[0], parts[1]);
                    }
                }
            }
        }
        return salesmen;
    }

    public boolean reassignCar(String carID, String newSalesmanID) throws IOException {
        String data = DataIO.readFile(CAR_FILE);
        String[] lines = data.split("\n");
        StringBuilder newData = new StringBuilder();
        boolean found = false;
        
        if (lines != null) {
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts[0].equals(carID)) {
                        parts[6] = newSalesmanID;
                        found = true;
                    }
                    newData.append(String.join(",", parts)).append("\n");
                }
            }
        }
        
        if (found) {
            DataIO.writeFile(CAR_FILE, newData.toString().trim());
            return true;
        }
        return false;
    }
}
