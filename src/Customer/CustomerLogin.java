package Customer;

import javax.swing.*;
import utils.*;

public class CustomerLogin extends JFrame{
    public CustomerLogin(){
        // Frame setup
        this.setTitle("APU Car Sales System");
        this.setSize(500, 250); // size can change
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);  // temporarily 
        this.setLayout(null);
        this.setResizable(false);
        
        MainMenuButton.addToFrame(this);
        this.setVisible(true);;
    }
}
