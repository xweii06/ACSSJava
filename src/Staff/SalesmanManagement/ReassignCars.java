package Staff.SalesmanManagement;

import repositories.SalesmanRepository;
import java.awt.*;
import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReassignCars {
    private final SalesmanRepository salesmanRepository;

    public ReassignCars() {
        this.salesmanRepository = new SalesmanRepository();
    }

    public boolean reassignCarsBeforeDeletion(String salesmanID) {
        try {
            ArrayList<String[]> assignedCars = salesmanRepository.getAssignedCars(salesmanID);
            if (assignedCars.isEmpty()) {
                return true;
            }

            HashMap<String, String> availableSalesmen = salesmanRepository.getAvailableSalesmen(salesmanID);
            if (availableSalesmen == null || availableSalesmen.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                    "No available salesmen to reassign cars to",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            String[] options = createDropdownOptions(availableSalesmen);
            int successfullyReassigned = 0;
            
            for (String[] car : assignedCars) {
                String carID = car[0];
                String model = car[1];
                String status = car[5];
                
                if (!status.equals("Paid")) {
                    String selected = showDropdownDialog(carID, model, options);
                    if (selected == null) {
                        break; // User cancelled
                    }
                    String newSalesmanID = selected.split(" - ")[0];
                    salesmanRepository.reassignCar(carID, newSalesmanID);
                    successfullyReassigned++;
                } else {
                    successfullyReassigned++;
                }
            }
            if (successfullyReassigned == assignedCars.size()) {
                JOptionPane.showMessageDialog(null,
                    "Reassigned all " + successfullyReassigned + " cars",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                return true;
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null,
                "Error during reassignment: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    private String[] createDropdownOptions(HashMap<String, String> salesmen) {
        String[] options = new String[salesmen.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : salesmen.entrySet()) {
            options[i++] = entry.getKey() + " - " + entry.getValue();
        }
        return options;
    }

    private String showDropdownDialog(String carID, String carModel, String[] options) {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.add(new JLabel("Reassign Car: " + carID + " (" + carModel + ")"));

        JComboBox<String> dropdown = new JComboBox<>(options);
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
}