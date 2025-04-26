package utils;

import javax.swing.*;
import java.awt.*;
import navigation.FrameManager;

public class MainMenuButton extends JButton{
    public MainMenuButton() {
        this.customizeButton();
        this.addActionListener(event -> FrameManager.backToMain());
    }
    
    private void customizeButton() {
        this.setText("Main Menu");
        this.setBounds(10, 10, 120, 30);
        this.setFont(new Font("Arial", Font.PLAIN, 14));
        this.setFocusable(false);
    }
    
    public static void addToFrame(JFrame frame) {
        MainMenuButton button = new MainMenuButton();
        frame.getContentPane().add(button);
    }
}
