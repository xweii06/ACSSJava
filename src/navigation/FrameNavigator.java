package navigation;

import javax.swing.JFrame;

public class FrameNavigator {
    private JFrame currentFrame;
    private JFrame previousFrame;

    public FrameNavigator(JFrame initialFrame) {
        this.currentFrame = initialFrame;
        this.previousFrame = null;
    }

    public void navigateTo(JFrame newFrame) {
        if (currentFrame != null) {
            currentFrame.setVisible(false);
            previousFrame = currentFrame;
        }
        currentFrame = newFrame;
        currentFrame.setVisible(true);
    }

    public void navigateBack() {
        if (previousFrame != null) {
            currentFrame.dispose();
            currentFrame = previousFrame;
            currentFrame.setVisible(true);
            previousFrame = null; // Clear previous frame after going back
        }
    }
}