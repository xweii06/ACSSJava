package Staff;

import java.awt.event.*;
import javax.swing.*;
import navigation.FrameManager;
import utils.*;

public class StaffLogin extends JFrame implements ActionListener{
    
    JButton menuButton;
    
    public StaffLogin(){
        // Frame setup
        this.setTitle("APU Car Sales System");
        this.setSize(500, 250); // size can change
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);  // temporarily 
        this.setLayout(null);
        this.setResizable(false);
        
        menuButton = new JButton("Menu");
        menuButton.setBounds(100, 100, 80, 30);
        menuButton.setFocusable(false);
        menuButton.addActionListener(this);
        
        MainMenuButton.addToFrame(this);
        this.add(menuButton);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==menuButton){
            FrameManager.showFrame(new StaffMenu());
        }
    }
    
}
