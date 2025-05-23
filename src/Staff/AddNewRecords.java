package Staff;

import Car.Car;
import Salesman.Salesman;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.Year;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import utils.DataIO;

public class AddNewRecords {
    
    private static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024; // 5MB max size
    private static final String[] ALLOWED_EXTENSIONS = {"jpg", "jpeg", "png"};
    
    private static JFrame frame;
    private static LinkedHashMap<String, JComponent> fields;
    private static JPasswordField pwField;
    private static JCheckBox showPW;
    private static JLabel imageLabel;
    private static JButton submitBtn, cancelBtn, uploadBtn;
    private static JPanel panel;
    
    public static JFrame createAddFrame(String menuItem) {
        frame = new JFrame("Add New " + (menuItem.split(" "))[0]);
        frame.setLayout(new BorderLayout());
        frame.setSize(400, 300);
        frame.setResizable(false);

        fields = new LinkedHashMap<>();
        panel = new JPanel(new GridLayout(0,2,10,10));
        panel.setBorder(BorderFactory.createEmptyBorder(10,40,10,40));

        pwField = new JPasswordField();
        pwField.setPreferredSize(new Dimension(100, 25));
        pwField.setMaximumSize(new Dimension(100, 350));
        pwField.setMinimumSize(new Dimension(100, 350));
        pwField.setFont(new Font("Arial", Font.PLAIN, 14));

        showPW = new JCheckBox("Show Password");
        showPW.setFont(new Font("Arial", Font.PLAIN, 12));
        showPW.setFocusPainted(false);
        showPW.addActionListener(e -> passwordVisibility());

        switch (menuItem) {
            case "Staff Management":
                fields.put("Staff ID",new JLabel(
                        generateID("M",FileManager.getFilename(menuItem))));
                fields.put("Password",pwField);
                fields.put("Name",createTextField());
                fields.put("Role",new JLabel("Staff"));
                break;

            case "Salesman Management":
                fields.put("Salesman ID",new JLabel(
                        generateID("S",FileManager.getFilename(menuItem))));
                fields.put("Password",pwField);
                fields.put("Name",createTextField());
                fields.put("Phone",createTextField());
                fields.put("Email",createTextField());
                break;

            case "Car Management":
                fields.put("Car ID",new JLabel(
                        generateID("CAR",FileManager.getFilename(menuItem))));
                fields.put("Model", createTextField());
                fields.put("Year", createTextField());
                fields.put("Color", createTextField());
                fields.put("Price", createTextField());
                fields.put("Status", new JLabel("Available"));
                fields.put("Assigned Salesman ID", createTextField());
                uploadBtn = new JButton("Upload Image");
                uploadBtn.setFocusable(false);
                imageLabel = new JLabel("No image selected");
                fields.put("Image", imageLabel);
                uploadBtn.addActionListener(e -> handleImageUpload(imageLabel));
                break;
        }

        for (Map.Entry<String, JComponent> entry : fields.entrySet()) {
            if (entry.getKey().equals("Assigned Salesman ID")) {
                ((JTextField) entry.getValue()).setText("S");
            }
            if (entry.getKey().equals("Price")) {
                panel.add(new JLabel(entry.getKey() + "(RM):"));
            } else if (entry.getKey().equals("Image")) {
                panel.add(new JLabel(entry.getKey() + ":"));
                panel.add(uploadBtn);
                panel.add(new JLabel("Selected Image:"));
                panel.add(imageLabel);
            }
            else {
                panel.add(new JLabel(entry.getKey() + ":"));
            }
            panel.add(entry.getValue());
            if (entry.getKey().equals("Password")) {
                panel.add(new JLabel(" "));
                panel.add(showPW);
            }
        }

        submitBtn = new JButton("Submit");
        submitBtn.setBackground(new Color(0x08A045));
        submitBtn.setForeground(Color.white);
        submitBtn.addActionListener(e -> processSubmission(menuItem, fields));
        
        cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(new Color(0x999999));
        cancelBtn.setForeground(Color.white);
        cancelBtn.addActionListener(e -> {
            frame.dispose();
            StaffMenu.refreshMenu(menuItem); 
        });
        
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                StaffMenu.refreshMenu(menuItem);
            }
        });
        
        panel.add(submitBtn);
        panel.add(cancelBtn);
        frame.add(panel,BorderLayout.CENTER);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        return frame;
    }

    private static String generateID(String startingLetter, String filename) {
        Set<Integer> existingIDs = new HashSet<>();
        String id = startingLetter + "01";

        try {
            String data = DataIO.readFile(filename);
            if (data != null && !data.trim().isEmpty()) {
                String[] lines = data.split("\n");
                for (String line : lines) {
                    if (!line.trim().isEmpty()) {
                        String[] parts = line.split(",");
                        if (parts.length > 0) {
                            String savedID = parts[0].trim();
                            if (savedID.startsWith(startingLetter)) {
                                try {
                                    String numStr = savedID.substring(startingLetter.length());
                                    int num = Integer.parseInt(numStr);
                                    existingIDs.add(num);
                                } catch (NumberFormatException ex) {
                                    continue;
                                }
                            }
                        }
                    }
                }
            }
            int nextNum = 1;
            while (existingIDs.contains(nextNum)) {
                nextNum++;
            }
            id = startingLetter + String.format("%02d", nextNum);
            
        } catch (Exception ex) { 
            System.err.println("Error reading IDs: " + ex.getMessage());
        }
        return id;
    }
    
    private static JTextField createTextField() {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(100, 25));
        field.setMaximumSize(new Dimension(100, 350));
        field.setMinimumSize(new Dimension(100, 350));
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        return field;
    }
    
    private static void passwordVisibility() {
        if (showPW.isSelected()) {
            pwField.setEchoChar((char)0);
        } else {
            pwField.setEchoChar('•'); 
        }
    }
    
    private static void handleImageUpload(JLabel imageLabel) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Car Image");

        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Image files", ALLOWED_EXTENSIONS);
        fileChooser.setFileFilter(filter);

        int returnValue = fileChooser.showOpenDialog(frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            try {
                validateImageFile(selectedFile);
                imageLabel.setText(selectedFile.getName());
                imageLabel.putClientProperty("selectedFile", selectedFile);

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), 
                    "Upload Error", JOptionPane.ERROR_MESSAGE);
                imageLabel.setText("No image selected");
                imageLabel.putClientProperty("selectedFile", null);
            }
        }
    }
    
    private static void validateImageFile(File file) throws IOException {
        if (!file.exists()) {
            throw new IOException("Selected file does not exist");
        }
        if (file.length() > MAX_IMAGE_SIZE) {
            throw new IOException("Image exceeds maximum size of " + 
                (MAX_IMAGE_SIZE/1024/1024) + "MB");
        }
        String extension = getFileExtension(file.getName());
        boolean valid = false;
        for (String ext : ALLOWED_EXTENSIONS) {
            if (ext.equalsIgnoreCase(extension)) {
                valid = true;
                break;
            }
        }
        if (!valid) {
            throw new IOException("Only JPEG, JPG, PNG images are allowed");
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
            JOptionPane.showMessageDialog(frame, "All fields are required!", 
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
                    validatePhone(((JTextField) fields.get("Phone")).getText(), menuItem);
                    validateEmail(((JTextField) fields.get("Email")).getText(), menuItem);
                    break;

                case "Car Management":
                    validateModel(((JTextField) fields.get("Model")).getText());
                    validateYear(((JTextField) fields.get("Year")).getText());
                    validateColor(((JTextField) fields.get("Color")).getText());
                    validatePrice(((JTextField) fields.get("Price")).getText());
                    validateID(((JTextField) fields.get("Assigned Salesman ID")).getText(),menuItem);
                    break;
            }
        } catch (InvalidInputException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), 
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String details = "";
        for (Map.Entry<String, JComponent> entry : fields.entrySet()) {
            String label = entry.getKey();
            JComponent field = entry.getValue();
            details += label + ": ";
            if (label.equals("Password")) {
                details += "********";
            } else if (field instanceof JTextField) {
                details += ((JTextField) field).getText();
            } else if (field instanceof JLabel) {
                details += ((JLabel) field).getText();
            }
            details += "\n";  
        }

        int confirm = JOptionPane.showConfirmDialog(panel,details + "\nAre you sure?",
                "Confirmation",JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            try{
                String filename = FileManager.getFilename(menuItem);
                String record = buildRecord(menuItem, fields);
                DataIO.appendToFile(filename, record);
                JOptionPane.showMessageDialog(null, "Added successfully!", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFormFields();
                frame.dispose();
                StaffMenu.refreshMenu(menuItem); 
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error reading: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
        
    private static void validateID(String menuItem, String id) throws InvalidInputException {
        if (id.length() < 3 || id.length() > 12) {
            throw new InvalidInputException("ID must be between 4 and 12 characters");
        }
        if (!id.matches("^[A-Za-z0-9]+$")) {
            throw new InvalidInputException("ID can only contain letters and numbers");
        }
        if (!id.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]+$")) {
            throw new InvalidInputException("ID must contain both letters and numbers");
        }
        // check if it is a valid salesman ID in car management
        if (menuItem.equals("Car Management")) {
            String data = DataIO.readFile(FileManager.SALESMAN_FILE);
            if (!data.contains(id)) {
                throw new InvalidInputException("Salesman ID don't exist.");
            }
        } 
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
    
    private static String parsePhoneFormat(String phone) {
        // remove spaces and dashes
        phone = phone.replaceAll("[\\s\\-]", "");
        
        // Convert local format to international (+60)
        if (phone.matches("^0[1-9][0-9]{7,9}$")) {
            phone = "+60" + phone.substring(1); 
        } else if (phone.matches("^60[1-9][0-9]{7,9}$")) {
            phone = "+" + phone;  
        } 
        return phone;
    }
    
    private static void validatePhone(String phone, String menuItem) throws InvalidInputException {
        String newPhone = parsePhoneFormat(phone);
        if (!newPhone.matches("^\\+60(1[0-9]{8,9}|[3-9][0-9]{7,8})$")) {
            throw new InvalidInputException(
                    "Please enter a valid Malaysian phone number.");
        }
        if (menuItem.equals("Salesman Management")) {
            String data = DataIO.readFile(FileManager.SALESMAN_FILE);
            if (data.contains(newPhone)) {
                throw new InvalidInputException("Phone number already exist.");
            }
        }
    }

    private static void validateEmail(String email, String menuItem) throws InvalidInputException {
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new InvalidInputException("Invalid email format");
        }
        if (menuItem.equals("Salesman Management")) {
            String data = DataIO.readFile(FileManager.SALESMAN_FILE);
            if (data.contains(email)) {
                throw new InvalidInputException("Email already exist.");
            }
        }
    }

    private static void validateModel(String model) throws InvalidInputException {
        if (model.length() < 2 || model.length() > 30) {
            throw new InvalidInputException("Model name must be between 2 and 30 characters");
        }
    }
    
    private static void validateYear(String inputYear) throws InvalidInputException {
        try {
            int year = Integer.parseInt(inputYear);
            if (year < 2000 || year > Year.now().getValue()) {
                throw new InvalidInputException("Invalid year");
            }
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Year must be a number");
        }
    }
    
    private static void validateColor(String color) throws InvalidInputException {
        if (!color.matches("^[a-zA-Z ]+$")) {
            throw new InvalidInputException("Color can only contain letters and spaces");
        }

        if (color.length() < 2 || color.length() > 20) {
            throw new InvalidInputException("Color must be between 2 and 20 characters");
        }
    }
    
    private static void validatePrice(String price) throws InvalidInputException {
        try {
            double priceValue = Double.parseDouble(price);
            if (priceValue <= 0) {
                throw new InvalidInputException("Price must be greater than 0");
            } 
            if (priceValue > 90000000 || priceValue < 1000) {
                throw new InvalidInputException("Invalid price");
            }
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Price must be a valid number without letters");
        }
    }

    private static class InvalidInputException extends Exception {
        public InvalidInputException(String message) {
            super(message);
        }
    }

    private static String buildRecord(String menuItem, LinkedHashMap<String, JComponent> fields) throws IOException {
        switch (menuItem) {
            case "Staff Management":
                String staffID = ((JLabel) fields.get("Staff ID")).getText();
                String staffName = ((JTextField) fields.get("Name")).getText();
                String staffPW = new String(((JPasswordField) fields.get("Password")).getPassword());
                String role = ((JLabel) fields.get("Role")).getText();
                Staff newStaff = new Staff(staffID, staffName, role, staffPW);
                return newStaff.toDataString();

            case "Salesman Management":
                String salesmanID = ((JLabel) fields.get("Salesman ID")).getText();
                String salesmanName = ((JTextField) fields.get("Name")).getText();
                String salesmanPhone= ((JTextField) fields.get("Phone")).getText();
                String newSMPhone = parsePhoneFormat(salesmanPhone);
                String salesmanEmail = ((JTextField) fields.get("Email")).getText().toLowerCase();
                String salesmanPW = new String(((JPasswordField) fields.get("Password")).getPassword());
                Salesman newSalesman = new Salesman(
                        salesmanID, salesmanName, newSMPhone, salesmanEmail,salesmanPW);
                return newSalesman.toDataString();

            case "Car Management":
                String carID = ((JLabel) fields.get("Car ID")).getText();
                String model = ((JTextField) fields.get("Model")).getText();
                String year = ((JTextField) fields.get("Year")).getText();
                String color = ((JTextField) fields.get("Color")).getText();
                String price = ((JTextField) fields.get("Price")).getText();
                String status = ((JLabel) fields.get("Status")).getText();
                String assignedSM= ((JTextField) fields.get("Assigned Salesman ID")).getText();
                
                JLabel imageLabel = (JLabel) fields.get("Image");
                File selectedFile = (File) imageLabel.getClientProperty("selectedFile");
                String imagePath = " "; // empty string if no image
                if (selectedFile != null) {
                    try {
                        String fileExtension = getFileExtension(selectedFile.getName());
                        imagePath = DataIO.saveCarImage(
                                selectedFile, carID, fileExtension);
                    } catch (IOException ex) {
                        System.err.println("Error saving image: " + ex.getMessage());
                        JOptionPane.showMessageDialog(frame, 
                            "Car record saved but image upload failed: " + ex.getMessage(),
                            "Image Upload Error", JOptionPane.WARNING_MESSAGE);
                    }
                }
                Car newCar = new Car(carID, model, year, color, price, status, assignedSM, imagePath);
                return newCar.toDataString();

            default:
                throw new IllegalArgumentException("Unknown menu item: " + menuItem);
        }
    }
    
    private static String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }

        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < filename.length() - 1) {
            return filename.substring(dotIndex + 1).toLowerCase();
        }
        return "";
    }
    
    private static void clearFormFields() {
        for (JComponent field : fields.values()) {
            if (field instanceof JTextField) {
                ((JTextField) field).setText("");
            } else if (field instanceof JPasswordField) {
                ((JPasswordField) field).setText("");
            }
        }
    }
}