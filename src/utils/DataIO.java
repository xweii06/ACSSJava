package utils;

import java.io.*;
import java.nio.file.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class DataIO {
    
    // for reading files
    public static String readFile(String filename) {
        String content = "";
        Path filePath = Paths.get("data", filename);
        
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                content += line + "\n";
            }
            return content.trim();
        } catch (FileNotFoundException ex) {
            System.out.println( filePath + " not found: " + ex.getMessage());
            return null;
        } catch (IOException ex) {
            System.out.println("Error reading " + filePath + ": " + ex.getMessage());
            return null;
        }
    }
    
    // for adding records
    public static void appendToFile(String filename, String data) throws IOException {
        Path filePath = Paths.get("data", filename);
        if (Files.notExists(filePath.getParent())) {
            Files.createDirectories(filePath.getParent());
        }

        try (PrintWriter writer = new PrintWriter(
                new FileWriter(filePath.toFile(), true))) {
            writer.println(data);
        } catch (IOException ex) {
            System.out.println("Error reading " + filePath + ": " + ex.getMessage());
        }
    }
    
    // for icons
    public static ImageIcon loadIcon(String filename) {
        try {
            InputStream is = DataIO.class.getResourceAsStream("/resources/" + filename);
            if (is != null) {
                return new ImageIcon(ImageIO.read(is));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return new ImageIcon();
    }
}
