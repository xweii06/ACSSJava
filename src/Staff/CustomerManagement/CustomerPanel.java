package Staff.CustomerManagement;

import Customer.Customer;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

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
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton refreshButton = new JButton("Refresh");
        
        ApproveCus approver = new ApproveCus();
        approveButton.addActionListener(e -> approver.handlePendingCus());
        updateButton.addActionListener(this::editCustomer);
        deleteButton.addActionListener(this::deleteCustomer);
        refreshButton.addActionListener(e -> loadCustomerData());
        
        toolBar.add(updateButton);
        toolBar.add(deleteButton);
        toolBar.addSeparator();
        toolBar.add(refreshButton);
        
        this.add(toolBar, BorderLayout.NORTH);
        
        // Table
        tableModel = new CustomerTableModel();
        customerTable = new JTable(tableModel);
        customerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customerTable.getTableHeader().setReorderingAllowed(false);
        customerTable.getTableHeader().setResizingAllowed(false);
        
        this.add(new JScrollPane(customerTable), BorderLayout.CENTER);
    }
    
    private void loadCustomerData() {
        try {
            List<Customer> customers = service.getAllCustomers();
            tableModel.setCustomers(customers);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading customer data: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void editCustomer(ActionEvent e) {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a customer to edit", 
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            Customer selectedCustomer = tableModel.getCustomerAt(selectedRow);
            CustomerDialog dialog = new CustomerDialog(
                (JFrame)SwingUtilities.getWindowAncestor(this), 
                "Edit Customer",
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
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a customer to delete", 
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            Customer selectedCustomer = tableModel.getCustomerAt(selectedRow);
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
            switch (column) {
                case 0: return customer.getId();
                case 1: return customer.getName();
                case 2: return customer.getPhone();
                case 3: return customer.getEmail();
                default: return null;
            }
        }
    }
}