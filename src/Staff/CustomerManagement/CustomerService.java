package Staff.CustomerManagement;

import repositories.CustomerRepository;
import Customer.Customer;
import utils.*;
import java.io.IOException;
import java.util.List;

public class CustomerService {
    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public List<Customer> getAllCustomers() throws IOException {
        return repository.getAll();
    }

    public Customer getCustomerByID(String id) throws IOException {
        return repository.getByID(id);
    }

    public void updateCustomer(Customer customer) throws IOException, InvalidInputException {
        validateCustomer(customer);
        repository.update(customer);
    }

    public void deleteCustomer(String id) throws IOException {
        repository.delete(id);
    }

    public boolean customerExists(String id) throws IOException {
        return repository.getByID(id) != null;
    }
    
    private void validateCustomer(Customer customer) throws InvalidInputException {
        if (customer.getName() == null || customer.getName().trim().isEmpty()) {
            throw new InvalidInputException("Customer name is required");
        }
        if (customer.getPhone() != null && !customer.getPhone().trim().isEmpty()) {
            InputValidator.validatePhone(customer.getPhone());
        }
        if (customer.getEmail() != null && !customer.getEmail().trim().isEmpty()) {
            InputValidator.validateEmail(customer.getEmail());
        }
        if (customer.getPassword() == null || customer.getPassword().trim().isEmpty()) {
            throw new InvalidInputException("Password is required");
        }
    }
}