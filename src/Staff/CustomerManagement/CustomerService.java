package Staff.CustomerManagement;

import repositories.CustomerRepository;
import repositories.AppointmentRepository;
import repositories.SaleRepository;
import Customer.Customer;
import Staff.SaleManagement.Appointment;
import Staff.SaleManagement.Sale;
import utils.*;
import java.io.IOException;
import java.util.List;

public class CustomerService {
    private final CustomerRepository repository;
    private final AppointmentRepository apptRepo;
    private final SaleRepository saleRepo;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
        this.apptRepo = new AppointmentRepository();
        this.saleRepo = new SaleRepository();
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

    public void deleteCustomer(String id) throws IOException, InvalidInputException {
        // Check for pending appointments
        List<Appointment> appointments = apptRepo.getAppointmentsByCustomerID(id);
        List<Sale> sales = saleRepo.findSalesByCustomerID(id);
        
        for (Appointment appt : appointments) {
            if ("pending".equalsIgnoreCase(appt.getStatus())) {
                throw new InvalidInputException(
                    "Cannot delete customer with pending bookings. Order ID: " + appt.getOrderID()
                );
            }
        }
        for (Appointment appt : appointments) {
            if (!"pending".equalsIgnoreCase(appt.getStatus())) {
                appt.setCustomerID("DLTD_USER_" + id);
                apptRepo.updateAppointment(appt);
            }
        }
        
        for (Sale sale : sales) {
            if (sale != null) {
                sale.setCustomerID("DLTD_USER_" + id);
                saleRepo.updateSale(sale);
            }
        }
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