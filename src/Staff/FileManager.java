package Staff;

import utils.DataIO;

public class FileManager {
    public static final String STAFF_FILE = "staff.txt", SALESMAN_FILE = "salesman.txt", 
        CUSTOMER_FILE = "customers.txt", CAR_FILE = "car.txt";

    public static String getFilename(String menuItem) {
        switch (menuItem) {
            case "Staff Management": return STAFF_FILE;
            case "Customer Management": return CUSTOMER_FILE;
            case "Salesman Management": return SALESMAN_FILE;
            case "Car Management": return CAR_FILE;
            default: return "";
        }
    }
    
    public static String[] getLines(String filename) {
        String data = DataIO.readFile(filename);
        if (data == null || data.isEmpty()) {
            return null;
        }
        String[] lines = data.split("\n");
        return lines;
    }
}
