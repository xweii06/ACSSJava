package utils;

import java.io.*;

public class DataIO {
    
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
}
