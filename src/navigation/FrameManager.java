package navigation;

import Main.MainMenu;
import javax.swing.JFrame;
import java.util.Stack;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

public class FrameManager {

    private static final Stack<JFrame> frameStack = new Stack<>();

    // Show a new frame and hide the current one
    public static void showFrame(JFrame newFrame) {
        if (!frameStack.isEmpty()) {
            frameStack.peek().setVisible(false); // Hide instead of dispose
        }
        frameStack.push(newFrame);
        newFrame.setVisible(true);
        newFrame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        newFrame.setLocationRelativeTo(null);
    }

    // Reset the stack and show only the given frame
    public static void resetAndShow(JFrame newFrame) {
        while (!frameStack.isEmpty()) {
            frameStack.pop().dispose();
        }
        showFrame(newFrame);
    }

    // Go back to the previous frame
    public static void goBack() {
        if (frameStack.size() > 1) {
            frameStack.pop().dispose(); // Dispose current frame
            frameStack.peek().setVisible(true); // Show previous frame
        }
    }

    // Go back to the MainMenu and clear all frames
    public static void backToMain() {
        while (!frameStack.isEmpty()) {
            frameStack.pop().dispose();
        }
        showFrame(new MainMenu());
    }
}
