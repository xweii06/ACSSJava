package Staff;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import java.util.*;
import java.util.List;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import navigation.FrameManager;
import utils.DataIO;

public class StaffActions {
    
    private static final String STAFF_FILE = "staff.txt", SALESMAN_FILE = "salesman.txt", 
            CUSTOMER_FILE = "customers.txt", PENDING_CUS_FILE = "pendingCustomers.txt",
            CAR_FILE = "car.txt";
    
    private static String filename;
    private static JFrame formFrame;
    private static LinkedHashMap<String, JComponent> fields;
    private static JButton submitBtn, cancelBtn;
    private static JPanel panel;
    private static JComboBox availabilityBox;
    
    public static ActionListener addAction(String menuItem) {
        return e -> {
            formFrame = new JFrame("Add New " + (menuItem.split(" "))[0]);
            formFrame.setLayout(new BorderLayout());
            formFrame.setSize(400, 300);
            formFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
            fields = new LinkedHashMap<>();
            
            availabilityBox = new JComboBox<>(new String[]{"Available", "Booked", "Paid", "Cancelled"});
            
            switch (menuItem) {
                case "Staff Management":
                    fields.put("ID", createTextField());
                    fields.put("Password", createPasswordField());
                    fields.put("Name", createTextField());
                    fields.put("Role", createTextField());
                    break;
                    
                case "Salesman Management":
                    fields.put("ID", createTextField());
                    fields.put("Password", createPasswordField());
                    fields.put("Name", createTextField());
                    break;

                case "Customers Management":
                    fields.put("Customer ID", createTextField());
                    fields.put("Name", createTextField());
                    fields.put("Phone",createTextField());
                    fields.put("Email", createTextField());
                    break;

                case "Car Management":
                    fields.put("Model", createTextField());
                    fields.put("Year", createTextField());
                    fields.put("Price", createTextField());
                    fields.put("Status", availabilityBox);
                    break;
            }
            
            panel = new JPanel(new GridLayout(0,2,10,10));
            
            for (Map.Entry<String, JComponent> entry : fields.entrySet()) {
                panel.add(new JLabel(entry.getKey() + ":"));
                panel.add(entry.getValue());
            }
            
            submitBtn = new JButton("Submit");
            submitBtn.addActionListener(ev -> processFormSubmission(menuItem, fields));
            cancelBtn = new JButton("Cancel");
            cancelBtn.setBackground(Color.white);
            cancelBtn.addActionListener(ev -> FrameManager.goBack());
            
            panel.add(submitBtn);
            panel.add(cancelBtn);
            panel.setBorder(BorderFactory.createEmptyBorder(10,40,10,40));
            formFrame.add(panel,BorderLayout.CENTER);
            
            formFrame.pack();
            formFrame.setVisible(true);
            FrameManager.showFrame(formFrame);
        };
    }

    private static void processFormSubmission(String menuItem, LinkedHashMap<String, JComponent> fields) {
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
            JOptionPane.showMessageDialog(formFrame, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
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
                details += "********"; // Hide password
            } else if (field instanceof JComboBox) {
                details += ((JComboBox<?>) field).getSelectedItem();
            }
            details += "\n";
        }

        int confirm = JOptionPane.showConfirmDialog(null,details + "\nAre you sure?",
                "Confirmation",JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                switch (menuItem) {
                    case "Staff Management": filename = STAFF_FILE; break;
                    case "Salesman Management": filename = SALESMAN_FILE; break;
                    case "Car Management": filename = CAR_FILE; break;
                    case "Customer Management": filename = CUSTOMER_FILE; break;
                }
                String record = buildRecord(menuItem, fields);
                DataIO.appendToFile(filename, record);
                JOptionPane.showMessageDialog(null, "Added successfully!", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error saving: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static String buildRecord(String menuItem, LinkedHashMap<String, JComponent> fields) {
        List<String> data = new ArrayList<>();

        switch (menuItem) {
            case "Staff Management":
                data.add(((JTextField) fields.get("ID")).getText());
                data.add(new String(((JPasswordField) fields.get("Password")).getPassword()));
                data.add(((JTextField) fields.get("Name")).getText());
                data.add(((JTextField) fields.get("Role")).getText());
                break;

            case "Salesman Management":
                data.add(((JTextField) fields.get("ID")).getText());
                data.add(new String(((JPasswordField) fields.get("Password")).getPassword()));
                data.add(((JTextField) fields.get("Name")).getText());
                break;

            case "Customers Management":
                data.add(((JTextField) fields.get("Customer ID")).getText());
                data.add(((JTextField) fields.get("Name")).getText());
                data.add(((JTextField) fields.get("Phone")).getText());
                data.add(((JTextField) fields.get("Email")).getText());
                break;

            case "Car Management":
                data.add(((JTextField) fields.get("Car ID")).getText());
                data.add(((JTextField) fields.get("Model")).getText());
                data.add(((JTextField) fields.get("Year")).getText());
                data.add(((JTextField) fields.get("Price")).getText());
                data.add(((JComboBox<?>) fields.get("Status")).getSelectedItem().toString());
                break;
        }

        return String.join(",", data);
    }
    
    public static JTextField createTextField() {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(100, 25));
        field.setMaximumSize(new Dimension(100, 350));
        field.setMinimumSize(new Dimension(100, 350));
        field.setFont(new Font("Arial", Font.PLAIN, 18));
        return field;
    }
    
    public static JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setPreferredSize(new Dimension(100, 25));
        field.setMaximumSize(new Dimension(100, 350));
        field.setMinimumSize(new Dimension(100, 350));
        field.setFont(new Font("Arial", Font.PLAIN, 18));
        return field;
    }
}
