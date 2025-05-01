package Customer; //CustomerManager.java is the logic — it handles loading, saving, registering, and logging in.: Think of it like a manager or helper behind the scenes.

import java.io.*;
import java.util.*;

public class CustomerManager {
    private static List<Customer> customers = new ArrayList<>();
    private static final String FILE_NAME = "customers.txt";

    static {
        loadFromFile();
    }

    public static void addCustomer(Customer customer) {
        customers.add(customer);
        saveToFile();
    }

    public static Customer authenticate(String id, String password) {
        for (Customer customer : customers) {
            if (customer.getId().equals(id) && customer.getPassword().equals(password)) {
                return customer;
            }
        }
        return null;
    }

    public static void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Customer customer : customers) {
                writer.write(String.join(",",
                        customer.getId(),
                        customer.getName(),
                        customer.getPhone(),
                        customer.getEmail(),
                        customer.getPassword()
                ));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadFromFile() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 5) {
                        Customer customer = new Customer(parts[0], parts[1], parts[2], parts[3], parts[4]);
                        customers.add(customer);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String generateCustomerId() {
        return String.format("C%04d", customers.size() + 1);
    }
}
