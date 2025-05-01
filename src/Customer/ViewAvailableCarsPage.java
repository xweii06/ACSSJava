package Customer;

import javax.swing.*;
import navigation.FrameManager;

public class ViewAvailableCarsPage extends JFrame {
    private Customer customer;

    public ViewAvailableCarsPage(Customer customer) {
        this.customer = customer;

        setTitle("Available Cars");
        setSize(400, 400);
        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea carListArea = new JTextArea();
        carListArea.setEditable(false);
        carListArea.setBounds(20, 20, 340, 250);
        carListArea.setText(
            "Available Cars:\n" +
            "1. Toyota Vios 2020 - RM70,000\n" +
            "2. Honda City 2021 - RM80,000\n" +
            "3. Proton X70 2022 - RM100,000\n" +
            "4. Perodua Myvi 2022 - RM50,000\n"
        );
        add(carListArea);

        JButton backButton = new JButton("Back");
        backButton.setBounds(140, 300, 120, 30);
        add(backButton);

        backButton.addActionListener(e -> FrameManager.showFrame(new CustomerMainPage(customer)));
    }
}
