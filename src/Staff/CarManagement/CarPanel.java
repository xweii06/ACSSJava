package Staff.CarManagement;

import Main.Car;
import Staff.SaleManagement.Appointment;
import Staff.SaleManagement.AppointmentDialog;
import Staff.SaleManagement.Sale;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
                    "Add New Car",null,service, false);
            
            if (dialog.showDialog() == CarDialog.OK_OPTION) {
                Car newCar = dialog.getNewCar();
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
        int selectedViewRow  = carTable.getSelectedRow();
        if (selectedViewRow  == -1) {
            JOptionPane.showMessageDialog(this, "Please select a car to edit", 
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int selectedModelRow = carTable.convertRowIndexToModel(selectedViewRow);
        try {
            Car originalCar = tableModel.getCarAt(selectedModelRow);
        
            // Block editing paid cars
            if ("paid".equalsIgnoreCase(originalCar.getStatus())) {
                JOptionPane.showMessageDialog(this, "Paid cars cannot be edited", 
                        "Action Blocked", JOptionPane.WARNING_MESSAGE);
                return;
            }

            CarDialog dialog;
            if ("booked".equalsIgnoreCase(originalCar.getStatus())) {
                dialog = new CarDialog(
                        (JFrame)SwingUtilities.getWindowAncestor(this), 
                        "Edit Car Status Only",
                        originalCar, 
                        service,
                        true);
            } else {
                dialog = new CarDialog(
                        (JFrame)SwingUtilities.getWindowAncestor(this), 
                        "Edit Car",
                        originalCar, 
                        service,
                        false);
            }

            if (dialog.showDialog() == CarDialog.OK_OPTION) {
                Car updatedCar = dialog.getCar();
                String originalStatus = originalCar.getStatus();
                String newStatus = updatedCar.getStatus();
                
                if (!originalStatus.equalsIgnoreCase(newStatus)) {
                    if (!handleStatusChange(originalStatus, newStatus, updatedCar)) {
                        return; // Exit if status change was cancelled/failed
                    }
                    service.updateCar(updatedCar);
                    loadCarData();
                    JOptionPane.showMessageDialog(this, "Car status updated successfully", 
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                } // If only non-status fields changed
                else if (!carDetailsEqual(originalCar, updatedCar)) {
                    service.updateCar(updatedCar);
                    loadCarData();
                    JOptionPane.showMessageDialog(this, "Car details updated successfully", 
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "No changes were made", 
                            "Info", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
    
    private boolean carDetailsEqual(Car oriCar, Car newCar) {
        return oriCar.getModel().equals(newCar.getModel()) &&
               oriCar.getYear().equals(newCar.getYear()) &&
               oriCar.getColor().equals(newCar.getColor()) &&
               oriCar.getPrice().equals(newCar.getPrice()) &&
               oriCar.getAssignedSalesmanID().equals(newCar.getAssignedSalesmanID());
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
    
    private boolean handleStatusChange(String originalStatus, String newStatus, Car car) throws Exception {
        switch (newStatus.toLowerCase()) {
            case "booked":
                if (!originalStatus.equalsIgnoreCase("available") && 
                    !originalStatus.equalsIgnoreCase("cancelled")) {
                    showInvalidTransitionError("Can only book available or cancelled cars");
                    return false;
                }
                return confirmAndBook(car);

            case "available":
                if (!originalStatus.equalsIgnoreCase("booked") && 
                    !originalStatus.equalsIgnoreCase("cancelled")) {
                    showInvalidTransitionError("Can only cancel booking from booked status");
                    return false;
                }
                return confirmAndCancelBooking(car);

            case "paid":
                if (!originalStatus.equalsIgnoreCase("booked")) {
                    showInvalidTransitionError("Only booked cars can be paid");
                    return false;
                }
                return confirmAndPay(car);

            case "cancelled":
                return confirmAndCancelCar(car);

            default:
                showInvalidTransitionError("Invalid status: " + newStatus);
                return false;
        }
    }
    
    private void showInvalidTransitionError(String message) {
        JOptionPane.showMessageDialog(this, 
            "Invalid status change: " + message,
            "Invalid Operation",
            JOptionPane.ERROR_MESSAGE);
    }
    
    private boolean confirmAndBook(Car car) throws Exception {
        int choice = JOptionPane.showConfirmDialog(this,
                "Book this car?", "Book Car", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            if (bookCar(car)) {
                return true;
            }
        }
        return false;
    }

    private boolean confirmAndCancelBooking(Car car) throws Exception {
        int choice = JOptionPane.showConfirmDialog(this,
                "Cancel booking?", "Booking Cancellation", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            cancelBooking(car);
            return true;
        }
        return false;
    }
    
    private boolean confirmAndPay(Car car) throws Exception {
        int choice = JOptionPane.showConfirmDialog(this,
            "Mark this booking as paid?", 
            "Confirm Payment", 
            JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            if (markAsPaid(car)) {
                return true;
            }
        }
        return false;
    }

    private boolean confirmAndCancelCar(Car car) throws Exception {
        int choice = JOptionPane.showConfirmDialog(this,
            "Cancel this car?", 
            "Confirm Cancellation", 
            JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            if (markAsCancelled(car)) {
                return true; 
            }
        }
        return false;
    }
    
    private boolean bookCar(Car car) {
        try {
            AppointmentDialog dialog = new AppointmentDialog(
                    (JFrame)SwingUtilities.getWindowAncestor(this), 
                    "Book Car - " + car.getModel(), 
                    car, 
                    service.getCustomerService()
            );

            if (dialog.showDialog() == AppointmentDialog.OK_OPTION) {
                // Create and save appointment
                Appointment appointment = dialog.getAppointment();
                service.addAppointment(appointment);

                // Update car status
                car.setStatus("booked");
                service.updateCar(car);

                // Refresh UI
                loadCarData();

                JOptionPane.showMessageDialog(this, 
                    "Car booked successfully!\n" +
                    "Order ID: " + appointment.getOrderID() + "\n" +
                    "Due Date: " + appointment.getDueDate(),
                    "Booking Confirmed", 
                    JOptionPane.INFORMATION_MESSAGE);
                return true;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error booking car: " + ex.getMessage(),
                "Booking Error", 
                JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
    
    private boolean markAsPaid(Car car) {
    try {
        double price;
        try {
            price = Double.parseDouble(car.getPrice());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Invalid car price format",
                "Price Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Find existing appointment
        Appointment appointment = service.findAppointmentByCarID(car.getCarID());
        if (appointment == null) {
            JOptionPane.showMessageDialog(this,
                "No booking record found for this car",
                "Payment Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Payment method input validation
        String paymentMethod;
        while (true) {
            paymentMethod = JOptionPane.showInputDialog(this,
                "Enter payment method:",
                "Payment Method",
                JOptionPane.QUESTION_MESSAGE);

            if (paymentMethod == null) {
                JOptionPane.showMessageDialog(this,
                        "Action Cancelled",
                        "",
                        JOptionPane.INFORMATION_MESSAGE);
                return false; // User cancelled
            }
            
            if (paymentMethod.trim().matches("[a-zA-Z ]{2,20}")) {
                break;
            }
            JOptionPane.showMessageDialog(this,
                "Invalid payment method!\nMust be 2-20 letters only",
                "Invalid Input",
                JOptionPane.WARNING_MESSAGE);
        }
        
        price = Double.parseDouble(car.getPrice());
        double formattedPrice = Double.parseDouble(String.format("%.2f", price));
        
        // Create sale record with current date
        Sale sale = new Sale(
            service.generateSaleID(),
            appointment.getCustomerID(),
            car.getCarID(),
            car.getAssignedSalesmanID(),
            formattedPrice,
            paymentMethod.trim(),
            new Date()  // Add current date
        );
        service.addSale(sale);

        // Update appointment status
        appointment.setStatus("paid");
        service.updateAppointment(appointment);

        // Update car status
        car.setStatus("paid");
        service.updateCar(car);

        // Refresh UI
        loadCarData();

        JOptionPane.showMessageDialog(this,
            "Payment recorded successfully!\n" +
            "Sale ID: " + sale.getSaleID() + "\n" +
            "Amount: RM" + car.getPrice() + "\n" +
            "Date: " + new SimpleDateFormat("yyyy-MM-dd").format(sale.getSaleDate()),
            "Payment Confirmed",
            JOptionPane.INFORMATION_MESSAGE);
        return true;

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this,
            "Error processing payment: " + ex.getMessage(),
            "Payment Error",
            JOptionPane.ERROR_MESSAGE);
        return false;
    }
}
    
    private boolean markAsCancelled(Car car) {
        try {
            if ("booked".equalsIgnoreCase(car.getStatus())) { 
                if (cancelBooking(car)){
                    car.setStatus("cancelled");
                    service.updateCar(car);
                    // Refresh UI
                    loadCarData();
                    JOptionPane.showMessageDialog(this,
                        "Cancellation recorded successfully!\n",
                        "Cancellation Confirmed",
                        JOptionPane.INFORMATION_MESSAGE);
                    return true;
                } 
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error processing cancellation: " + ex.getMessage(),
                "Payment Error",
                JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
    
    private boolean cancelBooking(Car car) {
        try {
            Appointment appointment = service.findAppointmentByCarID(car.getCarID());
            if (appointment == null) {
                JOptionPane.showMessageDialog(this,
                    "No booking record found for this car",
                    "Payment Error",
                    JOptionPane.ERROR_MESSAGE);
            } else {
                appointment.setStatus("cancelled");
                service.updateAppointment(appointment);
                return true;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error processing cancellation: " + ex.getMessage(),
                "Payment Error",
                JOptionPane.ERROR_MESSAGE);
        }
        return false;
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