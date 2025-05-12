package Staff;

import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import utils.DataIO;

public class DeleteRecords {
    
    private static final String STAFF_FILE = "staff.txt", SALESMAN_FILE = "salesman.txt",
            CUSTOMER_FILE = "customers.txt", CAR_FILE = "car.txt";
    
    private static String getFilename(String menuItem) {
        switch (menuItem) {
            case "Staff Management": return STAFF_FILE;
            case "Salesman Management": return SALESMAN_FILE;
            case "Customer Management": return CUSTOMER_FILE;
            case "Car Management": return CAR_FILE;
            default:
                throw new IllegalArgumentException("Unknown menu item: " + menuItem);
        }
    }
    
    public static void deleteSelectedRecords(String menuItem, JTable table) {
        int[] selectedRows = table.getSelectedRows();
        
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(null, 
                "Please select records to delete", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        TableModel model = table.getModel();
        ArrayList<String> recordIDs = new ArrayList<>();
        
        for (int viewRow : selectedRows) {
            int modelRow = table.convertRowIndexToModel(viewRow);
            String recordID = model.getValueAt(modelRow, 0).toString();
            recordIDs.add(recordID);
        }
        for (String recordID : recordIDs) {
            if (!validateDeletion(menuItem, recordID)) {
                return; 
            }
        }
        
        int confirm = JOptionPane.showConfirmDialog(null, 
            "Are you sure you want to delete " + recordIDs.size() + " record?", 
            "Confirm Deletion", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            for (String recordID : recordIDs) {
                deleteCurrentRecord(menuItem, recordID);
            }
            StaffMenu.refreshMenu(menuItem);
        }
    }
    
    private static boolean validateDeletion(String menuItem, String recordID) {
        switch (menuItem) {
            case "Staff Management":
                String superAdminID = getSuperAdminID();
                if (recordID.equals(superAdminID)) {
                    JOptionPane.showMessageDialog(null,
                        "Cannot delete super admin.",
                        "Action blocked", JOptionPane.WARNING_MESSAGE);
                    return false;
                }
                break;
            case "Salesman Management":
                boolean reassigned = reassignCarsBeforeDeletion(recordID);
                if (!reassigned) {
                    return false;
                }
                break;
            case "Car Management":
                if (!isAvailable(recordID)) {
                    JOptionPane.showMessageDialog(null,
                        "Cannot delete unavailable car.",
                        "Action blocked", JOptionPane.WARNING_MESSAGE);
                    return false;
                }
                break;
        }
        return true;
    }
    
    private static String getSuperAdminID() {
        String[] lines = FileManager.getLines(STAFF_FILE);
        if (lines != null) {
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String id = parts[0];
                    if (!id.startsWith("M")) {
                        return id;
                    }
                }
            }
        }
        return null;
    }
    
    private static boolean reassignCarsBeforeDeletion(String currentRecordID) {
        try {
            ArrayList<String[]> assignedCars = getAssignedCars(currentRecordID);
            if (assignedCars.isEmpty()) {
                return true;
            }

            HashMap<String, String> availableSalesmen = getAvailableSalesmen(currentRecordID);
            int successfullyReassigned = 0;
            String[] options = createDropdownOptions(availableSalesmen);
            
            for (String[] car : assignedCars) {
                String carID = car[0];
                String model = car[1];
                String status = car[4];
                String salesmanID = car[5];
                
                String newSalesmanID = "DELETED_USER_" + salesmanID;
                if (!status.equals("Paid") || !status.equals("Cancelled")) {
                    String selected = showDropdownDialog(carID, model, options);
                    if (selected == null) {
                        break;
                    }
                    newSalesmanID = selected.split(" - ")[0];
                    reassignSingleCar(carID, newSalesmanID);
                }
                successfullyReassigned++;
            }
            
            if (successfullyReassigned == assignedCars.size()) {
                JOptionPane.showMessageDialog(null,
                    "Reassigned all " + successfullyReassigned + " cars",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                return true;
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null,
                "Error during deletion: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    private static ArrayList<String[]> getAssignedCars(String currentRecordID) throws IOException {
        ArrayList<String[]> cars = new ArrayList<>();
        String carData = DataIO.readFile(CAR_FILE);

        for (String record : carData.split("\n")) {
            if (!record.trim().isEmpty()) {
                String[] parts = record.split(",");
                if (parts.length >= 7 && parts[6].trim().equals(currentRecordID)) {
                    cars.add(parts);
                }
            }
        }
        return cars;
    }

    private static HashMap<String, String> getAvailableSalesmen(String currentRecordID) throws IOException {
        HashMap<String, String> salesmen = new HashMap<>();
        String[] lines = FileManager.getLines(SALESMAN_FILE);
        if (lines != null) {
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 4 && !parts[0].equals(currentRecordID)) {
                        salesmen.put(parts[0], parts[1]);
                    }
                }
            }
            return salesmen;
        }
        return null;
    }
    
    private static String[] createDropdownOptions(HashMap<String, String> salesmen) {
        String[] options = new String[salesmen.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : salesmen.entrySet()) {
            options[i++] = entry.getKey() + " - " + entry.getValue();
        }
        return options;
    }

    private static String showDropdownDialog(String carID, String carModel, String[] options) {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.add(new JLabel("Reassign Car: " + carID + " (" + carModel + ")"));

        JComboBox<String> dropdown = new JComboBox<>(options);
        dropdown.setSelectedIndex(0);
        panel.add(dropdown);

        int result = JOptionPane.showConfirmDialog(null,
            panel,
            "Select New Salesman",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE);

        return (result == JOptionPane.OK_OPTION) ? 
            (String) dropdown.getSelectedItem() : 
            null;
    }

    private static boolean reassignSingleCar(String carID, String newSalesmanID) throws IOException {
        String[] lines = FileManager.getLines(CAR_FILE);
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
    
    private static boolean isAvailable(String id) {
        String[] lines = FileManager.getLines(CAR_FILE);
        if (lines != null) {
            for (String line : lines) {
                if (line.startsWith(id + ",")) {
                    String parts[] = line.split(",");
                    if (parts.length >= 7) {
                        String status = parts[5];
                        return status.equals("Available");
                    }
                }
            }
        }
        return false;
    }
    
    private static void deleteCurrentRecord(String menuItem, String currentRecordID) {
        String filename = getFilename(menuItem);
        try {
            String[] lines = FileManager.getLines(filename);
            if (lines == null) {
                throw new IOException("File is empty or couldn't be read");
            }
            
            StringBuilder newContent = new StringBuilder();
            boolean recordFound = false;    
            String imagePath = null;
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    if (!line.startsWith(currentRecordID + ",")) {
                        newContent.append(line).append("\n");
                    } else {
                        recordFound = true;
                        if (menuItem.equals("Car Management") && line.split(",").length > 7) {
                            imagePath = line.split(",")[7].trim();
                        }
                    }
                }
            }
            if (!recordFound) {
                JOptionPane.showMessageDialog(null,
                    "Record not found!",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            DataIO.writeFile(filename, newContent.toString().trim());
            deleteCarImage(imagePath);
            
            if (menuItem.equals("Car Management") && imagePath != null && !imagePath.isEmpty()) {
                try {
                    File imageFile = new File(new File(imagePath).getName());
                    if (imageFile.exists()) {
                        if (imageFile.delete()) {
                            System.out.println("Deleted image: " + imageFile.getPath());
                        } else {
                            System.out.println("Failed to delete image: " + imageFile.getPath());
                        }
                    }
                } catch (SecurityException ex) {
                    System.err.println("Security exception when deleting image: " + ex.getMessage());
                }
            }
            JOptionPane.showMessageDialog(null,
                "Record deleted successfully!",
                "Success", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null,
                "Error deleting record: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private static void deleteCarImage(String imagePath) {
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
}