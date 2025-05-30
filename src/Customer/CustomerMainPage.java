package Customer;

import navigation.FrameManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class CustomerMainPage extends JFrame {
    private static final String WELCOME_PANEL = "Welcome";
    private static final String EDIT_PROFILE_PANEL = "EditProfile";
    private static final String VIEW_CARS_PANEL = "ViewCars";
    private static final String ORDERS_PANEL = "MyOrders";

    private static final Color PRIMARY_COLOR = new Color(90, 0, 200);
    private static final Color SECONDARY_COLOR = new Color(70, 0, 150);
    private static final Color ACCENT_COLOR = new Color(0x2A74FF);
    private static final int FRAME_WIDTH = 1000;
    private static final int FRAME_HEIGHT = 600;
    private static final int NAV_HEIGHT = 60;

    private JPanel contentPanel;
    private CardLayout cardLayout;
    private final Customer customer;

    public CustomerMainPage(Customer customer) {
        this.customer = customer;
        initializeFrame();
        setupComponents();
        setVisible(true);
    }

    private void initializeFrame() {
        setTitle("Customer Dashboard");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void setupComponents() {
        add(createTopNavBar(), BorderLayout.NORTH);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE);

        contentPanel.add(createWelcomePanel(), WELCOME_PANEL);
        contentPanel.add(new EditProfilePage(customer), EDIT_PROFILE_PANEL);
        contentPanel.add(createEmbeddedPanel(new ViewAvailableCarsPage(customer)), VIEW_CARS_PANEL);
        contentPanel.add(new ViewAppointmentsPage(customer), ORDERS_PANEL);

        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createEmbeddedPanel(JFrame frame) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        Container contentPane = frame.getContentPane();
        JPanel wrapperPanel = new JPanel(contentPane.getLayout());

        for (Component comp : contentPane.getComponents()) {
            if (comp instanceof JButton && "Back".equals(((JButton) comp).getText())) {
                JButton newBackBtn = new JButton("Back");
                newBackBtn.setBounds(comp.getBounds());
                newBackBtn.addActionListener(e -> cardLayout.show(contentPanel, WELCOME_PANEL));
                wrapperPanel.add(newBackBtn);
            } else {
                wrapperPanel.add(comp);
            }
        }

        panel.add(wrapperPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createTopNavBar() {
        JPanel topNavBar = new JPanel(new BorderLayout());
        topNavBar.setBackground(PRIMARY_COLOR);
        topNavBar.setPreferredSize(new Dimension(FRAME_WIDTH, NAV_HEIGHT));

        topNavBar.add(createLeftNavPanel(), BorderLayout.WEST);
        topNavBar.add(createRightNavPanel(), BorderLayout.EAST);

        return topNavBar;
    }

    private JPanel createLeftNavPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 10));
        panel.setOpaque(false);

        JLabel logo = new JLabel("Customer Menu");
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        panel.add(logo);

        panel.add(Box.createHorizontalStrut(10));

        panel.add(createNavButton("Home", WELCOME_PANEL));
        panel.add(createNavButton("Available Cars", VIEW_CARS_PANEL));
        panel.add(createNavButton("My Orders", ORDERS_PANEL));

        return panel;
    }

    private JPanel createRightNavPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setOpaque(false);

        JButton userBtn = new JButton(customer.getName() + " ▼");
        styleUserMenuButton(userBtn);

        JPopupMenu menu = new JPopupMenu();
        JMenuItem editItem = new JMenuItem("Edit Profile");
        JMenuItem logoutItem = new JMenuItem("Logout");

        editItem.addActionListener(e -> cardLayout.show(contentPanel, EDIT_PROFILE_PANEL));
        logoutItem.addActionListener(e -> {
            FrameManager.resetAndShow(new CustomerLogin());
        });

        menu.add(editItem);
        menu.add(logoutItem);
        userBtn.addActionListener(e -> menu.show(userBtn, 0, userBtn.getHeight()));

        panel.add(userBtn);
        return panel;
    }

    private void styleUserMenuButton(JButton button) {
        button.setFocusPainted(false);
        button.setBackground(SECONDARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    private JButton createNavButton(String text, String cardName) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addActionListener(e -> cardLayout.show(contentPanel, cardName));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(SECONDARY_COLOR);
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR);
            }
        });

        return button;
    }

    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        gbc.gridy = 0;
        JLabel avatarLabel = loadAvatar();
        panel.add(avatarLabel, gbc);

        gbc.gridy++;
        JLabel welcome = new JLabel("Welcome, " + customer.getName() + "!");
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcome.setForeground(ACCENT_COLOR);
        panel.add(welcome, gbc);

        gbc.gridy++;
        JLabel sub = new JLabel("Feel free to explore your dashboard or update your profile.");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        sub.setForeground(Color.DARK_GRAY);
        panel.add(sub, gbc);

        return panel;
    }

    private JLabel loadAvatar() {
        File avatarFile = new File("src/resources/avatar.png");
        if (avatarFile.exists()) {
            ImageIcon icon = new ImageIcon(avatarFile.getAbsolutePath());
            Image scaled = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            return new JLabel(new ImageIcon(scaled));
        } else {
            return createPlaceholderAvatar();
        }
    }

    private JLabel createPlaceholderAvatar() {
        JLabel label = new JLabel(customer.getName().substring(0, 1).toUpperCase(), SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(120, 120));
        label.setOpaque(true);
        label.setBackground(new Color(200, 200, 200));
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 48));
        label.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        return label;
    }
}
