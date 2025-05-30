package Customer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import utils.AppointmentManager;

public class ViewAvailableCarsPage extends JFrame {
    private static final Color HEADER_COLOR = new Color(90, 0, 200);
    private static final Color BUTTON_COLOR = new Color(0x2A74FF);
    private static final Color HOVER_COLOR = new Color(0x1A64EF);

    private final Customer customer;
    private JPanel gridPanel;
    private JTextField searchField;
    private List<String[]> allCars;
    private Timer autoRefreshTimer;
    private ViewAppointmentsFrame appointmentsFrame;

    public ViewAvailableCarsPage(Customer customer) {
        this.customer = customer;
        initializeFrame();
        setupComponents();
        startAutoRefresh();
    }

    private void initializeFrame() {
        setTitle("Available Cars");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void setupComponents() {
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createCarGridPanel(), BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(HEADER_COLOR);
        panel.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel title = new JLabel("Available Cars", JLabel.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        panel.add(title, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);

        searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        rightPanel.add(searchField);

        JButton searchBtn = new JButton("Search");
        styleButton(searchBtn);
        searchBtn.addActionListener(e -> filterCars());
        rightPanel.add(searchBtn);

        JButton refreshBtn = new JButton("Refresh");
        styleButton(refreshBtn);
        refreshBtn.addActionListener(e -> refreshCarList());
        rightPanel.add(refreshBtn);

        JButton appointmentsBtn = new JButton("My Appointments");
        styleButton(appointmentsBtn);
        appointmentsBtn.addActionListener(e -> new ViewAppointmentsFrame(customer));
        rightPanel.add(appointmentsBtn);

        JButton backBtn = new JButton("Return");
        styleButton(backBtn);
        backBtn.addActionListener(e -> dispose());
        rightPanel.add(backBtn);

        panel.add(rightPanel, BorderLayout.EAST);
        return panel;
    }

    private JScrollPane createCarGridPanel() {
        gridPanel = new JPanel(new GridLayout(0, 3, 15, 15));
        gridPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        gridPanel.setBackground(Color.WHITE);

        allCars = getAvailableCars();
        updateGrid(allCars);

        return new JScrollPane(gridPanel);
    }

    private void updateGrid(List<String[]> carsToDisplay) {
        gridPanel.removeAll();
        for (String[] car : carsToDisplay) {
            gridPanel.add(createCarCard(car));
        }
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private void filterCars() {
        String keyword = searchField.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            updateGrid(allCars);
        } else {
            List<String[]> filtered = allCars.stream()
                    .filter(car -> car[1].toLowerCase().contains(keyword))
                    .collect(Collectors.toList());
            updateGrid(filtered);
        }
    }

    private JPanel createCarCard(String[] car) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        card.setBackground(Color.WHITE);
        
        JLabel imageLabel;
        if (car.length < 8) {
            imageLabel = createNoImageLabel();
        } else {
            String imagePath = car[7].trim();
            if (imagePath != null) {
                try {
                    File imageFile = new File(imagePath);
                    if (imageFile.exists() && imageFile.canRead()) {
                        ImageIcon icon = new ImageIcon(imagePath);
                        Image scaledImage = icon.getImage().getScaledInstance(200, 120, Image.SCALE_SMOOTH);
                        imageLabel = new JLabel(new ImageIcon(scaledImage));
                    } else {
                        imageLabel = createNoImageLabel();
                    }
                } catch (Exception e) {
                    imageLabel = createNoImageLabel();
                }
            } else {
                imageLabel = createNoImageLabel();
            }
        }
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        card.add(imageLabel, BorderLayout.CENTER);

        JLabel title = new JLabel(car[1] + " - " + car[2], JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));
        card.add(title, BorderLayout.NORTH);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBorder(new EmptyBorder(5, 10, 5, 10));

        JLabel price = new JLabel("Price: " + car[4] + " | Color: " + car[3]);

        JButton viewBtn = new JButton("View");
        styleButton(viewBtn);
        viewBtn.addActionListener(e -> new CarDetailsPage(car));

        JButton bookBtn = new JButton("Book");
        styleButton(bookBtn);
        bookBtn.addActionListener(e -> {
            Runnable onBookingConfirmed = () -> {
                if (appointmentsFrame != null && appointmentsFrame.isDisplayable()) {
                    appointmentsFrame.refreshAppointments();
                }
            };
            new BookAppointmentPage(customer, car, onBookingConfirmed);
        });

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        btnPanel.setOpaque(false);
        btnPanel.add(viewBtn);
        btnPanel.add(bookBtn);

        bottom.add(price, BorderLayout.WEST);
        bottom.add(btnPanel, BorderLayout.EAST);
        card.add(bottom, BorderLayout.SOUTH);

        return card;
    }

    private JLabel createNoImageLabel() {
        ImageIcon icon = new ImageIcon("src/resources/noCar.png");
        JLabel imageLabel = new JLabel(icon);
        return imageLabel;
    }

    private void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setBackground(BUTTON_COLOR);
        button.setForeground(Color.WHITE);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(HOVER_COLOR);
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(BUTTON_COLOR);
            }
        });
    }

    private List<String[]> getAvailableCars() {
        List<String[]> cars = new ArrayList<>();
        List<String> bookedCarIds = AppointmentManager.getBookedCarIds();

        try (BufferedReader br = new BufferedReader(new FileReader("data/car.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    String carId = parts[0];
                    String status = parts[5].trim();
                    if ("Available".equalsIgnoreCase(status) && !bookedCarIds.contains(carId)) {
                        cars.add(parts);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cars;
    }

    private void refreshCarList() {
        allCars = getAvailableCars();
        filterCars(); // Reapply current search filter
    }

    private void startAutoRefresh() {
        autoRefreshTimer = new Timer(10000, e -> refreshCarList());
        autoRefreshTimer.start();
    }

    @Override
    public void dispose() {
        if (autoRefreshTimer != null) {
            autoRefreshTimer.stop();
        }
        super.dispose();
    }
}

class ViewAppointmentsFrame extends JFrame {
    private ViewAppointmentsPage appointmentsPage;
    
    public ViewAppointmentsFrame(Customer customer) {
        setTitle("My Appointments");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        appointmentsPage = new ViewAppointmentsPage(customer);
        add(appointmentsPage, BorderLayout.CENTER);
        setVisible(true);
    }
    
    // Method to refresh appointments from outside
    public void refreshAppointments() {
        if (appointmentsPage != null) {
            appointmentsPage.refreshAppointments();
        }
    }
    
    @Override
    public void dispose() {
        if (appointmentsPage != null) {
            appointmentsPage.stopAutoRefresh();
        }
        super.dispose();
    }
}