package Staff.SaleManagement;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import javax.swing.table.TableRowSorter;
import navigation.FrameManager;
import repositories.SaleRepository;
import utils.TableUtils;

public class SalePanel extends JPanel {
    private final SaleRepository repository;
    private JTable saleTable;
    private SaleTableModel tableModel;

    public SalePanel(SaleRepository repository) {
        this.repository = repository;
        initializeUI();
        loadSaleData();
    }
    
    private void initializeUI() {
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Toolbar
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        
        JButton backButton = new JButton("Back");
        backButton.setFocusable(false);
        backButton.addActionListener(e -> FrameManager.goBack());
        toolBar.add(backButton);
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFocusable(false);
        refreshButton.addActionListener(e -> loadSaleData());
        toolBar.add(refreshButton);
        
        // table
        tableModel = new SaleTableModel();
        saleTable = new JTable(tableModel);
        saleTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        saleTable.getTableHeader().setReorderingAllowed(false);
        
        TableRowSorter<SaleTableModel> sorter = new TableRowSorter<>(tableModel);
        saleTable.setRowSorter(sorter);
        
        sorter.setSortKeys(List.of(new RowSorter.SortKey(0, SortOrder.ASCENDING)));

        // Apply numeric sorting to selected columns 
        TableUtils.sortNumericColumn(saleTable, 4);
        
        Font tableFont = new Font("Arial", Font.PLAIN, 14);
        saleTable.setFont(tableFont);
        saleTable.setRowHeight(24);
        
        JPanel searchPanel = TableUtils.createSearchPanel(saleTable);
        
        // Main panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(searchPanel, BorderLayout.EAST);
        topPanel.add(toolBar, BorderLayout.WEST);
        
        this.add(topPanel, BorderLayout.NORTH);
        this.add(new JScrollPane(saleTable), BorderLayout.CENTER);
    }

    private void loadSaleData() {
        try {
            List<Sale> sales = repository.getAllSales();
            tableModel.setSales(sales);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading sales data: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static class SaleTableModel extends AbstractTableModel {
        private List<Sale> sales;
        private final String[] columnNames = {
            "Sale ID", "Customer ID", "Car ID", "Salesman ID", "Price(RM)", "Payment Method", "Date"
        };

        public SaleTableModel() {
            this.sales = new ArrayList<>();
        }

        public void setSales(List<Sale> sales) {
            this.sales = sales;
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return sales.size();
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }
        
        @Override
        public Object getValueAt(int row, int column) {
            Sale sale = sales.get(row);
            switch (column) {
                case 0: return sale.getSaleID();
                case 1: return sale.getCustomerID();
                case 2: return sale.getCarID();
                case 3: return sale.getSalesmanID();
                case 4: return sale.getPrice();
                case 5: return sale.getPaymentMethod();
                case 6: return new SimpleDateFormat("yyyy-MM-dd").format(sale.getSaleDate());
                default: return null;
            }
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }
    }
}