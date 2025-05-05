package Staff;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import java.util.*;
import navigation.FrameManager;
import utils.DataIO;

public class AddNew {
    
    private static final String STAFF_FILE = "staff.txt", SALESMAN_FILE = "salesman.txt",
            CAR_FILE = "car.txt";
    
    private static String filename;
    private static JFrame formFrame;
    private static LinkedHashMap<String, JComponent> fields;
    private static JPasswordField pwField;
    private static JCheckBox showPW;
    private static JComboBox availabilityBox;
    private static JButton submitBtn, cancelBtn;
    private static JPanel panel;
    
    public static ActionListener addNew(String menuItem) {
        return e -> FrameManager.showFrame(formFrame(menuItem));
    }
    
    private static JFrame formFrame(String menuItem) {
        formFrame = new JFrame("Add New " + (menuItem.split(" "))[0]);
        formFrame.setLayout(new BorderLayout());
        formFrame.setSize(400, 300);

        fields = new LinkedHashMap<>();
        panel = new JPanel(new GridLayout(0,2,10,10));

        pwField = new JPasswordField();
        pwField.setPreferredSize(new Dimension(100, 25));
        pwField.setMaximumSize(new Dimension(100, 350));
        pwField.setMinimumSize(new Dimension(100, 350));
        pwField.setFont(new Font("Arial", Font.PLAIN, 14));

        showPW = new JCheckBox("Show Password");
        showPW.setFont(new Font("Arial", Font.PLAIN, 12));
        showPW.setFocusPainted(false);
        showPW.addActionListener(e -> passwordVisibility());

        availabilityBox = new JComboBox<>(new String[]{"Available", "Booked", "Paid", "Cancelled"});

        switch (menuItem) {
            case "Staff Management":
                fields.put("Staff ID",new JLabel(
                        generateID("M",getFilename(menuItem))));
                fields.put("Password",pwField);
                fields.put("Name",createTextField());
                break;

            case "Salesman Management":
                fields.put("Salesman ID",new JLabel(
                        generateID("S",getFilename(menuItem))));
                fields.put("Password",pwField);
                fields.put("Name",createTextField());
                fields.put("Phone",createTextField());
                fields.put("Email",createTextField());
                break;

            case "Car Management":
                fields.put("Car ID", createTextField());
                fields.put("Model", createTextField());
                fields.put("Price", createTextField());
                fields.put("Status", availabilityBox);
                fields.put("Assigned Salesman ID", createTextField());
                break;
        }

        for (Map.Entry<String, JComponent> entry : fields.entrySet()) {
            if (entry.getKey().equals("Assigned Salesman ID")) {
                ((JTextField) entry.getValue()).setText("S");
            }
            
            panel.add(new JLabel(entry.getKey() + ":"));
            panel.add(entry.getValue());
            if (entry.getKey().equals("Password")) {
                panel.add(new JLabel(" "));
                panel.add(showPW);
            }
        }

        submitBtn = new JButton("Submit");
        submitBtn.addActionListener(e -> processSubmission(menuItem, fields));
        cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(Color.white);
        cancelBtn.addActionListener(e -> FrameManager.goBack());

        panel.add(submitBtn);
        panel.add(cancelBtn);
        panel.setBorder(BorderFactory.createEmptyBorder(10,40,10,40));
        formFrame.add(panel,BorderLayout.CENTER);

        formFrame.pack();
        formFrame.setVisible(true);
        return formFrame;
    }

    private static String generateID(String startingLetter, String filename) {
        Set<Integer> existingIDs = new HashSet<>();
        String nextID = startingLetter + "01";

        String data = DataIO.readFile(filename);
        String[] lines = data.split("\n");
        for (String line : lines) {
            String[] parts = line.split(",");
            String id = parts[0].trim();
            if (id.startsWith(startingLetter)) {
                try {
                    int num = Integer.parseInt(id.substring(startingLetter.length()));
                    existingIDs.add(num);
                } catch (NumberFormatException e) {
                    continue;
                }
            }
        }
        for (int i = 1; i <= existingIDs.size() + 1; i++) {
            if (!existingIDs.contains(i)) {
                nextID = startingLetter + String.format("%02d", i);
                break;
            }
        }
        return nextID;
    }
    
    private static JTextField createTextField() {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(100, 25));
        field.setMaximumSize(new Dimension(100, 350));
        field.setMinimumSize(new Dimension(100, 350));
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        return field;
    }
    
    public static void passwordVisibility() {
        if (showPW.isSelected()) {
            pwField.setEchoChar((char)0);
        } else {
            pwField.setEchoChar('•'); 
        }
    }
    
    private static void processSubmission(String menuItem, LinkedHashMap<String, JComponent> fields) {
        boolean emptyField = false;
        for (JComponent field : fields.values()) {
            if (field instanceof JTextField && ((JTextField) field).getText().isEmpty()) {
                emptyField = true;
                break;
            } 
            if (field instanceof JPasswordField && ((JPasswordField) field).getPassword().length == 0) {
                emptyField = true;
                break;
            }
        }
        
        if (emptyField) {
            JOptionPane.showMessageDialog(formFrame, "All fields are required!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            switch (menuItem) {
                case "Staff Management":
                    validatePW(((JPasswordField) fields.get("Password")).getPassword());
                    validateName(((JTextField) fields.get("Name")).getText());
                    break;
                    
                case "Salesman Management":
                    validatePW(((JPasswordField) fields.get("Password")).getPassword());
                    validateName(((JTextField) fields.get("Name")).getText());
                    validatePhone(((JTextField) fields.get("Phone")).getText());
                    validateEmail(((JTextField) fields.get("Email")).getText());
                    break;

                case "Customers Management":
                    validateName(((JTextField) fields.get("Name")).getText());
                    validatePhone(((JTextField) fields.get("Phone")).getText());
                    validateEmail(((JTextField) fields.get("Email")).getText());
                    break;

                case "Car Management":
                    validateID(((JTextField) fields.get("Car ID")).getText(),"none");
                    validateModel(((JTextField) fields.get("Model")).getText());
                    validatePrice(((JTextField) fields.get("Price")).getText());
                    validateID(((JTextField) fields.get("Assigned Salesman ID")).getText(),"S");
                    break;
            }
        } catch (InvalidInputException ex) {
            JOptionPane.showMessageDialog(formFrame, ex.getMessage(), 
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String details = "";
        for (Map.Entry<String, JComponent> entry : fields.entrySet()) {
            String label = entry.getKey();
            JComponent field = entry.getValue();
            details += label + ": ";
            if (field instanceof JTextField) {
                details += ((JTextField) field).getText();
            } else if (field instanceof JPasswordField) {
                details += "********";
            } else if (field instanceof JComboBox) {
                details += ((JComboBox<?>) field).getSelectedItem();
            }
            details += "\n";   
        }

        int confirm = JOptionPane.showConfirmDialog(panel,details + "\nAre you sure?",
                "Confirmation",JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            try{
                filename = getFilename(menuItem);
                String record = buildRecord(menuItem, fields);
                DataIO.appendToFile(filename, record);
                JOptionPane.showMessageDialog(null, "Added successfully!", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                FrameManager.goBack();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error reading: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void validateID(String id, String startingLetter) throws InvalidInputException {
        if (id.length() < 3 || id.length() > 12) {
            throw new InvalidInputException("ID must be between 4 and 12 characters");
        }
        if (!id.matches("^[A-Za-z0-9]+$")) {
            throw new InvalidInputException("ID can only contain letters and numbers");
        }
        if (!id.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]+$")) {
            throw new InvalidInputException("ID must contain both letters and numbers");
        }
        if (!startingLetter.equals("none")){
            if (!id.startsWith(startingLetter)) {
                throw new InvalidInputException("ID must start with the letter " + startingLetter);
            }
        }
        //read file and check if car id dont exist and salesman id exist
    }
    
    private static void validatePW(char[] password) throws InvalidInputException {
        if (password.length < 4 || password.length > 12) {
            throw new InvalidInputException("Password must be between 4 and 12 characters");
        }
    }

    private static void validateName(String name) throws InvalidInputException {
        if (name.length() < 2 || name.length() > 20) {
            throw new InvalidInputException("Name must be between 2 and 20 characters");
        }
        if (!name.matches("^[A-Za-z ]+$")) {
            throw new InvalidInputException("Name can only contain letters and spaces");
        }
    }

    private static void validatePhone(String phone) throws InvalidInputException {
        if (!phone.matches("^[0-9]{10,15}$")) {
            throw new InvalidInputException("Phone number must be 10-15 digits");
        }
    }

    private static void validateEmail(String email) throws InvalidInputException {
        if (!email.matches("^[a-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new InvalidInputException("Please enter a valid email address");
        }
    }

    private static void validateModel(String model) throws InvalidInputException {
        if (model.length() < 2 || model.length() > 30) {
            throw new InvalidInputException("Model name must be between 2 and 30 characters");
        }
    }

    private static void validatePrice(String price) throws InvalidInputException {
        try {
            double priceValue = Double.parseDouble(price);
            if (priceValue <= 0) {
                throw new InvalidInputException("Price must be greater than 0");
            } 
            if (priceValue > 90000000) {
                throw new InvalidInputException("Price too large");
            }
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Price must be a valid number");
        }
    }

    private static class InvalidInputException extends Exception {
        public InvalidInputException(String message) {
            super(message);
        }
    }

    private static String buildRecord(String menuItem, LinkedHashMap<String, JComponent> fields) {
        ArrayList<String> data = new ArrayList<>();

        switch (menuItem) {
            case "Staff Management":
                data.add(((JLabel) fields.get("ID")).getText());
                data.add(new String(((JPasswordField) fields.get("Password")).getPassword()));
                data.add(((JTextField) fields.get("Name")).getText());
                break;

            case "Salesman Management":
                data.add(((JLabel) fields.get("ID")).getText().trim());
                data.add(new String(((JPasswordField) fields.get("Password")).getPassword()));
                data.add(((JTextField) fields.get("Name")).getText());
                data.add(((JTextField) fields.get("Phone")).getText());
                data.add(((JTextField) fields.get("Email")).getText());
                break;

            case "Car Management":
                data.add(((JTextField) fields.get("Car ID")).getText().trim());
                data.add(((JTextField) fields.get("Model")).getText());
                data.add(((JTextField) fields.get("Price")).getText());
                data.add(((JComboBox<?>) fields.get("Status")).getSelectedItem().toString());
                break;
        }
        return String.join(",", data);
    }
    
    private static String getFilename(String menuItem) {
        switch (menuItem) {
            case "Staff Management": filename = STAFF_FILE; break;
            case "Salesman Management": filename = SALESMAN_FILE; break;
            case "Car Management": filename = CAR_FILE; break;
        } return filename;
    }
}