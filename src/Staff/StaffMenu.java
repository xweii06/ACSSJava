package Staff;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.*;
import navigation.FrameManager;
import utils.DataIO;

public class StaffMenu extends JFrame {
    
    private Staff currentStaff;
    private static final String EXITPIN_FILE = "/data/exitPIN.txt";
    private static final String ADDCAR_PNG = "add_car.png", DELCAR_PNG = "del_car.png", 
            UPDATECAR_PNG = "edit_car.png", SEARCHCAR_PNG = "search_car.png";
    
    private JPanel mainPanel, sidebar, contentPanel, titlePanel, subMenuPanel;
    private JLabel pageTitle, titleLabel;
    private JTable recordsTable;
    private JButton currentlySelectedButton = null; 
    
    public StaffMenu(Staff staff) {
        this.currentStaff = staff;
        this.setTitle("Staff Menu");
        this.setSize(1000, 600);
        this.setLocationRelativeTo(null);
        
        mainPanel = new JPanel(new BorderLayout());
        
        JPanel sidebarContent = createSidebar();
        JScrollPane sidebarScroll = new JScrollPane(sidebarContent);
        sidebarScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sidebarScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        sidebarScroll.setBorder(null);
        mainPanel.add(sidebarScroll, BorderLayout.WEST);
        
        contentPanel = new JPanel();
        contentPanel.setBackground(Color.white);
        JScrollPane contentScroll = new JScrollPane(contentPanel);
        contentScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        contentScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainPanel.add(contentScroll, BorderLayout.CENTER);

        this.add(mainPanel);
        showDefaultContent(contentPanel,staff.getName());
        this.setVisible(true);
    }
    
    private JPanel createSidebar() {
        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Color.black);
        sidebar.setMaximumSize(new Dimension(180, 600));
    
        pageTitle = new JLabel("Staff Dashboard");
        pageTitle.setFont(new Font("Arial", Font.BOLD, 18));
        pageTitle.setForeground(new Color(0x2A74FF));
        pageTitle.setBorder(BorderFactory.createEmptyBorder(20, 20, 30, 20));
        sidebar.add(pageTitle);
        
        ArrayList<String> menuItems = new ArrayList<>();
        if (currentStaff.isSuperAdmin()) {
            menuItems.add("Staff Management"); 
        } else {
            menuItems.add("Salesman Management");
            menuItems.add("Customer Management"); 
            menuItems.add("Car Management");
            menuItems.add("Payment & Feedback Analysis");
            menuItems.add("Reports");
            menuItems.add("End Program");
        }
        
        for (String menuItem : menuItems) {
            JButton menuButton = new JButton(menuItem);
            styleMenuButton(menuButton);
            sidebar.add(menuButton);
            menuButton.addActionListener(e -> switchContent(menuItem));
        }
        
        sidebar.add(Box.createVerticalGlue()); 
        JButton logoutBtn = new JButton("Logout");
        styleMenuButton(logoutBtn);
        logoutBtn.addActionListener(e -> {
            currentStaff = null;
            Window[] windows = Window.getWindows();
            for (Window window : windows) {
                if (window != this) {
                    window.dispose();
                }
            }
            FrameManager.goBack();
        });
        sidebar.add(logoutBtn);
        return sidebar;
    }
    
    private void showDefaultContent(JPanel contentPanel, String staffName) {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        
        JPanel welcomePanel = new JPanel(new GridLayout(6,1));
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(200,10,100,10));
        
        JLabel welcomeText = new JLabel("Welcome "+ staffName+ "! \\(@^0^@)/",JLabel.CENTER);
        welcomeText.setFont(new Font("Arial", Font.PLAIN, 18));
        welcomeText.setForeground(new Color(150,150,150));
        
        JLabel instructionText = new JLabel("-Please select a menu option-",JLabel.CENTER);
        instructionText.setFont(new Font("Arial", Font.PLAIN, 18));
        instructionText.setForeground(new Color(150,150,150));
        
        welcomePanel.add(welcomeText);
        welcomePanel.add(instructionText);
        contentPanel.add(welcomePanel);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    public void switchContent(String menuItem) {
        if (menuItem.equals("End Program")) {
            handleExitProgram();
            return;
        }
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        
        titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));
        titlePanel.setBackground(Color.lightGray);

        titleLabel = new JLabel(menuItem);
        titleLabel.setFont(new Font("MV Boli", Font.BOLD, 30));
        titleLabel.setForeground(Color.black);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        titlePanel.add(titleLabel, BorderLayout.CENTER);
        contentPanel.add(titlePanel, BorderLayout.NORTH);
        
        subMenuPanel = new JPanel();
        subMenuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        subMenuPanel.setBackground(Color.white);
        if (menuItem.equals("Payment & Feedback Analysis") || menuItem.equals("Reports")) {
            subMenuPanel.setLayout(new GridLayout(0, 4, 15, 15));
        } else {
            subMenuPanel.setLayout(new BorderLayout());
        }
        
        switch(menuItem) {
            case "Staff Management":
            case "Salesman Management":
            case "Car Management":
            case "Customer Management":
                JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,10,5));
                btnPanel.setBackground(Color.white);
                
                if (!menuItem.equals("Customer Management")) {
                    JButton addBtn = styleActionButton("Add New");
                    addBtn.setBackground(new Color(0x08A045));
                    addBtn.setForeground(Color.white);
                    addBtn.addActionListener(e -> {
                        AddNewRecords.createAddFrame(menuItem);
                        addBtn.setEnabled(false);
                    });
                    btnPanel.add(addBtn);
                } else {
                    JButton approveBtn = styleActionButton("Approve");
                    approveBtn.setBackground(new Color(0x08A045));
                    approveBtn.setForeground(Color.white);
                    approveBtn.addActionListener(e -> ApproveCus.handlePendingCus());
                    btnPanel.add(approveBtn);
                }
                
                JButton delBtn = styleActionButton("Delete");
                delBtn.setBackground(new Color(0xE31A3E));
                delBtn.setForeground(Color.white);
                delBtn.addActionListener(e -> {
                    DeleteRecords.deleteSelectedRecords(menuItem, recordsTable);
                    refreshMenu(menuItem);
                });
                btnPanel.add(delBtn);
                
                JButton updateBtn = styleActionButton("Update");
                updateBtn.setBackground(new Color(0x7092BE));
                updateBtn.setForeground(Color.white);
                updateBtn.addActionListener(e -> {
                    int selectedRow = recordsTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        String[] rowData = new String[recordsTable.getColumnCount()];
                        for (int i = 0; i < rowData.length; i++) {
                            rowData[i] = recordsTable.getValueAt(selectedRow, i).toString();
                        }
                        UpdateRecords.createUpdateFrame(menuItem, rowData);
                        updateBtn.setEnabled(false);
                    } else {
                        JOptionPane.showMessageDialog(null, 
                            "Please select a record to update", 
                            "No Selection", JOptionPane.WARNING_MESSAGE);
                    }
                });
                btnPanel.add(updateBtn);
                
                subMenuPanel.add(btnPanel,BorderLayout.NORTH);
                displayTable(menuItem);
                break;
            case "Payment & Feedback Analysis":
                addSubMenuButton(subMenuPanel, "Payment Records",
                        DataIO.loadIcon(ADDCAR_PNG),null);
                addSubMenuButton(subMenuPanel, "Pending Payments",
                        DataIO.loadIcon(DELCAR_PNG),null);
                addSubMenuButton(subMenuPanel, "User Feedback",
                        DataIO.loadIcon(SEARCHCAR_PNG),null);
                addSubMenuButton(subMenuPanel, "Rating Analysis",
                        DataIO.loadIcon(UPDATECAR_PNG),null);
                break;
            case "Reports":
                addSubMenuButton(subMenuPanel, "Sales Records",
                        DataIO.loadIcon(ADDCAR_PNG),null);
                addSubMenuButton(subMenuPanel, "Sales Analysis",
                        DataIO.loadIcon(DELCAR_PNG),null);
                addSubMenuButton(subMenuPanel, "Sales by Salesman",
                        DataIO.loadIcon(SEARCHCAR_PNG),null);
                addSubMenuButton(subMenuPanel, "Revenue Analysis",
                        DataIO.loadIcon(UPDATECAR_PNG),null);
                break;
        }
        
        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        wrapperPanel.setBackground(Color.white);
        wrapperPanel.add(subMenuPanel);

        contentPanel.add(wrapperPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void styleMenuButton(JButton button) {
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(250, 50));
        button.setPreferredSize(new Dimension(250, 50));
        button.setBackground(Color.black);
        button.setForeground(Color.white)   ;
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setHorizontalAlignment(JLabel.LEFT);
        button.setFocusPainted(false);
        
        button.addActionListener(e -> {
        if (currentlySelectedButton != null) {
            currentlySelectedButton.setBackground(Color.black);
            currentlySelectedButton.setForeground(Color.white);
            }
            button.setBackground(Color.white);
            button.setForeground(Color.black);

            currentlySelectedButton = button;
        });
    }
    
    private JButton styleActionButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        return button;
    }
    
    private void addSubMenuButton(JPanel panel, String text, ImageIcon icon, ActionListener action) {
        JButton button = new JButton(text);
        button.setIcon(icon);
        
        button.setVerticalTextPosition(JLabel.BOTTOM);
        button.setHorizontalTextPosition(JLabel.CENTER);
        
        button.setFont(new Font("Arial Narrow", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(160, 350));
        button.setMaximumSize(new Dimension(160, 350));
        button.setMinimumSize(new Dimension(160, 350));
        
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        
        Border btnBorder = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.black),
            BorderFactory.createEmptyBorder(100,20,100,20));
        button.setBorder(btnBorder);
        button.setIconTextGap(10); 
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setContentAreaFilled(true);
                button.setBackground(new Color(230, 240, 255));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setContentAreaFilled(false);
            }
        });
        
        if (action == null) {
            button.setEnabled(false);
        }
        button.addActionListener(action);
        panel.add(button);
    }
    
    private void handleExitProgram() {
        String exitPIN = JOptionPane.showInputDialog("Enter Exit PIN:");
        if (exitPIN != null){
            try {
                if (checkExitPIN(exitPIN)) {
                    int confirmation= JOptionPane.showConfirmDialog(this,"Are you sure?",
                                    "Confirmation",JOptionPane.YES_NO_OPTION);
                    if (confirmation == 0) {
                        System.exit(0);
                    }
                } else {
                    JOptionPane.showMessageDialog(this,"Invalid PIN.",
                            "Error",JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                    "Error accessing records",
                    "System Error",
                    JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
    
    private boolean checkExitPIN(String exitPIN) throws IOException{
        String savedExitPIN = DataIO.readFile(EXITPIN_FILE);
            if (savedExitPIN != null){
                if (savedExitPIN.equals(exitPIN)){
                    return true;
                }
            }
        return false;
    }
    
    private void displayTable(String menuItem) {
        recordsTable = ViewAllRecords.getRecordsTable(menuItem);

        JPanel controlPanel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JTextField searchField = new JTextField(20);
        JButton searchBtn = new JButton("Search");
        searchBtn.setFocusable(false);

        searchBtn.addActionListener(e -> {
            TableRowSorter<TableModel> sorter = new TableRowSorter<>(recordsTable.getModel());
            recordsTable.setRowSorter(sorter);
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchField.getText()));
        });

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);

        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        JLabel sortLabel = new JLabel("Sort by:");
        sortLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        String[] columnNames = ViewAllRecords.getColumnsFor(menuItem);
        JComboBox<String> sortComboBox = new JComboBox<>(columnNames);
        sortComboBox.insertItemAt("Default", 0);
        sortComboBox.setSelectedIndex(0);
        sortComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        sortComboBox.setPreferredSize(new Dimension(130, 30));

        sortComboBox.addActionListener(e -> {
            String selected = (String) sortComboBox.getSelectedItem();
            if ("Default".equals(selected)) {
                recordsTable.setRowSorter(null);
                return;
            }

            int columnIndex = -1;
            for (int i = 0; i < columnNames.length; i++) {
                if (columnNames[i].equals(selected)) {
                    columnIndex = i;
                    break;
                }
            }

            if (columnIndex >= 0) {
                if (menuItem.equals("Car Management") && columnIndex == 2 || columnIndex == 4) {
                    // year and price
                    sortNumericColumn(recordsTable, columnIndex);
                } else {
                    sortTable(recordsTable, columnIndex);
                }
            }
        });

        sortPanel.add(sortLabel);
        sortPanel.add(sortComboBox);
        controlPanel.add(sortPanel, BorderLayout.WEST);
        controlPanel.add(searchPanel, BorderLayout.EAST);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setMinimumSize(new Dimension(650, 350));
        tablePanel.setPreferredSize(new Dimension(650, 350));
        tablePanel.add(controlPanel, BorderLayout.NORTH);
        tablePanel.add(new JScrollPane(recordsTable), BorderLayout.CENTER);

        if (menuItem.equals("Car Management")) {
            JButton viewImageBtn = new JButton("View Selected Car Image");
            viewImageBtn.addActionListener(e -> {
                int selectedRow = recordsTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String carID = (String) recordsTable.getValueAt(selectedRow, 0);
                    showImageInFrame(carID);
                } else {
                    JOptionPane.showMessageDialog(null, 
                        "Please select a car first", 
                        "No Selection", JOptionPane.WARNING_MESSAGE);
                }
            });

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.add(viewImageBtn);
            tablePanel.add(buttonPanel, BorderLayout.SOUTH);
        }

        subMenuPanel.add(tablePanel, BorderLayout.CENTER);
    }
    
    private void sortTable(JTable table, int columnIndex) {
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(sorter);
        sorter.setSortKeys(Arrays.asList(
                new RowSorter.SortKey(columnIndex, SortOrder.ASCENDING)));
    }

    private void sortNumericColumn(JTable table, int columnIndex) {
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
    
    private void showImageInFrame(String carID) {
        String imagePath = null;
        
        String[] lines = FileManager.getLines("car.txt");
        for (String line : lines) {
            String parts[] = line.split(",");
            String savedID = parts[0];
            if (parts.length >= 8 && savedID.equals(carID)) {
                imagePath = parts[7];
                break;
            }
        }
        
        if (imagePath == null || imagePath.isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                "No image available for this car", 
                "Image Not Found", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            JFrame imageFrame = new JFrame("Car Image - " + carID);
            imageFrame.setSize(600, 400);
            imageFrame.setLocationRelativeTo(null);
            
            ImageIcon originalIcon = new ImageIcon(imagePath);
            Image originalImage = originalIcon.getImage();
            int maxWidth = 550;
            int maxHeight = 350;

            int originalWidth = originalIcon.getIconWidth();
            int originalHeight = originalIcon.getIconHeight();

            double widthRatio = (double)maxWidth / originalWidth;
            double heightRatio = (double)maxHeight / originalHeight;
            double ratio = Math.min(widthRatio, heightRatio);

            int scaledWidth = (int)(originalWidth * ratio);
            int scaledHeight = (int)(originalHeight * ratio);
            
            Image scaledImage = originalImage.getScaledInstance(
                scaledWidth, scaledHeight, Image.SCALE_SMOOTH);

            JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
            imageLabel.setHorizontalAlignment(JLabel.CENTER);
            imageLabel.setHorizontalAlignment(JLabel.CENTER);
            imageFrame.add(imageLabel); 
            imageFrame.pack();
            imageFrame.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, 
                "Failed to load image: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void refreshMenu(String menuItem) {
        Window[] windows = Window.getWindows();
        for (Window window : windows) {
            if (window instanceof StaffMenu) {
                StaffMenu staffMenu = (StaffMenu)window;
                staffMenu.switchContent(menuItem);
                break;
            }
        }
    }
}