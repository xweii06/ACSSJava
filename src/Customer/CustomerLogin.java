package Customer;

import javax.swing.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import utils.MainMenuButton;
import navigation.FrameManager;

public class CustomerLogin extends JFrame {
    private JTextField idField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;

    public CustomerLogin() {
        setTitle("Customer Login");
        setSize(500, 250);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JLabel idLabel = new JLabel("Customer ID:");
        idLabel.setBounds(50, 40, 100, 25);
        add(idLabel);

        idField = new JTextField();
        idField.setBounds(150, 40, 180, 25);
        add(idField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 80, 100, 25);
        add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 80, 180, 25);
        add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setBounds(80, 130, 100, 30);
        loginButton.addActionListener(e -> handleLogin());
        add(loginButton);

        registerButton = new JButton("Register");
        registerButton.setBounds(200, 130, 100, 30);
        registerButton.addActionListener(e -> FrameManager.showFrame(new CustomerRegister()));
        add(registerButton);

        MainMenuButton.addToFrame(this);
        setVisible(true);
    }

    private void handleLogin() {
        String id = idField.getText();
        String password = new String(passwordField.getPassword());

        Customer customer = CustomerManager.authenticate(id, password);
        if (customer != null) {
            JOptionPane.showMessageDialog(this, "Login successful!");
            FrameManager.showFrame(new CustomerMainPage(customer)); // login 后进入主页面
        } else {
            JOptionPane.showMessageDialog(this, "Invalid ID or Password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
