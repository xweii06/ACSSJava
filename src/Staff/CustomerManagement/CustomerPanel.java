package Staff.CustomerManagement;

import Customer.Customer;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import utils.TableUtils;

public class CustomerPanel extends JPanel {
    private final CustomerService service;
    private JTable customerTable;
    private CustomerTableModel tableModel;
    
    public CustomerPanel(CustomerService service) {
        this.service = service;
        initializeUI();
        loadCustomerData();
    }
    
    private void initializeUI() {
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Toolbar
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        
        JButton approveButton = new JButton("Approve");
        approveButton.setFocusable(false);
        JButton updateButton = new JButton("Update");
        updateButton.setFocusable(false);
        JButton deleteButton = new JButton("Delete");
        deleteButton.setFocusable(false);
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFocusable(false);
        
        ApproveCus approver = new ApproveCus();
        approveButton.addActionListener(e -> approver.handlePendingCus());
        updateButton.addActionListener(this::editCustomer);
        deleteButton.addActionListener(this::deleteCustomer);
        refreshButton.addActionListener(e -> loadCustomerData());
        
        toolBar.add(approveButton);
        toolBar.add(updateButton);
        toolBar.add(deleteButton);
        toolBar.addSeparator();
        toolBar.add(refreshButton);
        
        // Table
        tableModel = new CustomerTableModel();
        customerTable = new JTable(tableModel);
        customerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customerTable.getTableHeader().setReorderingAllowed(false);
        customerTable.getTableHeader().setResizingAllowed(false);
        TableUtils.setDefaultSort(customerTable, 0); 
        
        Font tableFont = new Font("Arial", Font.PLAIN, 14); 
        customerTable.setFont(tableFont);
        customerTable.setRowHeight(24);
        
        // Main panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(TableUtils.createSearchPanel(customerTable), BorderLayout.EAST);
        topPanel.add(toolBar, BorderLayout.WEST);
        this.add(topPanel, BorderLayout.NORTH);
        
        this.add(new JScrollPane(customerTable), BorderLayout.CENTER);
    }
    
    private void loadCustomerData() {
        try {
            List<Customer> customers = service.getAllCustomers();
            tableModel.setCustomers(customers);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading customer data: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void editCustomer(ActionEvent e) {
        int selectedViewRow = customerTable.getSelectedRow();
        if (selectedViewRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a customer to edit", 
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int selectedModelRow = customerTable.convertRowIndexToModel(selectedViewRow);
        try {
            Customer selectedCustomer = tableModel.getCustomerAt(selectedModelRow);
            CustomerDialog dialog = new CustomerDialog(
                (JFrame)SwingUtilities.getWindowAncestor(this), 
                "Update Customer",
                selectedCustomer
            );
            
            if (dialog.showDialog() == CustomerDialog.OK_OPTION) {
                Customer updatedCustomer = dialog.getCustomer();
                service.updateCustomer(updatedCustomer);
                loadCustomerData();
                JOptionPane.showMessageDialog(this, "Customer updated successfully", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteCustomer(ActionEvent e) {
        int selectedViewRow = customerTable.getSelectedRow();
        if (selectedViewRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a customer to delete", 
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int selectedModelRow = customerTable.convertRowIndexToModel(selectedViewRow);
        try {
            Customer selectedCustomer = tableModel.getCustomerAt(selectedModelRow);
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Are you sure you want to delete " + selectedCustomer.getName() + "?", 
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                service.deleteCustomer(selectedCustomer.getId());
                loadCustomerData();
                JOptionPane.showMessageDialog(this, "Customer deleted successfully", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private static class CustomerTableModel extends AbstractTableModel {
        private List<Customer> customers;
        private final String[] columnNames = {"ID", "Name", "Phone", "Email"};
        
        public CustomerTableModel() {
            this.customers = new ArrayList<>();
        }
        
        public void setCustomers(List<Customer> customers) {
            this.customers = customers;
            fireTableDataChanged();
        }
        
        public Customer getCustomerAt(int row) {
            return customers.get(row);
        }
        
        @Override
        public int getRowCount() { return customers.size(); }
        
        @Override
        public int getColumnCount() { return columnNames.length; }
        
        @Override
        public String getColumnName(int column) { return columnNames[column]; }
        
        @Override
        public Object getValueAt(int row, int column) {
            Customer customer = customers.get(row);
            return switch (column) {
                case 0 -> customer.getId();
                case 1 -> customer.getName();
                case 2 -> customer.getPhone();
                case 3 -> customer.getEmail();
                default -> null;
            };
        }
    }
}