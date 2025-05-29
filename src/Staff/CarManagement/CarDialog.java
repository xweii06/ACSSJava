package Staff.CarManagement;

import Main.Car;
import repositories.CarRepository;
import javax.swing.*;
import java.util.List; 
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.filechooser.FileNameExtensionFilter;
import utils.DataIO;
import utils.InvalidInputException;

public class CarDialog extends JDialog {
    public static final int OK_OPTION = 1;
    public static final int CANCEL_OPTION = 0;
    private int option = CANCEL_OPTION;
    private final boolean statusOnlyMode;
    
    private final JTextField carIDField;
    private final JTextField modelField;
    private final JTextField yearField;
    private final JTextField colorField;
    private final JTextField priceField;
    private JComboBox<String> statusComboBox;
    private JComboBox<String> assignedSMIDComboBox;
    private final JLabel imagePathLabel;
    private String imagePath;

    public CarDialog(JFrame parent, String title, Car car, CarService carService, boolean statusOnlyMode) {
        super(parent, title, true);
        this.setSize(500, 400);
        this.setLocationRelativeTo(null);
        
        this.statusOnlyMode = statusOnlyMode;
        
        JPanel formPanel = new JPanel(new GridLayout(9, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Car ID
        formPanel.add(new JLabel("Car ID:"));
        carIDField = new JTextField();
        carIDField.setEditable(false);
        
        try {
            if (car == null) {
                // Generate new ID for new cars
                carIDField.setText(carService.generateNewCarID());
            } else {
                // Use existing ID for editing
                carIDField.setText(car.getCarID());
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                "Error generating car ID: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            carIDField.setText("ERROR");
        }
        
        formPanel.add(carIDField);
        
        // Model
        formPanel.add(new JLabel("Model:"));
        modelField = new JTextField(car != null ? car.getModel() : "");
        formPanel.add(modelField);
        
        // Year
        formPanel.add(new JLabel("Year:"));
        yearField = new JTextField(car != null ? car.getYear() : "");
        formPanel.add(yearField);
        
        // Color
        formPanel.add(new JLabel("Color:"));
        colorField = new JTextField(car != null ? car.getColor() : "");
        formPanel.add(colorField);
        
        // Price
        formPanel.add(new JLabel("Price(RM):"));
        priceField = new JTextField(car != null ? car.getPrice() : "");
        formPanel.add(priceField);
        
        // Status
        formPanel.add(new JLabel("Status:"));
        if (car == null) {
            formPanel.add(new JLabel("available"));
        }
        if (car != null) {
            String[] statusOptions = {"available", "booked", "paid", "cancelled"};
            statusComboBox = new JComboBox<>(statusOptions);
            statusComboBox.setSelectedItem(car.getStatus());
            formPanel.add(statusComboBox);
        }
        
        // Assigned Salesman ID
        formPanel.add(new JLabel("Salesman ID:"));
        try {
            List<String> salesmanIDs = carService.getAllSalesmanIDs();
            assignedSMIDComboBox = new JComboBox<>(salesmanIDs.toArray(new String[0]));
            if (car != null && car.getAssignedSalesmanID() != null) {
                assignedSMIDComboBox.setSelectedItem(car.getAssignedSalesmanID());
            }
        } catch (IOException ex) {
            assignedSMIDComboBox = new JComboBox<>(new String[]{" "});
            JOptionPane.showMessageDialog(this, "Error loading salesman IDs: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        formPanel.add(assignedSMIDComboBox);
        
        // Image Path
        formPanel.add(new JLabel("Image:"));
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePathLabel = new JLabel(car != null && car.getImagePath() != null ? 
            new File(car.getImagePath()).getName() : "No image selected");
        JButton browseButton = new JButton("Browse");
        browseButton.addActionListener(this::chooseImage);
        imagePanel.add(imagePathLabel, BorderLayout.CENTER);
        imagePanel.add(browseButton, BorderLayout.EAST);
        formPanel.add(imagePanel);
        
        if (car != null) {
            imagePath = car.getImagePath();
        }
        
        if (statusOnlyMode) {
            modelField.setEnabled(false);
            yearField.setEnabled(false);
            colorField.setEnabled(false);
            priceField.setEnabled(false);
        }
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        
        okButton.addActionListener(e -> {
            try {
                carService.validateCar(getCar()); // validate only
                option = OK_OPTION;
                this.dispose(); // dispose only after everything's validated
            } catch (InvalidInputException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
            } 
        }); 
        
        cancelButton.addActionListener(e -> {
            option = CANCEL_OPTION;
            this.dispose();
        });
        
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        
        this.add(formPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void chooseImage(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Image files", "jpg", "jpeg", "png", "gif");
        fileChooser.setFileFilter(filter);
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                CarRepository.validateImageFile(selectedFile);
                String extension = getFileExtension(selectedFile.getName());
                imagePath = DataIO.saveCarImage(selectedFile, carIDField.getText(),extension);
                imagePathLabel.setText(selectedFile.getName());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Image Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public static String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }

        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < filename.length() - 1) {
            return filename.substring(dotIndex + 1).toLowerCase();
        }
        return "";
    }
    
    public Car getNewCar() {
        return new Car(
                carIDField.getText().trim(),
                modelField.getText().trim(),
                yearField.getText().trim(),
                colorField.getText().trim(),
                priceField.getText().trim(),
                "available", // always available when adding new
                (String) assignedSMIDComboBox.getSelectedItem(),
                imagePath
        );
    }
    
    public Car getCar() {
        return new Car(
            carIDField.getText().trim(),
            modelField.getText().trim(),
            yearField.getText().trim(),
            colorField.getText().trim(),
            priceField.getText().trim(),
            (String) statusComboBox.getSelectedItem(),
            (String) assignedSMIDComboBox.getSelectedItem(),
            imagePath
        );
    }
    
    public int showDialog() {
        this.setVisible(true);
        return option;
    }
}