package Staff.SalesmanManagement;

import Salesman.Salesman;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class SalesmanPanel extends JPanel {
    private final SalesmanService service;
    private JTable salesmanTable;
    private SalesmanTableModel tableModel;
    
    public SalesmanPanel(SalesmanService service) {
        this.service = service;
        initializeUI();
        loadSalesmanData();
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
        
        addButton.addActionListener(this::addSalesman);
        updateButton.addActionListener(this::editSalesman);
        deleteButton.addActionListener(this::deleteSalesman);
        refreshButton.addActionListener(e -> loadSalesmanData());
        
        toolBar.add(addButton);
        toolBar.add(updateButton);
        toolBar.add(deleteButton);
        toolBar.addSeparator();
        toolBar.add(refreshButton);
        
        add(toolBar, BorderLayout.NORTH);
        
        // Table
        tableModel = new SalesmanTableModel();
        salesmanTable = new JTable(tableModel);
        salesmanTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        salesmanTable.getTableHeader().setReorderingAllowed(false);
        salesmanTable.getTableHeader().setResizingAllowed(false);
        
        this.add(new JScrollPane(salesmanTable), BorderLayout.CENTER);
    }
    
    private void loadSalesmanData() {
        try {
            List<Salesman> salesmen = service.getAllSalesmen();
            ((SalesmanTableModel)salesmanTable.getModel()).setSalesmen(salesmen);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading salesman data: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void addSalesman(ActionEvent e) {
        try {
            SalesmanDialog dialog = new SalesmanDialog(
                (JFrame)SwingUtilities.getWindowAncestor(this), 
                "Add New Salesman",
                service.generateNewID()
            );
            
            if (dialog.showDialog() == SalesmanDialog.OK_OPTION) {
                Salesman newSalesman = dialog.getSalesman();
                service.addSalesman(newSalesman);
                loadSalesmanData();
                JOptionPane.showMessageDialog(this, "Salesman added successfully", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void editSalesman(ActionEvent e) {
        int selectedRow = salesmanTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a salesman to edit", 
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            Salesman selectedSalesman = ((SalesmanTableModel)salesmanTable.getModel()).getSalesmanAt(selectedRow);
            SalesmanDialog dialog = new SalesmanDialog(
                (JFrame)SwingUtilities.getWindowAncestor(this), 
                "Edit Salesman",
                selectedSalesman
            );
            
            if (dialog.showDialog() == SalesmanDialog.OK_OPTION) {
                Salesman updatedSalesman = dialog.getSalesman();
                service.updateSalesman(updatedSalesman);
                loadSalesmanData();
                JOptionPane.showMessageDialog(this, "Salesman updated successfully", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteSalesman(ActionEvent e) {
        int selectedRow = salesmanTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a salesman to delete", 
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String selectedID = (String) salesmanTable.getValueAt(selectedRow, 0);
            
            // try to reassign cars
            ReassignCars reassignment = new ReassignCars();
            if (!(reassignment.reassignCarsBeforeDeletion(selectedID))) {
                return; // Reassignment failed or was cancelled
            }
            
            // Proceed with deletion if reassignment was successful
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete this salesman?", 
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                //salesmanService.deleteSalesman(selectedID);
                loadSalesmanData();
                JOptionPane.showMessageDialog(this, 
                    "Salesman deleted successfully", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private static class SalesmanTableModel extends AbstractTableModel {
        private List<Salesman> salesmen;
        private final String[] columnNames = {"ID", "Name", "Phone", "Email"};
        
        public SalesmanTableModel() {
            this.salesmen = new ArrayList<>();
        }
        
        public void setSalesmen(List<Salesman> salesmen) {
            this.salesmen = salesmen;
            fireTableDataChanged();
        }
        
        public Salesman getSalesmanAt(int row) {
            return salesmen.get(row);
        }
        
        @Override
        public int getRowCount() { return salesmen.size(); }
        
        @Override
        public int getColumnCount() { return columnNames.length; }
        
        @Override
        public String getColumnName(int column) { return columnNames[column]; }
        
        @Override
        public Object getValueAt(int row, int column) {
            Salesman salesman = salesmen.get(row);
            switch (column) {
                case 0: return salesman.getId();
                case 1: return salesman.getName();
                case 2: return salesman.getPhone();
                case 3: return salesman.getEmail();
                default: return null;
            }
        }
    }
}