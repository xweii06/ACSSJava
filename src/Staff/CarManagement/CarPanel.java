package Staff.CarManagement;

import Main.Car;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class CarPanel extends JPanel {
    private final CarService service;
    private JTable carTable;
    private CarTableModel tableModel;
    //private JTextField searchField;
    
    public CarPanel(CarService service) {
        this.service = service;
        initializeUI();
        loadCarData();
    }
    
    private void initializeUI() {
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Search Panel
        
        
        // Toolbar
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        
        JButton addButton = new JButton("Add");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        JButton viewImageButton = new JButton("View Image");
        JButton refreshButton = new JButton("Refresh");
        
        addButton.addActionListener(this::addCar);
        editButton.addActionListener(this::editCar);
        deleteButton.addActionListener(this::deleteCar);
        viewImageButton.addActionListener(this::viewImage);
        refreshButton.addActionListener(e -> loadCarData());
        
        toolBar.add(addButton);
        toolBar.add(editButton);
        toolBar.add(deleteButton);
        toolBar.add(viewImageButton);
        toolBar.addSeparator();
        toolBar.add(refreshButton);
        
        // Main panel
        JPanel topPanel = new JPanel(new BorderLayout());
        //topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(toolBar, BorderLayout.SOUTH);
        
        this.add(topPanel, BorderLayout.NORTH);
        
        // Table
        tableModel = new CarTableModel();
        carTable = new JTable(tableModel);
        carTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        this.add(new JScrollPane(carTable), BorderLayout.CENTER);
    }
    
    private void loadCarData() {
        try {
            List<Car> cars = service.getAllCars();
            tableModel.setCars(cars);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading car data: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void addCar(ActionEvent e) {
        try {
            CarDialog dialog = new CarDialog(
                (JFrame)SwingUtilities.getWindowAncestor(this), 
                    "Add New Car",null,service);
            
            if (dialog.showDialog() == CarDialog.OK_OPTION) {
                Car newCar = dialog.getCar();
                service.addCar(newCar);
                loadCarData();
                JOptionPane.showMessageDialog(this, "Car added successfully", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void editCar(ActionEvent e) {
        int selectedRow = carTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a car to edit", 
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            Car selectedCar = tableModel.getCarAt(selectedRow);
            CarDialog dialog = new CarDialog(
                (JFrame)SwingUtilities.getWindowAncestor(this), "Edit Car", 
                    selectedCar, service);
            
            if (dialog.showDialog() == CarDialog.OK_OPTION) {
                Car updatedCar = dialog.getCar();
                service.updateCar(updatedCar);
                loadCarData();
                JOptionPane.showMessageDialog(this, "Car updated successfully", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteCar(ActionEvent e) {
        int selectedRow = carTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a car to delete", 
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            Car selectedCar = tableModel.getCarAt(selectedRow);
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Are you sure you want to delete " + selectedCar.getModel() + "?", 
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                service.deleteCar(selectedCar.getCarID());
                loadCarData();
                JOptionPane.showMessageDialog(this, "Car deleted successfully", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void viewImage(ActionEvent e) {
        int selectedRow = carTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a car to view image", 
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            Car selectedCar = tableModel.getCarAt(selectedRow);
            showImageInFrame(selectedCar);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error viewing image: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showImageInFrame(Car car) {
        String imagePath = car.getImagePath();
        
        if (imagePath == null || imagePath.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No image available for this car", 
                "Image Not Found", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            JFrame imageFrame = new JFrame("Car Image - " + car.getCarID());
            imageFrame.setSize(600, 400);
            imageFrame.setLocationRelativeTo(null);
            
            ImageIcon originalIcon = new ImageIcon(imagePath);
            Image originalImage = originalIcon.getImage();
            int maxWidth = 550;
            int maxHeight = 350;

            int originalWidth = originalIcon.getIconWidth();
            int originalHeight = originalIcon.getIconHeight();

            double widthRatio = (double)maxWidth / originalWidth;
            double heightRatio = (double)maxHeight / originalHeight;
            double ratio = Math.min(widthRatio, heightRatio);

            int scaledWidth = (int)(originalWidth * ratio);
            int scaledHeight = (int)(originalHeight * ratio);
            
            Image scaledImage = originalImage.getScaledInstance(
                scaledWidth, scaledHeight, Image.SCALE_SMOOTH);

            JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
            imageLabel.setHorizontalAlignment(JLabel.CENTER);
            imageLabel.setVerticalAlignment(JLabel.CENTER);
            imageFrame.add(imageLabel); 
            imageFrame.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Failed to load image: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private static class CarTableModel extends AbstractTableModel {
        private List<Car> cars;
        private final String[] columnNames = {
            "Car ID", "Model", "Year", "Color", "Price", "Status", "Salesman ID"
        };
        
        public CarTableModel() {
            this.cars = new ArrayList<>();
        }
        
        public void setCars(List<Car> cars) {
            this.cars = cars;
            fireTableDataChanged();
        }
        
        public Car getCarAt(int row) {
            return cars.get(row);
        }
        
        @Override
        public int getRowCount() { return cars.size(); }
        
        @Override
        public int getColumnCount() { return columnNames.length; }
        
        @Override
        public String getColumnName(int column) { return columnNames[column]; }
        
        @Override
        public Object getValueAt(int row, int column) {
            Car car = cars.get(row);
            switch (column) {
                case 0: return car.getCarID();
                case 1: return car.getModel();
                case 2: return car.getYear();
                case 3: return car.getColor();
                case 4: return car.getPrice();
                case 5: return car.getStatus();
                case 6: return car.getAssignedSalesmanID();
                default: return null;
            }
        }
    }
}