package Staff.CarManagement;

import Main.Car;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import utils.TableUtils;

public class CarPanel extends JPanel {
    private final CarService service;
    private JTable carTable;
    private CarTableModel tableModel;
    
    public CarPanel(CarService service) {
        this.service = service;
        initializeUI();
        loadCarData();
    }
    
    private void initializeUI() {
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Toolbar
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        
        JButton addButton = new JButton("Add");
        addButton.setFocusable(false);
        JButton updateButton = new JButton("Update");
        updateButton.setFocusable(false);
        JButton deleteButton = new JButton("Delete");
        deleteButton.setFocusable(false);
        JButton viewImageButton = new JButton("View Image");
        viewImageButton.setFocusable(false);
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFocusable(false);
        
        addButton.addActionListener(this::addCar);
        updateButton.addActionListener(this::editCar);
        deleteButton.addActionListener(this::deleteCar);
        viewImageButton.addActionListener(this::viewImage);
        refreshButton.addActionListener(e -> loadCarData());
        
        toolBar.add(addButton);
        toolBar.add(updateButton);
        toolBar.add(deleteButton);
        toolBar.add(viewImageButton);
        toolBar.addSeparator();
        toolBar.add(refreshButton);
        
        // Table
        tableModel = new CarTableModel();
        carTable = new JTable(tableModel);
        carTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        carTable.getTableHeader().setReorderingAllowed(false);
        carTable.getTableHeader().setResizingAllowed(false);
        
        TableRowSorter<CarTableModel> sorter = new TableRowSorter<>(tableModel);
        carTable.setRowSorter(sorter);
        
        sorter.setSortKeys(List.of(new RowSorter.SortKey(0, SortOrder.ASCENDING)));

        // Apply numeric sorting to selected columns
        TableUtils.sortNumericColumn(carTable, 2);
        TableUtils.sortNumericColumn(carTable, 4);
        
        Font tableFont = new Font("Arial", Font.PLAIN, 14); 
        carTable.setFont(tableFont);
        carTable.setRowHeight(24);
        
        // Main panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(TableUtils.createSearchPanel(carTable), BorderLayout.EAST);
        topPanel.add(toolBar, BorderLayout.WEST);
        this.add(topPanel, BorderLayout.NORTH);
        
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
                    "Add New Car", null, service);
            
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
        int selectedViewRow = carTable.getSelectedRow();
        if (selectedViewRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a car to edit", 
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int selectedModelRow = carTable.convertRowIndexToModel(selectedViewRow);
        try {
            Car originalCar = tableModel.getCarAt(selectedModelRow);

            // Block editing booked and paid cars
            if ("paid".equalsIgnoreCase(originalCar.getStatus()) ||
                    "booked".equalsIgnoreCase(originalCar.getStatus())) {
                JOptionPane.showMessageDialog(this, "Paid or booked cars cannot be edited", 
                        "Action Blocked", JOptionPane.WARNING_MESSAGE);
                return;
            }

            CarDialog dialog = new CarDialog(
                    (JFrame)SwingUtilities.getWindowAncestor(this), 
                    "Edit Car",
                    originalCar, 
                    service);

            if (dialog.showDialog() == CarDialog.OK_OPTION) {
                Car updatedCar = dialog.getCar();
                service.updateCar(updatedCar);
                loadCarData();
                JOptionPane.showMessageDialog(this, "Car details updated successfully", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteCar(ActionEvent e) {
        int selectedViewRow = carTable.getSelectedRow();
        if (selectedViewRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a car to delete", 
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int selectedModelRow = carTable.convertRowIndexToModel(selectedViewRow);
        try {
            Car selectedCar = tableModel.getCarAt(selectedModelRow);
            if (!service.isCarAvailable(selectedCar.getCarID())) {
                JOptionPane.showMessageDialog(this,
                    "Cannot delete car that is not available (status: " + 
                    carTable.getValueAt(selectedModelRow, 5) + ")",
                    "Cannot Delete", JOptionPane.WARNING_MESSAGE);
                return;
            }
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
        int selectedViewRow = carTable.getSelectedRow();
        if (selectedViewRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a car to view image", 
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int selectedModelRow = carTable.convertRowIndexToModel(selectedViewRow);
        try {
            Car selectedCar = tableModel.getCarAt(selectedModelRow);
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
            "Car ID", "Model", "Year", "Color", "Price(RM)", "Status", "Salesman ID"
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