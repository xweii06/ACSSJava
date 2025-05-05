package Staff;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.Border;
import navigation.FrameManager;
import utils.DataIO;

public class DeleteRecords {
    
    private static final String STAFF_FILE = "staff.txt", SALESMAN_FILE = "salesman.txt",
            CUSTOMER_FILE = "customers.txt", CAR_FILE = "car.txt";
    
    private static String currentRecordID;
    private static JFrame deleteFrame;
    private static JPanel panel, formPanel, buttonPanel, searchInputPanel, previewPanel;
    private static JTextField idField;
    private static JTextArea previewArea;
    private static JButton searchBtn, deleteBtn, cancelBtn;
    
    public static ActionListener deleteRecords(String menuItem) {
        return e -> FrameManager.showFrame(createDeleteFrame(menuItem));
    }
    
    private static JFrame createDeleteFrame(String menuItem) {
        deleteFrame = new JFrame("Delete " + (menuItem.split(" "))[0]);
        deleteFrame.setLayout(new BorderLayout());
        deleteFrame.setSize(400, 300);
        deleteFrame.setResizable(false);
        
        panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        formPanel = new JPanel(new BorderLayout(10, 10));
        
        searchInputPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        searchInputPanel.add(new JLabel("Enter ID:"));
        idField = new JTextField();
        searchInputPanel.add(idField);
        
        searchBtn = new JButton("Search");
        searchBtn.setFocusable(false);
        searchBtn.addActionListener(e -> searchRecord(menuItem));
        searchInputPanel.add(searchBtn);
        
        previewPanel = new JPanel(new BorderLayout());
        previewPanel.setBorder(BorderFactory.createTitledBorder("Record Preview"));
        previewArea = new JTextArea();
        previewArea.setEditable(false);
        previewArea.setFont(new Font("Arial", Font.PLAIN, 12));
        previewPanel.add(new JScrollPane(previewArea), BorderLayout.CENTER);
        previewPanel.setVisible(false);
        
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        Border border = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.black,1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10));
        deleteBtn = new JButton("Delete Record");
        deleteBtn.setBackground(new Color(0xE31A3E));
        deleteBtn.setForeground(Color.white);
        deleteBtn.setBorder(border);
        deleteBtn.setEnabled(false);
        deleteBtn.setFocusable(false);
        deleteBtn.addActionListener(e -> confirmDelete(menuItem));
        
        cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(new Color(0x999999));
        cancelBtn.setForeground(Color.white);
        cancelBtn.setBorder(border);
        cancelBtn.setFocusable(false);
        cancelBtn.addActionListener(e -> FrameManager.goBack());
        
        buttonPanel.add(deleteBtn);
        buttonPanel.add(cancelBtn);
        
        formPanel.add(searchInputPanel, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        formPanel.add(previewPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        deleteFrame.add(panel);
        return deleteFrame;
    }
    
    private static void searchRecord(String menuItem) {
        String searchID = idField.getText().trim();
        
        if (searchID == null || searchID.isEmpty()) {
            JOptionPane.showMessageDialog(deleteFrame, 
                "Please enter an ID", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String id = searchID.toUpperCase();
        String filename = getFilename(menuItem);
        String data = DataIO.readFile(filename);
        String[] lines = data.split("\n");
        boolean found = false;
        for (String line : lines) {
            if (line.startsWith(id + ",")) {
                currentRecordID = id;
                displayRecordPreview(menuItem,line);
                found = true;
                break;
            }
        }
        if (!found) {
            previewPanel.setVisible(false);
            deleteBtn.setEnabled(false);
            JOptionPane.showMessageDialog(deleteFrame,
                    "No record found with ID: " + searchID,
                    "Not Found", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private static void displayRecordPreview(String menuItem, String line) {
        String[] parts = line.split(",");
        StringBuilder previewText = new StringBuilder();
        
        switch (menuItem) {
            case "Staff Management":
                previewText.append("Staff ID: ").append(parts[0]).append("\n");
                previewText.append("Name: ").append(parts[2]).append("\n");
                break;
            case "Salesman Management":
                previewText.append("Salesman ID: ").append(parts[0]).append("\n");
                previewText.append("Name: ").append(parts[2]).append("\n");
                previewText.append("Phone: ").append(parts[3]).append("\n");
                previewText.append("Email: ").append(parts[4]).append("\n");
                break;
            case "Customer Management":
                previewText.append("Customer ID: ").append(parts[0]).append("\n");
                previewText.append("Name: ").append(parts[1]).append("\n");
                previewText.append("Phone: ").append(parts[2]).append("\n");
                previewText.append("Email: ").append(parts[3]).append("\n");
                break;
            case "Car Management":
                previewText.append("Car ID: ").append(parts[0]).append("\n");
                previewText.append("Model: ").append(parts[1]).append("\n");
                previewText.append("Price: ").append(parts[2]).append("\n");
                previewText.append("Status: ").append(parts[3]).append("\n");
                previewText.append("Assigned Salesman ID: ").append(parts[4]).append("\n");
                break;
        }
        
        previewArea.setText(previewText.toString());
        previewPanel.setVisible(true);
        deleteBtn.setEnabled(true);
    }
    
    private static void confirmDelete(String menuItem) {
        switch (menuItem) {
            case "Staff Management":
                if (currentRecordID.equals("S00")) {
                    JOptionPane.showMessageDialog(deleteFrame,
                    "Cannot delete your own ID",
                    "Action blocked", JOptionPane.WARNING_MESSAGE);
                }
                return;
            case "Salesman Management":
                // need to reassign cars that is under this salesman
                // change assigned salesman in car.txt
                break;
            case "Customer Management":
                // maybe send a noti to that cus? idk
                break;
            case "Car Management":
                // status cannot be booked or paid (idk abt cancelled and available?)
                if (!checkCarStatus(menuItem)) {
                    return;
                } 
                break;
        }
        
        int confirm = JOptionPane.showConfirmDialog(deleteFrame,
            "Are you sure you want to delete this record?\n\n",
            "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            deleteCurrentRecord(menuItem);
        }
    }
    
    private static void deleteCurrentRecord(String menuItem) {
        String filename = getFilename(menuItem);
        try {
            String data = DataIO.readFile(filename);
            String[] lines = data.split("\n");
            
            for (String line : lines) {
                if (!line.startsWith(currentRecordID + ",")) {
                    DataIO.writeFile(filename,line);
                }
            }
            JOptionPane.showMessageDialog(deleteFrame,
                "Record deleted successfully!",
                "Success", JOptionPane.INFORMATION_MESSAGE);
            
            idField.setText("");
            previewArea.setText("");
            previewPanel.setVisible(false);
            deleteBtn.setEnabled(false);
            currentRecordID = null;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(deleteFrame,
                "Error deleting record: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private static boolean checkCarStatus(String menuItem) {
        String filename = getFilename(menuItem);
        String data = DataIO.readFile(filename);
        String[] lines = data.split("\n");
        for (String line : lines) {
            if (!line.startsWith(currentRecordID + ",")) {
                String parts[] = line.split(",");
                if (parts.length >= 4) {
                    String status = parts[3];
                    return !(status.equals("Booked") || status.equals("Paid"));
                }
            }
        }
        return false;
    }
    
    private static String getFilename(String menuItem) {
        switch (menuItem) {
            case "Staff Management": return STAFF_FILE;
            case "Salesman Management": return SALESMAN_FILE;
            case "Customer Management" : return CUSTOMER_FILE;
            case "Car Management": return CAR_FILE;
            default:
                throw new IllegalArgumentException("Unknown menu item: " + menuItem);
        }
    }
    
}