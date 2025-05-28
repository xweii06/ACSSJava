package Staff.StaffManagement;

import repositories.StaffRepository;
import Staff.Staff;
import utils.InputValidator;
import utils.InvalidInputException;
import java.io.IOException;
import java.util.List;

public class StaffService {
    private final StaffRepository repository;
    
    public StaffService(StaffRepository repository) {
        this.repository = repository;
    }
    
    public List<Staff> getAllStaff() throws IOException {
        return repository.getAll();
    }
    
    public void addStaff(Staff staff) throws InvalidInputException, IOException {
        validateStaff(staff);
        repository.add(staff);
    }
    
    public void updateStaff(Staff staff) throws InvalidInputException, IOException {
        validateStaff(staff);
        if (isSuperAdmin(staff.getId())) {
            throw new InvalidInputException("Cannot modify super admin");
        }
        repository.update(staff);
    }
    
    public void deleteStaff(String id) throws IOException, InvalidInputException {
        if (isSuperAdmin(id)) {
            throw new InvalidInputException("Cannot delete super admin");
        }
        repository.delete(id);
    }
    
    private void validateStaff(Staff staff) throws InvalidInputException {      
        InputValidator.validateName(staff.getName());
        InputValidator.validatePW(staff.getPassword());
        
        // role validation
        if (staff.getRole() == null || staff.getRole().trim().isEmpty()) {
            throw new InvalidInputException("Role cannot be empty");
        }
    }
    
    private boolean isSuperAdmin(String id) throws IOException {
        Staff staff = repository.getByID(id);
        return staff != null && (staff.getRole()).equals("SuperAdmin");
    }
    
    public String generateNewID() throws IOException {
        List<Staff> staffList = repository.getAll();
        int maxID = 0;
        
        for (Staff staff : staffList) {
            String idNum = staff.getId().replaceAll("\\D+", "");
            try {
                int currentID = Integer.parseInt(idNum);
                if (currentID > maxID) {
                    maxID = currentID;
                }
            } catch (NumberFormatException ex) {
                continue;
            }
        }
        
        return "M" + String.format("%02d", maxID + 1);
    }
}