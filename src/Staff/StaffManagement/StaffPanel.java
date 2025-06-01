package Staff.StaffManagement;

import Staff.Staff;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import utils.TableUtils;

public class StaffPanel extends JPanel {
    private final StaffService service;
    private JTable staffTable;
    private StaffTableModel tableModel;
    
    public StaffPanel(StaffService service) {
        this.service = service;
        initializeUI();
        loadStaffData();
    }
    
    private void initializeUI() {
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Toolbar
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        
        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton refreshButton = new JButton("Refresh");
        
        addButton.addActionListener(this::addStaff);
        updateButton.addActionListener(this::editStaff);
        deleteButton.addActionListener(this::deleteStaff);
        refreshButton.addActionListener(e -> loadStaffData());
        
        toolBar.add(addButton);
        toolBar.add(updateButton);
        toolBar.add(deleteButton);
        toolBar.addSeparator();
        toolBar.add(refreshButton);
        
        // Table
        tableModel = new StaffTableModel();
        staffTable = new JTable(tableModel);
        staffTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        staffTable.getTableHeader().setReorderingAllowed(false);
        staffTable.getTableHeader().setResizingAllowed(false);
        TableUtils.setDefaultSort(staffTable, 0); 
        
        Font tableFont = new Font("Arial", Font.PLAIN, 14); 
        staffTable.setFont(tableFont);
        staffTable.setRowHeight(24);
        
        // Main panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(TableUtils.createSearchPanel(staffTable), BorderLayout.EAST);
        topPanel.add(toolBar, BorderLayout.WEST);
        this.add(topPanel, BorderLayout.NORTH);
        
        this.add(new JScrollPane(staffTable), BorderLayout.CENTER);
    }
    
    private void loadStaffData() {
        try {
            List<Staff> staffList = service.getAllStaff();
            tableModel.setStaffList(staffList);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error loading staff data: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void addStaff(ActionEvent e) {
        try {
            StaffDialog dialog = new StaffDialog((JFrame) SwingUtilities.getWindowAncestor(this), 
                    "Add New Staff", service.generateNewID());
            
            if (dialog.showDialog() == StaffDialog.OK_OPTION) {
                Staff newStaff = dialog.getStaff();
                service.addStaff(newStaff);
                loadStaffData();
                JOptionPane.showMessageDialog(this, "Staff added successfully", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void editStaff(ActionEvent e) {
        int selectedViewRow = staffTable.getSelectedRow();
        if (selectedViewRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a staff member to edit", 
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int selectedModelRow = staffTable.convertRowIndexToModel(selectedViewRow);
        try {
            Staff selectedStaff = tableModel.getStaffAt(selectedModelRow);
            StaffDialog dialog = new StaffDialog((JFrame) SwingUtilities.getWindowAncestor(this), 
                    "Edit Staff", selectedStaff);
            
            if (dialog.showDialog() == StaffDialog.OK_OPTION) {
                Staff updatedStaff = dialog.getStaff();
                service.updateStaff(updatedStaff);
                loadStaffData();
                JOptionPane.showMessageDialog(this, "Staff updated successfully", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteStaff(ActionEvent e) {
        int selectedRow = staffTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a staff to delete", 
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            Staff selectedStaff = tableModel.getStaffAt(selectedRow);
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Are you sure you want to delete " + selectedStaff.getName() + "?", 
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                service.deleteStaff(selectedStaff.getId());
                loadStaffData();
                JOptionPane.showMessageDialog(this, "Staff deleted successfully", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private static class StaffTableModel extends AbstractTableModel {
        private List<Staff> staffList;
        private final String[] columnNames = {"ID", "Name", "Role"};
        
        public StaffTableModel() {
            this.staffList = new ArrayList<>();
        }
        
        public void setStaffList(List<Staff> staffList) {
            this.staffList = staffList;
            fireTableDataChanged();
        }
        
        public Staff getStaffAt(int row) {
            return staffList.get(row);
        }
        
        @Override
        public int getRowCount() { return staffList.size(); }
        
        @Override
        public int getColumnCount() { return columnNames.length; }
        
        @Override
        public String getColumnName(int column) { return columnNames[column]; }
        
        @Override
        public Object getValueAt(int row, int column) {
            Staff staff = staffList.get(row);
            return switch (column) {
                case 0 -> staff.getId();
                case 1 -> staff.getName();
                case 2 -> staff.getRole();
                default -> null;
            };
        }
    }
}