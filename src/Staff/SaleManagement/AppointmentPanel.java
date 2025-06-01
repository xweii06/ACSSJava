package Staff.SaleManagement;

import repositories.AppointmentRepository;
import utils.TableUtils;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import javax.swing.table.TableRowSorter;
import navigation.FrameManager;

public class AppointmentPanel extends JPanel {
    private final AppointmentRepository repository;
    private JTable appointmentTable;
    private AppointmentTableModel tableModel;

    public AppointmentPanel(AppointmentRepository repository) {
        this.repository = repository;
        initializeUI();
        loadAppointmentData();
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
        refreshButton.addActionListener(e -> loadAppointmentData());
        toolBar.add(refreshButton);

        // Table 
        tableModel = new AppointmentTableModel();
        appointmentTable = new JTable(tableModel);
        appointmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        appointmentTable.getTableHeader().setReorderingAllowed(false);
        
        TableRowSorter<AppointmentTableModel> sorter = new TableRowSorter<>(tableModel);
        appointmentTable.setRowSorter(sorter);
        
        sorter.setSortKeys(List.of(new RowSorter.SortKey(0, SortOrder.ASCENDING)));
        TableUtils.sortNumericColumn(appointmentTable, 4);
        sorter.setComparator(5, (d1, d2) -> {
            if (d1 instanceof LocalDate && d2 instanceof LocalDate) {
                return ((LocalDate) d1).compareTo((LocalDate) d2);
            }
            return 0;
        });

        Font tableFont = new Font("Arial", Font.PLAIN, 14);
        appointmentTable.setFont(tableFont);
        appointmentTable.setRowHeight(24);
        
        JPanel searchPanel = TableUtils.createSearchPanel(appointmentTable);
        
        // Main panel 
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(searchPanel, BorderLayout.EAST);
        topPanel.add(toolBar, BorderLayout.WEST);
        
        this.add(topPanel, BorderLayout.NORTH);
        this.add(new JScrollPane(appointmentTable), BorderLayout.CENTER);
    }

    private void loadAppointmentData() {
        try {
            List<Appointment> appointments = repository.getAllAppointments();
            
            tableModel.setAppointments(appointments);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error loading appointment data: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static class AppointmentTableModel extends AbstractTableModel {
        private List<Appointment> appointments;
        private final String[] columnNames = {
            "Order ID", "Customer ID", "Car ID", "Model", "Price(RM)", "Due Date", "Status"
        };

        public AppointmentTableModel() {
            this.appointments = new ArrayList<>();
        }

        public void setAppointments(List<Appointment> appointments) {
            this.appointments = appointments;
            fireTableDataChanged();
        }
        
        public Appointment getAppointmentAt(int row) {
            return appointments.get(row);
        }

        @Override
        public int getRowCount() {
            return appointments.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public Object getValueAt(int row, int column) {
            Appointment appointment = appointments.get(row);
            
            return switch (column) {
                case 0 -> appointment.getOrderID();
                case 1 -> appointment.getCustomerID();
                case 2 -> appointment.getCarID();
                case 3 -> appointment.getModel();
                case 4 -> appointment.getPrice();
                case 5 -> appointment.getDueDate();
                case 6 -> appointment.getStatus();
                default -> null;
            };
        }
        
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return switch (columnIndex) {
                case 4 -> Double.class;
                case 5 -> LocalDate.class;
                default -> String.class;
            };
        }
    }
}