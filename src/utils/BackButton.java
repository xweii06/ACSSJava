package utils;

import navigation.FrameManager;
import javax.swing.*;
import java.awt.*;

public class BackButton extends JButton {
    
    public BackButton() {
        this.initializeButton();
        this.addActionListener(e -> FrameManager.navigateBack());
    }
    
    private void initializeButton() {
        this.setText("Back");
        this.setBounds(10, 10, 80, 30);
        this.setFont(new Font("Arial", Font.PLAIN, 14));
        this.setFocusable(false);
    }
    
    public static void addToFrame(JFrame frame) {
        BackButton button = new BackButton();
        frame.getContentPane().add(button);
    }
}