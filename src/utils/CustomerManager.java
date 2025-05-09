package utils;

import Customer.Customer;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class CustomerManager {
    private static final List<Customer> customers = new ArrayList<>();
    private static final String DATA_DIR = "data";
    private static final String CUSTOMER_FILE = DATA_DIR + "/customers.txt";
    private static final String PENDING_FILE = DATA_DIR + "/pendingCustomers.txt";

    static {
        loadFromFile();
    }

    public static void addCustomer(Customer customer) {
        customers.add(customer);
        saveToFile();
    }

    public static void saveToPendingFile(Customer customer) {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
            Files.writeString(Paths.get(PENDING_FILE), customer.toDataString() + System.lineSeparator(), 
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Customer authenticate(String id, String password) {
        return customers.stream()
            .filter(c -> c.getId().equals(id) && c.getPassword().equals(password))
            .findFirst().orElse(null);
    }

    public static Customer authenticateBy(String method, String input, String password) {
        return customers.stream()
            .filter(c -> switch (method) {
                case "Customer ID" -> c.getId().equalsIgnoreCase(input);
                case "Username" -> c.getName().equalsIgnoreCase(input);
                case "Email" -> c.getEmail().equalsIgnoreCase(input);
                default -> false;
            } && c.getPassword().equals(password))
            .findFirst().orElse(null);
    }

    public static void saveToFile() {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
            List<String> lines = new ArrayList<>();
            for (Customer c : customers) {
                lines.add(c.toDataString());
            }
            Files.write(Paths.get(CUSTOMER_FILE), lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadFromFile() {
        customers.clear();
        try {
            if (!Files.exists(Paths.get(CUSTOMER_FILE))) return;

            List<String> lines = Files.readAllLines(Paths.get(CUSTOMER_FILE));
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    customers.add(new Customer(parts[0], parts[1], parts[2], parts[3], parts[4]));
                } else if (parts.length == 6) {
                    customers.add(new Customer(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String generateCustomerId() {
        return String.format("C%04d", customers.size() + 1);
    }
}
