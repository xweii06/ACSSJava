package utils;

import java.io.*;
import java.nio.file.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class DataIO {
    // for reading files
    public static String readFile(String filename) throws IOException {
        StringBuilder content = new StringBuilder();
        Path filePath = Paths.get("data", filename);
        if (Files.notExists(filePath.getParent())) {
            Files.createDirectories(filePath.getParent());
        }
        
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (FileNotFoundException ex) {
            System.out.println( filePath + " not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Error reading " + filePath + ": " + ex.getMessage());
        }
        return content.toString();
    }
    
    // for adding records
    public static void appendToFile(String filename, String line) throws IOException {
        Path filePath = Paths.get("data", filename);
        if (Files.notExists(filePath.getParent())) {
            Files.createDirectories(filePath.getParent());
        }

        try (PrintWriter writer = new PrintWriter(
                new FileWriter(filePath.toFile(), true))) {
            writer.println(line);
        } catch (IOException ex) {
            System.err.println("Error appending " + filePath + ": " + ex.getMessage());
        }
    }
    
    // for writing
    public static void writeFile(String filename, String line) throws IOException {
        Path filePath = Paths.get("data", filename);
        if (Files.notExists(filePath.getParent())) {
            Files.createDirectories(filePath.getParent());
        }

        try (PrintWriter writer = new PrintWriter(filePath.toFile())) {
            writer.println(line); 
        } catch (IOException ex) {
            System.err.println("Error writing to " + filename + ": " + ex.getMessage());
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
        }
        return new ImageIcon();
    }
    
    // for adding car img
    public static String saveCarImage(File imageFile, String carID, String extension) throws IOException {
        Path imageDir = Paths.get("data/carImg");
        try {
            if (!Files.exists(imageDir)) {
                Files.createDirectories(imageDir);
            }
        } catch (IOException ex) {
            throw new IOException("Failed to create image directory: " + ex.getMessage());
        }
        if (!Files.isWritable(imageDir)) {
            throw new IOException("No write permission for image directory");
        }
        String filename = "car_" + carID + "." + extension;
        Path destination = imageDir.resolve(filename);
        try {
            Files.copy(imageFile.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
            if (!Files.exists(destination)) {
                throw new IOException("Failed to verify copied image");
            }
            return destination.toString();

        } catch (IOException ex) {
            throw new IOException("Failed to save image: " + ex.getMessage());
        }
    }
    
    // for deleting car img
    public static void deleteCarImage(String imagePath) {
        if (imagePath == null || imagePath.trim().isEmpty()) {
            return;
        }
        try {
            Path imageFile = Paths.get(imagePath).normalize();
            if (Files.exists(imageFile)) {
                Files.delete(imageFile);
                System.out.println("Successfully deleted image: " + imageFile.toAbsolutePath());
            } else {
                System.out.println("Image file not found: " + imageFile.toAbsolutePath());
            }
        } catch (Exception ex) {
            System.err.println("Exception when deleting image: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
