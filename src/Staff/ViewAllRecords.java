package Staff;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import utils.DataIO;

public class ViewAllRecords {
    
    public static JTable getRecordsTable(String menuItem) {
        String[] columns = getColumnsFor(menuItem);
        
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        
        String filename = FileManager.getFilename(menuItem);
        String data = DataIO.readFile(filename);
        
        if (data != null && !data.trim().isEmpty()) {
            String[] records = data.split("\n");
            for (String record : records) {
                if (!record.trim().isEmpty()) {
                    String[] rowData = record.split(",");
                    model.addRow(rowData);
                }
            }
        }
        
        JTable table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setReorderingAllowed(false);
        
        return table;
    }
    
    public static String[] getColumnsFor(String menuItem) {
        switch(menuItem) {
            case "Staff Management":
                return new String[]{"Staff ID", "Name"};
            case "Salesman Management":
                return new String[]{"Salesman ID", "Name", "Phone", "Email"};
            case "Customer Management":
                return new String[]{"Customer ID", "Name", "Phone", "Email"};
            case "Car Management":
                return new String[]{"CarID", "Model", "Year", "Color", "Price(RM)", "Status", "SalesmanID"};
            default:
                return new String[0];
        }
    }
}