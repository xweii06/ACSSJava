package Staff;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import navigation.FrameManager;
import utils.DataIO;

public class ViewAllRecords {
    
    private static final String STAFF_FILE = "staff.txt", SALESMAN_FILE = "salesman.txt",
            CUSTOMER_FILE = "customers.txt", CAR_FILE = "car.txt";
    
    private static JFrame frame;
    private static JTable table;
    private static JTextField searchField;
    
    public static ActionListener viewAllRecords(String menuItem) {
        return e -> FrameManager.showFrame(showRecords(menuItem));
    }
    
    private static String getFilename(String menuItem) {
        switch (menuItem) {
            case "Staff Management": return STAFF_FILE;
            case "Customer Management": return CUSTOMER_FILE;
            case "Salesman Management": return SALESMAN_FILE;
            case "Car Management": return CAR_FILE;
            default:
                throw new IllegalArgumentException("Unknown menu item: " + menuItem);
        }
    }
    
    public static JFrame showRecords(String menuItem) {
        frame = new JFrame("View " + menuItem.split(" ")[0]);
        frame.setLayout(new BorderLayout());
        frame.setSize(800, 600);
        
        String[] columns = getColumnsFor(menuItem);
        Object[][] data = getTableData(menuItem);
        table = new JTable(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setRowHeight(25); 
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setReorderingAllowed(false);
        
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton backBtn = new JButton("Back");
        backBtn.setFont(new Font("Arial", Font.BOLD, 14));
        backBtn.setPreferredSize(new Dimension(100, 30));
        backBtn.setFocusable(false);
        backBtn.addActionListener(e -> FrameManager.goBack());
        
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        
        searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setPreferredSize(new Dimension(100, 30));
        
        JButton searchBtn = new JButton("Search");
        searchBtn.setFont(new Font("Arial", Font.PLAIN, 14));
        searchBtn.setPreferredSize(new Dimension(80, 30));
        searchBtn.setFocusable(false);
        
        searchBtn.addActionListener(e -> {
            String searchText = searchField.getText().toLowerCase();
            TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
            table.setRowSorter(sorter);
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
        });
        
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        
        controlPanel.add(searchPanel);
        
        if (menuItem.equals("Car Management")) {
            JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            JLabel sortLabel = new JLabel("Sort by:");
            sortLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            
            String[] sortOptions = {"Default", "Status", "Price", "Salesman ID"};
            JComboBox<String> sortComboBox = new JComboBox<>(sortOptions);
            sortComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
            sortComboBox.setPreferredSize(new Dimension(150, 30));
            
            sortComboBox.addActionListener(e -> {
                String selected = (String) sortComboBox.getSelectedItem();
                switch (selected) {
                    case "Status (A-Z)":
                        sortTable(4);
                        break;
                    case "Price":
                        sortNumericColumn(3);
                        break;
                    case "Salesman ID":
                        sortTable(5);
                        break;
                    default:
                        table.setRowSorter(null);
                        break;
                }
            });
            sortPanel.add(sortLabel);
            sortPanel.add(sortComboBox);
            controlPanel.add(sortPanel);
        }
        
        topPanel.add(controlPanel, BorderLayout.CENTER);
        topPanel.add(backBtn, BorderLayout.WEST);
        frame.add(topPanel, BorderLayout.NORTH);
        
        frame.setVisible(true);
        return frame;
    }
    
    private static String[] getColumnsFor(String menuItem) {
        switch(menuItem) {
            case "Staff Management":
                return new String[]{"Staff ID", "Name"};
            case "Salesman Management":
                return new String[]{"Salesman ID", "Name", "Phone", "Email"};
            case "Customer Management":
                return new String[]{"Customer ID", "Name", "Phone", "Email"};
            case "Car Management":
                return new String[]{"Car ID", "Model", "Year", "Price(RM)", "Status", "Salesman ID"};
            default:
                return new String[]{"ID", "Details"};
        }
    }
    
    private static Object[][] getTableData(String menuItem) {
        String filename = getFilename(menuItem);
        String data = DataIO.readFile(filename);
        
        if (data == null || data.trim().isEmpty()) {
            return new Object[0][0];
        }
        String[] records = data.split("\n");
        Object[][] tableData = new Object[records.length][];
        
        for (int i = 0; i < records.length; i++) {
            if (!records[i].trim().isEmpty()) {
                tableData[i] = records[i].split(",");
            }
        }
        return tableData;
    }
    
    private static void sortTable(int columnIndex) {
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(sorter);
        sorter.setSortKeys(Arrays.asList(
                new RowSorter.SortKey(columnIndex, SortOrder.ASCENDING)));
    }
    
    private static void sortNumericColumn(int columnIndex) {
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel()) {
            @Override
            public Comparator<?> getComparator(int column) {
                if (column == columnIndex) { 
                    return (Comparator<Object>) (o1, o2) -> {
                        try {
                            double d1 = Double.parseDouble(o1.toString());
                            double d2 = Double.parseDouble(o2.toString());
                            return Double.compare(d1, d2);
                        } catch (NumberFormatException e) {
                            return 0;
                        }
                    };
                }
                return super.getComparator(column);
            }
        };
        table.setRowSorter(sorter);
        sorter.setSortKeys(Arrays.asList(
                new RowSorter.SortKey(columnIndex, SortOrder.ASCENDING)));
    }
}