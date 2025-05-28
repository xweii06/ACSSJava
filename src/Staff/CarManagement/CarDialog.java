package Staff.CarManagement;

import Main.Car;
import javax.swing.*;
import java.util.List; 
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.filechooser.FileNameExtensionFilter;

public class CarDialog extends JDialog {
    public static final int OK_OPTION = 1;
    public static final int CANCEL_OPTION = 0;
    
    private int option = CANCEL_OPTION;
    private final JTextField carIDField;
    private final JTextField modelField;
    private final JTextField yearField;
    private final JTextField colorField;
    private final JTextField priceField;
    private final JComboBox<String> statusComboBox;
    private JComboBox<String> assignedSMIDComboBox;
    private final JLabel imagePathLabel;
    private String imagePath;

    public CarDialog(JFrame parent, String title, Car car, CarService carService) {
        super(parent, title, true);
        this.setSize(500, 400);
        this.setLocationRelativeTo(parent);
        
        JPanel formPanel = new JPanel(new GridLayout(9, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Car ID
        formPanel.add(new JLabel("Car ID:"));
        carIDField = new JTextField(car != null ? car.getCarID() : "");
        carIDField.setEditable(car == null);
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
        formPanel.add(new JLabel("Price:"));
        priceField = new JTextField(car != null ? car.getPrice() : "");
        formPanel.add(priceField);
        
        // Status
        formPanel.add(new JLabel("Status:"));
        String[] statusOptions = {"Available", "Booked", "Paid", "Cancelled"};
        statusComboBox = new JComboBox<>(statusOptions);
        if (car != null) {
            statusComboBox.setSelectedItem(car.getStatus());
        }
        formPanel.add(statusComboBox);
        
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
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        
        okButton.addActionListener(e -> {
            option = OK_OPTION;
            dispose();
        });
        cancelButton.addActionListener(e -> {
            option = CANCEL_OPTION;
            dispose();
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
        
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            imagePath = fileChooser.getSelectedFile().getAbsolutePath();
            imagePathLabel.setText(fileChooser.getSelectedFile().getName());
        }
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
        setVisible(true);
        return option;
    }
}