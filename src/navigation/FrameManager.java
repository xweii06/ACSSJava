package navigation;

import javax.swing.JFrame;

public class FrameManager {
    private static FrameNavigator navigator;

    private FrameManager() {
        // Private constructor to prevent instantiation
    }

    public static void initialize(JFrame initialFrame) {
        navigator = new FrameNavigator(initialFrame);
    }

    public static void navigateTo(JFrame newFrame) {
        if (navigator == null) {
            throw new IllegalStateException("FrameManager not initialized. Call initialize() first.");
        }
        navigator.navigateTo(newFrame);
    }

    public static void navigateBack() {
        if (navigator == null) {
            throw new IllegalStateException("FrameManager not initialized. Call initialize() first.");
        }
        navigator.navigateBack();
    }
}