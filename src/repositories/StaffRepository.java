package repositories;

import Staff.Staff;
import utils.DataIO;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

public class StaffRepository implements UserRepository<Staff> {
    private static final String FILE_PATH = "staff.txt";
    
    @Override
    public List<Staff> getAll() throws IOException {
        List<Staff> staffList = new ArrayList<>();
        String data = DataIO.readFile(FILE_PATH);
        
        if (data != null) {
            for (String line : data.split("\n")) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 4) {
                        staffList.add(new Staff(parts[0], parts[1], parts[2], parts[3]));
                    }
                }
            }
        }
        return staffList;
    }
    
    @Override
    public Staff getByID(String id) throws IOException {
        List<Staff> staffList = getAll();
        for (Staff staff : staffList) {
            if (staff.getId().equals(id)) {
                return staff;
            }
        }
        return null;
    }
    
    @Override
    public void add(Staff staff) throws IOException {
        DataIO.appendToFile(FILE_PATH, staff.toDataString());
    }
    
    @Override
    public void update(Staff staff) throws IOException {
        List<Staff> staffList = getAll();
        StringBuilder newData = new StringBuilder();
        
        for (Staff s : staffList) {
            if (s.getId().equals(staff.getId())) {
                newData.append(staff.toDataString()).append("\n");
            } else {
                newData.append(s.toDataString()).append("\n");
            }
        }
        DataIO.writeFile(FILE_PATH, newData.toString().trim());
    }
    
    @Override
    public void delete(String id) throws IOException {
        List<Staff> staffList = getAll();
        StringBuilder newData = new StringBuilder();
        
        for (Staff staff : staffList) {
            if (!staff.getId().equals(id)) {
                newData.append(staff.toDataString()).append("\n");
            }
        }
        DataIO.writeFile(FILE_PATH, newData.toString().trim());
    }
}
