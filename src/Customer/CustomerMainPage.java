package Customer;

import javax.swing.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import utils.MainMenuButton;

public class CustomerMainPage extends JFrame {
    public CustomerMainPage(Customer customer) {
        setTitle("Customer Home");
        setSize(500, 250);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JLabel label = new JLabel("Welcome, " + customer.getName() + "!");
        label.setBounds(100, 50, 300, 30);
        add(label);

        MainMenuButton.addToFrame(this); // 返回主菜单按钮
        setVisible(true);
    }
}
