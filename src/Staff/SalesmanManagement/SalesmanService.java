package Staff.SalesmanManagement;

import repositories.SalesmanRepository;
import repositories.SaleRepository;
import Salesman.Salesman;
import Staff.SaleManagement.Sale;
import utils.InputValidator;
import utils.InvalidInputException;
import java.io.IOException;
import java.util.List;

public class SalesmanService {
    private final SalesmanRepository repository;
    private final SaleRepository saleRepo;
    
    public SalesmanService(SalesmanRepository repository) {
        this.repository = repository;
        this.saleRepo = new SaleRepository();
    }
    
    public List<Salesman> getAllSalesmen() throws IOException {
        return repository.getAll();
    }
    
    public void addSalesman(Salesman salesman) throws InvalidInputException, IOException {
        // correct the phone format
        String newPhone = InputValidator.parsePhoneFormat(salesman.getPhone());
        salesman.setPhone(newPhone);
        // validate
        validateSalesman(salesman);
        // check for duplicates
        checkDuplicatePhone(newPhone,null);
        checkDuplicateEmail(salesman.getEmail(),null);
        repository.add(salesman);
    }
    
    public void updateSalesman(Salesman salesman) throws InvalidInputException, IOException {
        // correct the phone format
        String newPhone = InputValidator.parsePhoneFormat(salesman.getPhone());
        salesman.setPhone(newPhone);
        // validate
        validateSalesman(salesman);
        // check for duplicates
        checkDuplicatePhone(newPhone,salesman.getId());
        checkDuplicateEmail(salesman.getEmail(),salesman.getId());
        repository.update(salesman);
    }
    
    public void deleteSalesman(String id) throws IOException, InvalidInputException {
        // Check if salesman has any unpaid cars
        List<String[]> assignedCars = repository.getAssignedCars(id);
        for (String[] car : assignedCars) {
            String status = car[5];
            if (!status.equals("Paid") && !status.equals("Cancelled")) {
                throw new InvalidInputException(
                    "Cannot delete salesman with booked cars. Reassign them first.");
            }
        }
        repository.delete(id);
        updateRecordForDeletedSalesman(id);
    }
    
    private void validateSalesman(Salesman salesman) throws IOException, InvalidInputException {
        InputValidator.validateName(salesman.getName());
        InputValidator.validatePhone(salesman.getPhone());
        InputValidator.validateEmail(salesman.getEmail());
        InputValidator.validatePW(salesman.getPassword());
    }
    
    private static void checkDuplicatePhone(String phone, String excludeID) throws IOException, InvalidInputException {
        List<Salesman> salesmen = new SalesmanRepository().getAll();
        
        for (Salesman s : salesmen) {
            if ((excludeID == null || !s.getId().equals(excludeID)) 
                    && s.getPhone().equals(phone)) {
                throw new InvalidInputException("Phone number already exists in the system.");
            }
        }
    }
    
    private static void checkDuplicateEmail(String email, String excludeID) throws IOException, InvalidInputException {
        List<Salesman> salesmen = new SalesmanRepository().getAll();
        
        for (Salesman s : salesmen) {
            if ((excludeID == null || !s.getId().equals(excludeID)) 
                    && s.getEmail().equalsIgnoreCase(email)) {
                throw new InvalidInputException("Email already exists in the system.");
            }
        }
    }
    
    public String generateNewID() throws IOException {
        List<Salesman> salesmen = repository.getAll();
        int maxID = 0;
        
        for (Salesman salesman : salesmen) {
            String idNum = salesman.getId().replaceAll("\\D+", "");
            try {
                int currentID = Integer.parseInt(idNum);
                if (currentID > maxID) {
                    maxID = currentID;
                }
            } catch (NumberFormatException ex) {
            }
        }
        
        return "S" + String.format("%02d", maxID + 1);
    }
    
    private boolean updateRecordForDeletedSalesman(String salesmanID) throws IOException {
        List<Sale> sales = saleRepo.findSalesBySalesmanID(salesmanID);
        if (sales != null && !sales.isEmpty()) { 
            for (Sale sale : sales) {
                if (sale != null) {
                    sale.setSalesmanID("DLTD_USER_" + salesmanID);
                    saleRepo.updateSale(sale);
                }
            }
        }
        return true;
    }
}