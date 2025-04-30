package utils;

import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class DataIO {
    
    // for reading files
    public static String readFile(String filePath) {
        String content = "";

        try (InputStream is = DataIO.class.getResourceAsStream(filePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            String line;
            while ((line = reader.readLine()) != null) {
                content += line + "\n";
            }
            return content.trim();

        } catch (Exception ex) {
            System.out.println("Error reading " + filePath + ": " + ex.getMessage());
            return null;
        }
    }
    
    // for icons
    public static ImageIcon loadIcon(String filename) {
        try {
            InputStream is = DataIO.class.getResourceAsStream("/resources/icons/" + filename);
            if (is != null) {
                return new ImageIcon(ImageIO.read(is));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ImageIcon();
    }
}
