package Staff.SaleManagement;

import Main.Car;
import Staff.CustomerManagement.CustomerService;
import java.awt.*;
import java.time.LocalDate;
import java.util.UUID;
import javax.swing.*;

public class AppointmentDialog extends JDialog {
    public static final int OK_OPTION = 0;
    public static final int CANCEL_OPTION = 1;
    private int result = CANCEL_OPTION;
    
    private final JTextField customerIDField;
    private final JTextField dueDateField;
    private final JTextField carIDField;
    private final JTextField modelField;
    private final JTextField priceField;
    private final CustomerService customerService;
    
    public AppointmentDialog(JFrame parent, String title, Car car, CustomerService customerService) {
        super(parent, title, true);
        this.customerService = customerService;
        
        // Initialize components
        customerIDField = new JTextField(15);
        dueDateField = new JTextField(15);
        carIDField = new JTextField(15);
        modelField = new JTextField(15);
        priceField = new JTextField(15);
        
        // Set default values (non-editable fields)
        carIDField.setText(car.getCarID());
        carIDField.setEditable(false);
        modelField.setText(car.getModel());
        modelField.setEditable(false);
        priceField.setText(String.valueOf(car.getPrice()));
        priceField.setEditable(false);
        
        // Set default due date (2 weeks from now)
        LocalDate defaultDueDate = LocalDate.now().plusWeeks(2);
        dueDateField.setText(defaultDueDate.toString());
        
        // Create panel
        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel("Customer ID:"));
        panel.add(customerIDField);
        panel.add(new JLabel("Due Date (YYYY-MM-DD):"));
        panel.add(dueDateField);
        panel.add(new JLabel("Car ID:"));
        panel.add(carIDField);
        panel.add(new JLabel("Model:"));
        panel.add(modelField);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);
        
        // Add buttons
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            if (validateInput()) {
                result = OK_OPTION;
                this.dispose();
            }
        });
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> {
            result = CANCEL_OPTION;
            this.dispose();
        });
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        this.pack();
        this.setLocationRelativeTo(parent);
    }
    
    private boolean validateInput() {
        // Validate customer exists
        try {
            if (!customerService.customerExists(customerIDField.getText())) {
                JOptionPane.showMessageDialog(this, "Customer ID not found", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error checking customer: " + 
                    ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Validate due date
        try {
            LocalDate dueDate = LocalDate.parse(dueDateField.getText());
            LocalDate maxDate = LocalDate.now().plusWeeks(2);
            if (dueDate.isAfter(maxDate)) {
                JOptionPane.showMessageDialog(this, "Due date cannot exceed 2 weeks", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid date format (use YYYY-MM-DD)", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    
    public int showDialog() {
        this.setVisible(true);
        return result;
    }
    
    public Appointment getAppointment() {
        String orderID = UUID.randomUUID().toString().substring(0, 8);
        return new Appointment(
            orderID,
            customerIDField.getText(),
            carIDField.getText(),
            modelField.getText(),
            Double.parseDouble(priceField.getText()),
            LocalDate.parse(dueDateField.getText()),
            "pending"
        );
    }
}