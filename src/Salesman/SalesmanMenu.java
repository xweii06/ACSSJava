package Salesman;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SalesmanMenu extends JFrame {
    private JPanel mainPanel;
    private JPanel welcomePanel;
    private JPanel profilePanel;
    private JPanel paymentPanel;
    private JPanel commentsPanel;
    private String currentSalesmanID = "S01"; // Example logged-in Salesman ID

    public SalesmanMenu() {
        setTitle("Pastel Profile");
        setSize(650, 550); // Increased height to accommodate centered layout
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, 700, 550, 40, 40));

        mainPanel = new JPanel(new CardLayout());
        mainPanel.setOpaque(false);

        welcomePanel = createWelcomePanel();
        profilePanel = createProfilePanel();
        paymentPanel = createPaymentPanel();
        commentsPanel = createCommentsPanel();
        
        mainPanel.add(welcomePanel, "welcome");
        mainPanel.add(profilePanel, "profile");
        mainPanel.add(paymentPanel, "payment");
        mainPanel.add(commentsPanel, "comments");

        add(mainPanel);
    }

    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                paintGradientBackground(g2d);
                paintWhiteBorder(g2d);
            }
        };
        panel.setLayout(null);

        JLabel welcomeLabel = new JLabel("Welcome Back, Sha!");
        welcomeLabel.setFont(new Font("Lucida Handwriting", Font.PLAIN, 28));
        welcomeLabel.setForeground(new Color(70, 50, 100));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setBounds(100, 20, 400, 40);
        panel.add(welcomeLabel);
        
        
    // Add logout button (position it where the X button was)
    JButton logoutButton = new JButton("Logout");
    logoutButton.setBounds(550, 10, 80, 30);
    logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
    logoutButton.setForeground(Color.WHITE);
    logoutButton.setBackground(new Color(120, 80, 160));
    logoutButton.setFocusPainted(false);
    logoutButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    logoutButton.addActionListener(e -> logout());
    panel.add(logoutButton);

        JLabel userIcon = new JLabel(loadScaledIcon("/resources/User_profile.png", 100, 100));
        userIcon.setBounds(250, 70, 100, 100);
        panel.add(userIcon);

        JButton profileButton = createButton("Profile", 150, 200,
                loadScaledIcon("/resources/User_profile.png", 32, 32),
                new Color(180, 220, 255));
        profileButton.addActionListener(e -> switchToProfile());
        panel.add(profileButton);

        JButton carsButton = createButton("View Cars", 150, 280,
                loadScaledIcon("/resources/view_cars.png", 32, 32),
                new Color(180, 255, 220));
        carsButton.addActionListener(e -> showCarCards());
        panel.add(carsButton);

        JButton paymentButton = createButton("Collect Payment", 150, 360,
                loadScaledIcon("/resources/payment.png", 32, 32),
                new Color(255, 220, 180));
        paymentButton.addActionListener(e -> switchToPayment());
        panel.add(paymentButton);

        JButton commentsButton = createButton("Sales Comments", 150, 440,
                loadScaledIcon("/resources/comments.png", 32, 32),
                new Color(220, 180, 255));
        commentsButton.addActionListener(e -> switchToComments());
        panel.add(commentsButton);


        return panel;
    }
    
   private void logout() {
    int confirm = JOptionPane.showConfirmDialog(
        this,
        "Are you sure you want to logout?",
        "Confirm Logout",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE
    );
    
    if (confirm == JOptionPane.YES_OPTION) {
        this.dispose(); // Close the current window
        
        // Show the login screen again
        SwingUtilities.invokeLater(() -> {
            new SalesmanLogin().setVisible(true);
        });
    }
}

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                paintGradientBackground(g2d);
            }
        };
        panel.setLayout(null);

        JButton backButton = createBackButton();
        backButton.addActionListener(e -> switchToWelcome());
        panel.add(backButton);

        JLabel titleLabel = new JLabel("Edit Profile");
        titleLabel.setFont(new Font("Lucida Handwriting", Font.PLAIN, 24));
        titleLabel.setForeground(new Color(70, 50, 100));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(150, 20, 300, 30);
        panel.add(titleLabel);

        JLabel profilePic = new JLabel(loadScaledIcon("/resources/User_profile.png", 80, 80));
        profilePic.setBounds(260, 60, 80, 80);
        panel.add(profilePic);

        JButton changePicBtn = new JButton("Change Picture");
        changePicBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        changePicBtn.setForeground(new Color(70, 50, 100));
        changePicBtn.setBackground(new Color(255, 255, 255, 180));
        changePicBtn.setBounds(240, 150, 120, 25);
        changePicBtn.setFocusPainted(false);
        changePicBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 180, 220), 1),
            BorderFactory.createEmptyBorder(2, 5, 2, 5)
        ));
        panel.add(changePicBtn);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(0, 2, 10, 15));
        formPanel.setBounds(100, 190, 400, 200);
        formPanel.setOpaque(false);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nameLabel.setForeground(new Color(70, 50, 100));
        formPanel.add(nameLabel);

        JTextField nameField = new JTextField("Sha");
        styleTextField(nameField);
        formPanel.add(nameField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        emailLabel.setForeground(new Color(70, 50, 100));
        formPanel.add(emailLabel);

        JTextField emailField = new JTextField("sha@example.com");
        styleTextField(emailField);
        formPanel.add(emailField);

        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        phoneLabel.setForeground(new Color(70, 50, 100));
        formPanel.add(phoneLabel);

        JTextField phoneField = new JTextField("012-3456789");
        styleTextField(phoneField);
        formPanel.add(phoneField);

        JLabel pwLabel = new JLabel("password:");
        pwLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pwLabel.setForeground(new Color(70, 50, 100));
        formPanel.add(pwLabel);

        JTextArea pwArea = new JTextArea("****");
        pwArea.setLineWrap(true);
        pwArea.setWrapStyleWord(true);
        pwArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pwArea.setForeground(new Color(70, 50, 100));
        pwArea.setBackground(new Color(255, 255, 255, 180));
        pwArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 180, 220), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        JScrollPane pwScroll = new JScrollPane(pwArea);
        formPanel.add(pwScroll);

        panel.add(formPanel);

        JButton saveBtn = new JButton("Save Changes");
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setBackground(new Color(120, 80, 160));
        saveBtn.setBounds(200, 410, 200, 40);
        saveBtn.setFocusPainted(false);
        saveBtn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        saveBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                saveBtn.setBackground(new Color(140, 100, 180));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                saveBtn.setBackground(new Color(120, 80, 160));
            }
        });
        panel.add(saveBtn);

        return panel;
    }

    private JPanel createPaymentPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                paintGradientBackground(g2d);
            }
        };
        panel.setLayout(null);

        JButton backButton = createBackButton();
        backButton.addActionListener(e -> switchToWelcome());
        panel.add(backButton);

        JLabel titleLabel = new JLabel("Collect Payment");
        titleLabel.setFont(new Font("Lucida Handwriting", Font.PLAIN, 24));
        titleLabel.setForeground(new Color(70, 50, 100));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(150, 20, 300, 30);
        panel.add(titleLabel);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(0, 2, 10, 15));
        formPanel.setBounds(50, 70, 500, 250);
        formPanel.setOpaque(false);

        JLabel carLabel = new JLabel("Select Car:");
        carLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        carLabel.setForeground(new Color(70, 50, 100));
        formPanel.add(carLabel);

        JComboBox<String> carComboBox = new JComboBox<>();
        populateCarComboBox(carComboBox);
        carComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        carComboBox.setBackground(new Color(255, 255, 255, 180));
        carComboBox.setBorder(BorderFactory.createLineBorder(new Color(200, 180, 220), 1));
        formPanel.add(carComboBox);

        JLabel customerLabel = new JLabel("Customer ID:");
        customerLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        customerLabel.setForeground(new Color(70, 50, 100));
        formPanel.add(customerLabel);

        JTextField customerField = new JTextField();
        styleTextField(customerField);
        formPanel.add(customerField);

        JLabel amountLabel = new JLabel("Amount (RM):");
        amountLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        amountLabel.setForeground(new Color(70, 50, 100));
        formPanel.add(amountLabel);

        JTextField amountField = new JTextField();
        styleTextField(amountField);
        formPanel.add(amountField);

        JLabel methodLabel = new JLabel("Payment Method:");
        methodLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        methodLabel.setForeground(new Color(70, 50, 100));
        formPanel.add(methodLabel);

        JComboBox<String> methodComboBox = new JComboBox<>(new String[]{"Cash", "Credit Card", "Bank Transfer"});
        methodComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        methodComboBox.setBackground(new Color(255, 255, 255, 180));
        methodComboBox.setBorder(BorderFactory.createLineBorder(new Color(200, 180, 220), 1));
        formPanel.add(methodComboBox);

        JLabel dateLabel = new JLabel("Transaction Date:");
        dateLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        dateLabel.setForeground(new Color(70, 50, 100));
        formPanel.add(dateLabel);

        JTextField dateField = new JTextField(java.time.LocalDate.now().toString());
        styleTextField(dateField);
        dateField.setEditable(false);
        formPanel.add(dateField);

        panel.add(formPanel);

        JButton submitBtn = new JButton("Record Payment");
        submitBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setBackground(new Color(120, 80, 160));
        submitBtn.setBounds(200, 350, 200, 40);
        submitBtn.setFocusPainted(false);
        submitBtn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        submitBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                submitBtn.setBackground(new Color(140, 100, 180));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                submitBtn.setBackground(new Color(120, 80, 160));
            }
        });
        submitBtn.addActionListener(e -> {
            recordPayment(
                (String) carComboBox.getSelectedItem(),
                customerField.getText(),
                amountField.getText(),
                (String) methodComboBox.getSelectedItem(),
                dateField.getText()
            );
        });
        panel.add(submitBtn);

        return panel;
    }

      private JPanel createCommentsPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                paintGradientBackground(g2d);
            }
        };
        panel.setLayout(null);

        JButton backButton = createBackButton();
        backButton.addActionListener(e -> switchToWelcome());
        panel.add(backButton);

        JLabel titleLabel = new JLabel("Sales Comments");
        titleLabel.setFont(new Font("Lucida Handwriting", Font.PLAIN, 24));
        titleLabel.setForeground(new Color(70, 50, 100));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(200, 20, 300, 30); // Adjusted position
        panel.add(titleLabel);

        JPanel entryPanel = new JPanel();
        entryPanel.setLayout(new BorderLayout(10, 10));
        entryPanel.setBounds(50, 70, 600, 250); // Increased width from 500 to 600
        entryPanel.setOpaque(false);

        JPanel carPanel = new JPanel(new BorderLayout(10, 5));
        carPanel.setOpaque(false);

        JLabel carLabel = new JLabel("Select Car:");
        carLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        carLabel.setForeground(new Color(70, 50, 100));
        carPanel.add(carLabel, BorderLayout.NORTH);

        JComboBox<String> carComboBox = new JComboBox<>();
        populateCarComboBoxForComments(carComboBox);
        carComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        carComboBox.setBackground(new Color(255, 255, 255, 180));
        carComboBox.setBorder(BorderFactory.createLineBorder(new Color(200, 180, 220), 1));
        carComboBox.setPreferredSize(new Dimension(400, 30)); // Set preferred size
        carComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value != null) {
                    // Display just the car name without the ID
                    String fullText = value.toString();
                    setText(fullText.substring(0, fullText.lastIndexOf("(")).trim());
                }
                return this;
            }
        });
        carPanel.add(carComboBox, BorderLayout.CENTER);

        entryPanel.add(carPanel, BorderLayout.NORTH);

        JPanel commentPanel = new JPanel(new BorderLayout(10, 5));
        commentPanel.setOpaque(false);

        JLabel commentLabel = new JLabel("Your Comment:");
        commentLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        commentLabel.setForeground(new Color(70, 50, 100));
        commentPanel.add(commentLabel, BorderLayout.NORTH);

        JTextArea commentArea = new JTextArea();
        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);
        commentArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        commentArea.setForeground(new Color(70, 50, 100));
        commentArea.setBackground(new Color(255, 255, 255, 180));
        commentArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 180, 220), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        JScrollPane scrollPane = new JScrollPane(commentArea);
        commentPanel.add(scrollPane, BorderLayout.CENTER);

        entryPanel.add(commentPanel, BorderLayout.CENTER);

        panel.add(entryPanel);

        JButton submitBtn = new JButton("Save Comment");
        submitBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setBackground(new Color(120, 80, 160));
        submitBtn.setBounds(250, 350, 200, 40); // Centered position
        submitBtn.setFocusPainted(false);
        submitBtn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        submitBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                submitBtn.setBackground(new Color(140, 100, 180));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                submitBtn.setBackground(new Color(120, 80, 160));
            }
        });
        submitBtn.addActionListener(e -> {
            saveComment(
                (String) carComboBox.getSelectedItem(),
                commentArea.getText()
            );
        });
        panel.add(submitBtn);

        return panel;
    }
    private void showStatusUpdateDialog(String[] car) {
    // First show password verification dialog
    String password = JOptionPane.showInputDialog(this, 
        "Enter Salesman Password to Update Status:", 
        "Password Verification", 
        JOptionPane.PLAIN_MESSAGE);
    
    // Verify password (using "0123" as the salesman password)
    if (password == null || !password.equals("0123")) {
        JOptionPane.showMessageDialog(this, 
            "Incorrect password. Only authorized salesman can update car status.",
            "Access Denied", 
            JOptionPane.ERROR_MESSAGE);
        return;
    }
      // Proceed with status update dialog if password is correct
    JDialog dialog = new JDialog(this, "Update Car Status", true);
    dialog.setSize(400, 300);
    dialog.setLocationRelativeTo(this);
    dialog.setUndecorated(true);
    dialog.setShape(new RoundRectangle2D.Double(0, 0, 400, 300, 30, 30));
  
       JPanel dialogPanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            paintGradientBackground(g2d);
        }
    };
        dialogPanel.setLayout(new BorderLayout());
    dialogPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    JLabel titleLabel = new JLabel("Update Status: " + car[1] + " " + car[2]);
    titleLabel.setFont(new Font("Lucida Handwriting", Font.PLAIN, 18));
    titleLabel.setForeground(new Color(70, 50, 100));
    titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
    dialogPanel.add(titleLabel, BorderLayout.NORTH);

    JLabel currentStatusLabel = new JLabel("Current Status: " + car[5]);
    currentStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
    currentStatusLabel.setForeground(new Color(70, 50, 100));
    currentStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);
    dialogPanel.add(currentStatusLabel, BorderLayout.CENTER);

    JPanel statusPanel = new JPanel(new GridLayout(0, 1, 10, 10));
    statusPanel.setOpaque(false);

        String[] statusOptions = {"Available","Cancelled"};
    ButtonGroup statusGroup = new ButtonGroup();

    for (String status : statusOptions) {
        JRadioButton radioButton = new JRadioButton(status);
        radioButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        radioButton.setForeground(new Color(70, 50, 100));
        radioButton.setOpaque(false);
        if (status.equalsIgnoreCase(car[5])) {
            radioButton.setSelected(true);
        }
        statusGroup.add(radioButton);
        statusPanel.add(radioButton);
    }

    dialogPanel.add(statusPanel, BorderLayout.CENTER);

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
    buttonPanel.setOpaque(false);

    JButton updateButton = new JButton("Update");
    updateButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
    updateButton.setForeground(Color.WHITE);
    updateButton.setBackground(new Color(120, 80, 160));
    updateButton.setFocusPainted(false);
    updateButton.addActionListener(e -> {
        String newStatus = "";
        for (Component comp : statusPanel.getComponents()) {
            if (comp instanceof JRadioButton) {
                JRadioButton rb = (JRadioButton) comp;
                if (rb.isSelected()) {
                    newStatus = rb.getText();
                    break;
                }
            }
        }
        
        updateCarStatus(car[0], newStatus);
        dialog.dispose();
        showCarCards();
    });

    JButton cancelButton = new JButton("Cancel");
    cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
    cancelButton.setForeground(new Color(70, 50, 100));
    cancelButton.setBackground(new Color(255, 255, 255, 180));
    cancelButton.setFocusPainted(false);
    cancelButton.addActionListener(e -> dialog.dispose());

    buttonPanel.add(updateButton);
    buttonPanel.add(cancelButton);
    dialogPanel.add(buttonPanel, BorderLayout.SOUTH);

    dialog.add(dialogPanel);
    dialog.setVisible(true);
}

    private void updateCarStatus(String carID, String newStatus) {
        try {
            List<String[]> cars = readCarData("data/Car.txt");
            
            for (String[] car : cars) {
                if (car[0].equals(carID)) {
                    car[5] = newStatus;
                    break;
                }
            }
            
            writeCarData("data/Car.txt", cars);
            
            JOptionPane.showMessageDialog(this, "Car status updated successfully!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating car status: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void writeCarData(String filePath, List<String[]> cars) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String[] car : cars) {
                bw.write(String.join(",", car));
                bw.newLine();
            }
        }
    }

    private void showCarCards() {
        JPanel carPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                paintGradientBackground(g2d);
            }
        };

        JButton backButton = createBackButton();
        backButton.addActionListener(e -> switchToWelcome());
        carPanel.add(backButton);

        JLabel titleLabel = new JLabel("Available Cars");
        titleLabel.setFont(new Font("Lucida Handwriting", Font.PLAIN, 24));
        titleLabel.setForeground(new Color(70, 50, 100));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(150, 20, 300, 30);
        carPanel.add(titleLabel);

        JPanel cardContainer = new JPanel(new GridLayout(0, 2, 20, 20));
        cardContainer.setOpaque(false);
        cardContainer.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(cardContainer);
        scrollPane.setBounds(20, 60, 560, 400);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        carPanel.add(scrollPane);

        List<String[]> carData = readCarData("data/Car.txt");
        for (String[] car : carData) {
            JPanel card = createCarCard(car);
            cardContainer.add(card);
        }

        mainPanel.add(carPanel, "cars");
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, "cars");
    }
 
    private JPanel createCarCard(String[] car) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(255, 255, 255, 220));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 180, 220), 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setMaximumSize(new Dimension(250, 300));

        ImageIcon carIcon = new ImageIcon(car[7]);
        JLabel carImage = new JLabel(new ImageIcon(carIcon.getImage().getScaledInstance(200, 120, Image.SCALE_SMOOTH)));
        carImage.setAlignmentX(Component.CENTER_ALIGNMENT);
        carImage.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        card.add(carImage);

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setOpaque(false);
        detailsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel name = new JLabel(car[1] + " " + car[2]);
        name.setFont(new Font("Segoe UI", Font.BOLD, 16));
        name.setForeground(new Color(70, 50, 100));
        name.setAlignmentX(Component.CENTER_ALIGNMENT);
        detailsPanel.add(name);

        detailsPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JLabel price = new JLabel("RM " + car[4]);
        price.setFont(new Font("Segoe UI", Font.BOLD, 14));
        price.setForeground(new Color(120, 80, 160));
        price.setAlignmentX(Component.CENTER_ALIGNMENT);
        detailsPanel.add(price);

        detailsPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JLabel status = new JLabel("Status: " + car[5]);
        status.setFont(new Font("Segoe UI", Font.BOLD, 12));
        switch(car[5].toLowerCase()) {
            case "available":
                status.setForeground(new Color(50, 150, 50));
                break;
            case "booked":
                status.setForeground(new Color(200, 150, 0));
                break;
            case "paid":
                status.setForeground(new Color(0, 100, 200));
                break;
            case "cancelled":
                status.setForeground(new Color(200, 50, 50));
                break;
            default:
                status.setForeground(new Color(70, 50, 100));
        }
        status.setAlignmentX(Component.CENTER_ALIGNMENT);
        detailsPanel.add(status);

        detailsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        card.add(detailsPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setMaximumSize(new Dimension(200, 40));

    
        JButton updateBtn = createCardButton("Update", new Color(180, 255, 220));
        updateBtn.addActionListener(e -> showStatusUpdateDialog(car));

        
        buttonPanel.add(updateBtn);

        card.add(buttonPanel);

        return card;
    }

    private JButton createCardButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(new Color(60, 60, 60));
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 180, 220), 1),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(brighter(bgColor, 1.2f));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        return button;
    }

    private void populateCarComboBox(JComboBox<String> comboBox) {
        
       //Addd cars from data file 
        List<String[]> cars = readCarData("data/Car.txt");
        for (String[] car : cars) {
            if (car[5].equalsIgnoreCase("booked")) {
                comboBox.addItem(car[1] + " " + car[2] + " (" + car[0] + ")");
            }
        }
      
        //Add hardcoded Perodua cars
        comboBox.addItem("Perodua Myvi (PM001)");
        comboBox.addItem("Perodua Axia (PM001)");
    }
    

     private void populateCarComboBoxForComments(JComboBox<String> comboBox) {
         //Add cars from data file 
        List<String[]> cars = readCarData("data/Car.txt");
        for (String[] car : cars) {
            if (car[5].equalsIgnoreCase("paid")) {
                // Format: "Make Model (ID)"
                comboBox.addItem(car[1] + " " + car[2] + " (" + car[0] + ")");
            }
        }
        // Add hardcoded Perodua cars 
        comboBox.addItem("Perodua Myvi (PM001)");
        comboBox.addItem("Perodua Axia (PM001)");
    }

    private void recordPayment(String carInfo, String customer, String amount, String method, String date) {
        if (customer.isEmpty() || amount.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Parse payment amount
        double paymentAmount = Double.parseDouble(amount);
        String carId = carInfo.substring(carInfo.lastIndexOf("(") + 1, carInfo.length() - 1);
        String carName = carInfo.substring(0, carInfo.lastIndexOf("(")).trim();
        
        // Validate payment amount for specific cars
        if (carName.contains("Perodua Myvi") && paymentAmount != 45000) {
            JOptionPane.showMessageDialog(this, 
                "Payment amount must be exactly RM 45,000 for Perodua Myvi",
                "Invalid Amount", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (carName.contains("Perodua Axia") && paymentAmount != 59500) {
            JOptionPane.showMessageDialog(this, 
                "Payment amount must be exactly RM 59,500 for Perodua Axia",
                "Invalid Amount", JOptionPane.ERROR_MESSAGE);
            return;
        }
            
            // Create payment record string 
            String paymentRecord = String.format("%s,%s,%s,%s,%s,%s", 
            carId, customer, amount, method, date, currentSalesmanID);
            
            // Save to Record Payment.txt
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/Record Payment.txt", true))) {
            writer.write(String.format("%s,%s,%.2f,%s,%s,%s", 
                carId, customer, paymentAmount, method, date, currentSalesmanID));
            writer.newLine();
        }
         
         // If it's one of our hardcoded cars, don't update status
        if (!carId.equals("PM001") && !carId.equals("PA001")) {
            updateCarStatus(carId, "Paid");
        }
   
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/Payments.txt", true))) {
                writer.write(String.format("%s,%s,%s,%s,%s,%s\n", 
                    carId, customer, amount, method, date, currentSalesmanID));
            }
            
            updateCarStatus(carId, "Paid");
            
            
        JOptionPane.showMessageDialog(this, "Payment recorded successfully!", 
            "Success", JOptionPane.INFORMATION_MESSAGE);
        switchToWelcome();
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Insufficient funds",
            "Invalid Amount", JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error recording payment: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    private void saveComment(String carInfo, String comment) {
        if (comment.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a comment", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String carId = carInfo.substring(carInfo.lastIndexOf("(") + 1, carInfo.length() - 1);
            
            // Create comment record string
        String commentRecord = String.format("%s,%s,%s,%s", 
            carId, currentSalesmanID, java.time.LocalDate.now().toString(), comment);
        
            // Save to Sales Comment.txt
           try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/Sales Comment.txt", true))) {
            writer.write(commentRecord);
            writer.newLine();
        }
        
        JOptionPane.showMessageDialog(this, "Comment saved successfully!", 
            "Success", JOptionPane.INFORMATION_MESSAGE);
        switchToWelcome();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error saving comment: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    private JButton createBackButton() {
        JButton backButton = new JButton("Back");
        backButton.setIcon(loadScaledIcon("/resources/back_arrow.png", 16, 16));
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backButton.setForeground(new Color(70, 50, 100));
        backButton.setBackground(new Color(255, 255, 255, 180));
        backButton.setBounds(20, 20, 100, 30);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 180, 220), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                backButton.setBackground(new Color(255, 255, 255, 220));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                backButton.setBackground(new Color(255, 255, 255, 180));
            }
        });
        return backButton;
    }

    private void switchToProfile() {
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, "profile");
    }

    private void switchToWelcome() {
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, "welcome");
    }

    private void switchToPayment() {
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, "payment");
    }

    private void switchToComments() {
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, "comments");
    }

    private Color brighter(Color color, float factor) {
        int r = Math.min((int)(color.getRed() * factor), 255);
        int g = Math.min((int)(color.getGreen() * factor), 255);
        int b = Math.min((int)(color.getBlue() * factor), 255);
        return new Color(r, g, b, color.getAlpha());
    }

    private List<String[]> readCarData(String filePath) {
        List<String[]> carList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                carList.add(line.split(","));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return carList;
    }

    private void paintGradientBackground(Graphics2D g2d) {
        GradientPaint gradient = new GradientPaint(
                0, 0, new Color(255, 209, 220),
                getWidth(), getHeight(), new Color(220, 210, 255)
        );
        g2d.setPaint(gradient);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
    }

    private void paintWhiteBorder(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(3));
        g2d.setColor(new Color(255, 255, 255, 80));
        g2d.drawRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 35, 35);
    }

    private JButton createButton(String text, int x, int y, ImageIcon icon, Color bgColor) {
        JButton button = new JButton(text);
        button.setBounds(x, y, 300, 60);
        button.setFont(new Font("Segoe UI", Font.BOLD, 20));
        button.setForeground(new Color(60, 60, 60));
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setIcon(icon);
        if (icon != null) button.setIconTextGap(15);
        return button;
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(new Color(70, 50, 100));
        field.setBackground(new Color(255, 255, 255, 180));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 180, 220), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }

    private ImageIcon loadScaledIcon(String path, int width, int height) {
        try {
            return new ImageIcon(new ImageIcon(getClass().getResource(path)).getImage()
                    .getScaledInstance(width, height, Image.SCALE_SMOOTH));
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
    // Start with the login screen
    SwingUtilities.invokeLater(() -> {
        new SalesmanLogin().setVisible(true);
    });
}
}