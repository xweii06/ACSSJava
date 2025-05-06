package Staff;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.Border;
import navigation.FrameManager;
import utils.DataIO;

public class DeleteRecords {
    
    private static final String STAFF_FILE = "staff.txt", SALESMAN_FILE = "salesman.txt",
            CUSTOMER_FILE = "customers.txt", CAR_FILE = "car.txt";
    
    private static JFrame deleteFrame;
    private static JPanel panel, formPanel, buttonPanel, searchInputPanel, previewPanel;
    private static JTextField idField;
    private static JTextArea previewArea;
    private static JButton searchBtn, deleteBtn, cancelBtn;
    
    public static ActionListener deleteRecords(String menuItem) {
        return e -> FrameManager.showFrame(createDeleteFrame(menuItem));
        }
    
    private static String getFilename(String menuItem) {
        switch (menuItem) {
            case "Staff Management": return STAFF_FILE;
            case "Salesman Management": return SALESMAN_FILE;
            case "Customer Management" : return CUSTOMER_FILE;
            case "Car Management": return CAR_FILE;
            default:
                throw new IllegalArgumentException("Unknown menu item: " + menuItem);
        }
    }
    
    
    private static JFrame createDeleteFrame(String menuItem) {
        deleteFrame = new JFrame("Delete " + (menuItem.split(" "))[0]);
        deleteFrame.setLayout(new BorderLayout());
        deleteFrame.setSize(400, 300);
        deleteFrame.setResizable(false);
        
        panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        formPanel = new JPanel(new BorderLayout(10, 10));
        
        searchInputPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        searchInputPanel.add(new JLabel("Enter ID:"));
        idField = new JTextField();
        searchInputPanel.add(idField);
        
        searchBtn = new JButton("Search");
        searchBtn.setFocusable(false);
        searchBtn.addActionListener(e -> searchRecord(menuItem));
        searchInputPanel.add(searchBtn);
        
        previewPanel = new JPanel(new BorderLayout());
        previewPanel.setBorder(BorderFactory.createTitledBorder("Record Preview"));
        previewArea = new JTextArea();
        previewArea.setEditable(false);
        previewArea.setFont(new Font("Arial", Font.PLAIN, 12));
        previewPanel.add(new JScrollPane(previewArea), BorderLayout.CENTER);
        previewPanel.setVisible(false);
        
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        Border border = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.black,1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10));
        deleteBtn = new JButton("Delete Record");
        deleteBtn.setBackground(Color.gray); // set gray as default
        deleteBtn.setSize(new Dimension(100,30));
        deleteBtn.setForeground(Color.white);
        deleteBtn.setEnabled(false);
        deleteBtn.setFocusable(false);
        
        cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(new Color(0x999999));
        cancelBtn.setSize(new Dimension(100,30));
        cancelBtn.setForeground(Color.white);
        cancelBtn.setFocusable(false);
        cancelBtn.addActionListener(e -> FrameManager.goBack());
        
        buttonPanel.add(deleteBtn);
        buttonPanel.add(cancelBtn);
        
        formPanel.add(searchInputPanel, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        formPanel.add(previewPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        deleteFrame.add(panel);
        return deleteFrame;
    }
    
    private static void searchRecord(String menuItem) {
        String searchID = idField.getText().trim();
        
        if (searchID == null || searchID.isEmpty()) {
            JOptionPane.showMessageDialog(deleteFrame, 
                "Please enter an ID", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String id = searchID.toUpperCase();
        String filename = getFilename(menuItem);
        String data = DataIO.readFile(filename);
        String[] lines = data.split("\n");
        boolean found = false;
        for (String line : lines) {
            if (line.startsWith(id + ",")) {
                String currentRecordID = id;
                displayRecordPreview(menuItem, line, currentRecordID);
                found = true;
                break;
            }
        }
        if (!found) {
            previewPanel.setVisible(false);
            deleteBtn.setEnabled(false);
            JOptionPane.showMessageDialog(deleteFrame,
                    "No record found with ID: " + searchID,
                    "Not Found", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private static void displayRecordPreview(String menuItem, String line, String currentRecordID) {
        String[] parts = line.split(",");
        StringBuilder previewText = new StringBuilder();
        
        switch (menuItem) {
            case "Staff Management":
                previewText.append("Staff ID: ").append(parts[0]).append("\n");
                previewText.append("Name: ").append(parts[2]).append("\n");
                break;
            case "Salesman Management":
                previewText.append("Salesman ID: ").append(parts[0]).append("\n");
                previewText.append("Name: ").append(parts[2]).append("\n");
                previewText.append("Phone: ").append(parts[3]).append("\n");
                previewText.append("Email: ").append(parts[4]).append("\n");
                break;
            case "Customer Management":
                previewText.append("Customer ID: ").append(parts[0]).append("\n");
                previewText.append("Name: ").append(parts[1]).append("\n");
                previewText.append("Phone: ").append(parts[2]).append("\n");
                previewText.append("Email: ").append(parts[3]).append("\n");
                break;
            case "Car Management":
                previewText.append("Car ID: ").append(parts[0]).append("\n");
                previewText.append("Model: ").append(parts[1]).append("\n");
                previewText.append("Year: ").append(parts[2]).append("\n");
                previewText.append("Price(RM): ").append(parts[3]).append("\n");
                previewText.append("Status: ").append(parts[4]).append("\n");
                previewText.append("Assigned Salesman ID: ").append(parts[5]).append("\n");
                break;
        }
        
        previewArea.setText(previewText.toString());
        previewPanel.setVisible(true);
        deleteBtn.setBackground(new Color(0xE31A3E)); 
        deleteBtn.addActionListener(e -> confirmDelete(menuItem, currentRecordID));
        deleteBtn.setEnabled(true);
    }
    
    private static void confirmDelete(String menuItem, String currentRecordID) {
        switch (menuItem) {
            case "Staff Management":
                String superAdminID = getSuperAdminID();
                if (!(superAdminID == null)) {
                    if (currentRecordID.equals(superAdminID)) {
                        JOptionPane.showMessageDialog(deleteFrame,
                        "Cannot delete super admin.",
                        "Action blocked", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
            case "Salesman Management":
                if (!reassignCarsBeforeDeletion(currentRecordID)) {
                    return;
                }
                break;
            case "Customer Management":
                // maybe send a noti to that cus? idk
                break;
            case "Car Management":
                // status cannot be booked
                if (!isAvailable(currentRecordID)) {
                    JOptionPane.showMessageDialog(deleteFrame,
                        "Cannot delete unavailable car.",
                        "Action blocked", JOptionPane.WARNING_MESSAGE);
                    return;
                } 
                break;
        }
        
        int confirm = JOptionPane.showConfirmDialog(deleteFrame,
            "Are you sure you want to delete this record?\n\n",
            "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            deleteCurrentRecord(menuItem, currentRecordID);
        }
    }
    
    private static String getSuperAdminID() {
        String data = DataIO.readFile(STAFF_FILE);
        String[] lines = data.split("\n");
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length >= 3) {
                String id = parts[0];
                if (!id.startsWith("M")) {
                    return id;
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
                
                // default:
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
                JOptionPane.showMessageDialog(deleteFrame,
                    "Reassigned all " + successfullyReassigned + " cars",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                return true;
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(deleteFrame,
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
                if (parts.length >= 6 && parts[5].trim().equals(currentRecordID)) {
                    cars.add(parts);
                }
            }
        }
        return cars;
    }

    private static HashMap<String, String> getAvailableSalesmen(String currentRecordID) throws IOException {
        HashMap<String, String> salesmen = new HashMap<>();
        String data = DataIO.readFile(SALESMAN_FILE);
        String[] lines = data.split("\n");

        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                String[] parts = line.split(",");
                if (parts.length >= 3 && !parts[0].equals(currentRecordID)) {
                    salesmen.put(parts[0], parts[2]); // get id and name
                }
            }
        }
        return salesmen;
    }
    
    private static String[] createDropdownOptions(HashMap<String, String> salesmen) {
        String[] options = new String[salesmen.size()];
        int i = 1;
        for (Map.Entry<String, String> entry : salesmen.entrySet()) {
            options[i++] = entry.getKey() + " - " + entry.getValue();
        }
        return options;
    }

    private static String showDropdownDialog(String carId, String carModel, String[] options) {
        // Create panel with car info
        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.add(new JLabel("Reassign Car: " + carId + " (" + carModel + ")"));

        // Create dropdown
        JComboBox<String> dropdown = new JComboBox<>(options);
        dropdown.setSelectedIndex(0);
        panel.add(dropdown);

        int result = JOptionPane.showConfirmDialog(deleteFrame,
            panel,
            "Select New Salesman",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE);

        return (result == JOptionPane.OK_OPTION) ? 
            (String) dropdown.getSelectedItem() : 
            null;
    }

    private static boolean reassignSingleCar(String carID, String newSalesmanID) throws IOException {
        String data = DataIO.readFile(CAR_FILE);
        String[] lines = data.split("\n");
        StringBuilder newData = new StringBuilder();
        boolean found = false;

        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                String[] parts = line.split(",");
                if (parts[0].equals(carID)) {
                    parts[5] = newSalesmanID;
                    found = true;
                }
                newData.append(String.join(",", parts)).append("\n");
            }
        }
        if (found) {
            DataIO.writeFile(CAR_FILE, newData.toString().trim());
            return true;
        }
        return false;
    }
    
    // need change in sales.txt as well !!!
    
    private static boolean isAvailable(String id) {
        String data = DataIO.readFile(CAR_FILE);
        String[] lines = data.split("\n");
        for (String line : lines) {
            if (!line.startsWith(id + ",")) {
                String parts[] = line.split(",");
                if (parts.length >= 4) {
                    String status = parts[4];
                    return !(status.equals("Booked"));
                }
            }
        }
        return false;
    }
    
    private static void deleteCurrentRecord(String menuItem, String currentRecordID) {
        String filename = getFilename(menuItem);
        try {
            String data = DataIO.readFile(filename);
            if (data == null || data.isEmpty()) {
                throw new IOException("File is empty or couldn't be read");
            }
            
            StringBuilder newContent = new StringBuilder();
            String[] lines = data.split("\n");
            boolean recordFound = false;    
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    if (!line.startsWith(currentRecordID + ",")) {
                        newContent.append(line).append("\n");
                    } else {
                        recordFound = true;
                    }
                }
            }

            if (!recordFound) {
                JOptionPane.showMessageDialog(deleteFrame,
                    "Record not found!",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            DataIO.writeFile(filename, newContent.toString().trim());
            JOptionPane.showMessageDialog(deleteFrame,
                "Record deleted successfully!",
                "Success", JOptionPane.INFORMATION_MESSAGE);
            
            idField.setText("");
            previewArea.setText("");
            previewPanel.setVisible(false);
            deleteBtn.setEnabled(false);
            FrameManager.goBack();
            
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(deleteFrame,
                "Error deleting record: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}