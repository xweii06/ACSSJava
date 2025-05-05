package navigation;

import Main.MainMenu;
import javax.swing.JFrame;
import java.util.Stack;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class FrameManager {
    
    private static Stack<JFrame> frameStack = new Stack<>();
    
    public static void showFrame(JFrame newFrame) {
        if (!frameStack.isEmpty()) {
            frameStack.peek().setVisible(false); // Hide instead of dispose
        }
        frameStack.push(newFrame);
        newFrame.setVisible(true);
        newFrame.setDefaultCloseOperation(EXIT_ON_CLOSE); // temporarily
        newFrame.setLocationRelativeTo(null);
    }
    
    public static void goBack() {
        if (frameStack.size() > 1) {
            frameStack.pop().dispose(); // Dispose current frame
            frameStack.peek().setVisible(true); // Show previous frame
        }
    }
    
    public static void backToMain() {
        if (frameStack.size() > 1) {
            frameStack.pop().dispose();
            frameStack.clear();
            FrameManager.showFrame(new MainMenu());
        }
    }
}