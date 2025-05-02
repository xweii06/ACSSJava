package utils;

import java.io.*;
import java.nio.file.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class DataIO {
    
    // for reading files
    public static String readFile(String filePath) {
        String content = "";
        Path path = Paths.get("data", filePath);
        
        try (BufferedReader reader = Files.newBufferedReader(path)) {
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
    
    // for adding records
    public static void appendToFile(String filename, String data) throws IOException {
        String filePath = "src/main/resources/" + filename;
        try (FileWriter fw = new FileWriter(filePath, true);
             BufferedWriter writer = new BufferedWriter(fw)) {
            writer.write(data);
            writer.newLine();
        }
    }
    
    // for icons
    public static ImageIcon loadIcon(String filename) {
        try {
            InputStream is = DataIO.class.getResourceAsStream("/resources/" + filename);
            if (is != null) {
                return new ImageIcon(ImageIO.read(is));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ImageIcon();
    }
}
