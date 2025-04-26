package Staff;

import javax.swing.*;
import utils.*;

public class StaffLogin extends JFrame{
    
    public StaffLogin(){
        // Frame setup
        this.setTitle("APU Car Sales System");
        this.setSize(500, 250); // size can change
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);  // temporarily 
        this.setLayout(null);
        this.setResizable(false);
        
        BackButton.addToFrame(this);
        this.setVisible(true);
    }
}
