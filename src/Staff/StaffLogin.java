package Staff;

import java.awt.event.*;
import javax.swing.*;
import navigation.FrameManager;
import utils.*;

public class StaffLogin extends JFrame{
  
    public StaffLogin(){
        // Frame setup
        this.setTitle("APU Car Sales System");
        this.setSize(500, 250); // size can change
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);  // temporarily 
        this.setLayout(null);
        this.setResizable(false);
        
        MainMenuButton.addToFrame(this);
        this.setVisible(true);
    }
}
