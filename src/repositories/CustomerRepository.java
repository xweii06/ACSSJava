package repositories;

import Customer.Customer;
import utils.DataIO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepository implements UserRepository<Customer> {
    private static final String FILE_PATH = "customers.txt";
    
    @Override
    public List<Customer> getAll() throws IOException {
        List<Customer> customerList = new ArrayList<>();
        String data = DataIO.readFile(FILE_PATH);
        
        if (data != null) {
            for (String line : data.split("\n")) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 5) {
                        Customer customer = new Customer(parts[0], parts[1], parts[2], parts[3], parts[4]);
                        customerList.add(customer);
                    }
                }
            }
        }
        return customerList;
    }
    
    @Override
    public Customer getByID(String id) throws IOException {
        List<Customer> customerList = getAll();
        for (Customer customer : customerList) {
            if (customer.getId().equals(id)) {
                return customer;
            }
        }
        return null;
    }
    
    @Override
    public void add(Customer customer) throws IOException {
        DataIO.appendToFile(FILE_PATH, customer.toDataString());
    }
    
    @Override
    public void update(Customer customer) throws IOException {
        List<Customer> customerList = getAll();
        StringBuilder newData = new StringBuilder();
        
        for (Customer c : customerList) {
            if (c.getId().equals(customer.getId())) {
                newData.append(customer.toDataString()).append("\n");
            } else {
                newData.append(c.toDataString()).append("\n");
            }
        }
        DataIO.writeFile(FILE_PATH, newData.toString().trim());
    }
    
    @Override
    public void delete(String id) throws IOException {
        List<Customer> customerList = getAll();
        StringBuilder newData = new StringBuilder();
        
        for (Customer customer : customerList) {
            if (!customer.getId().equals(id)) {
                newData.append(customer.toDataString()).append("\n");
            }
        }
        DataIO.writeFile(FILE_PATH, newData.toString().trim());
    }
}