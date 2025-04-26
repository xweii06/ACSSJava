package navigation;

import javax.swing.JFrame;
import java.util.Stack;

public class FrameManager {
    private static Stack<JFrame> frameStack = new Stack<>();
    
    public static void showFrame(JFrame newFrame) {
        if (!frameStack.isEmpty()) {
            frameStack.peek().setVisible(false); // Hide instead of dispose
        }
        frameStack.push(newFrame);
        newFrame.setVisible(true);
    }
    
    public static void goBack() {
        if (frameStack.size() > 1) {
            frameStack.pop().dispose(); // Dispose current frame
            frameStack.peek().setVisible(true); // Show previous frame
        }
    }
}