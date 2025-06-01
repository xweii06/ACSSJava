package repositories;

import Customer.Customer;
import Staff.CustomerManagement.PendingCustomer;
import utils.DataIO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PendingCustomerRepository {
    private static final String FILE_PATH = "pendingCustomers.txt";

    public List<PendingCustomer> getAllPendingCustomers() throws IOException {
        List<PendingCustomer> customers = new ArrayList<>();
        String data = DataIO.readFile(FILE_PATH);
        
        if (data != null) {
            for (String line : data.split("\n")) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 5) {
                        PendingCustomer customer = new PendingCustomer(parts[0], parts[1], parts[2], parts[3], parts[4]);
                        if (customer != null) {
                            customers.add(customer);
                        }
                    }
                }
            }
        }
        return customers;
    }

    public void approveCustomer(PendingCustomer pendingCustomer) throws IOException {
        // Add to approved customers file
        Customer customer = new Customer(
                pendingCustomer.getId(), 
                pendingCustomer.getName(), 
                pendingCustomer.getPhone(), 
                pendingCustomer.getEmail(), 
                pendingCustomer.getPassword());
        new CustomerRepository().add(customer);
        // Remove from pending file
        removeCustomer(pendingCustomer.getId());
    }

    public void rejectCustomer(String customerId) throws IOException {
        removeCustomer(customerId);
    }

    private void removeCustomer(String customerId) throws IOException {
        String data = DataIO.readFile(FILE_PATH);
        if (data == null || data.isEmpty()) return;

        StringBuilder newContent = new StringBuilder();
        String[] lines = data.split("\n");
        for (String line : lines) {
            if (!line.trim().isEmpty() && !line.startsWith(customerId + ",")) {
                newContent.append(line).append("\n");
            }
        }
        DataIO.writeFile(FILE_PATH, newContent.toString().trim());
    }

    public boolean hasPendingCustomers() throws IOException {
        String data = DataIO.readFile(FILE_PATH);
        return data != null && !data.isEmpty() && data.contains(",");
    }
}