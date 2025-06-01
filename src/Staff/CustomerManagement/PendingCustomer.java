package Staff.CustomerManagement;

import Main.User;

public class PendingCustomer extends User {
    private final String phone;
    private final String email;

    public PendingCustomer(String id, String name, String phone, String email, String password) {
        super(id, name, password);
        this.phone = phone;
        this.email = email;
    }

    // Getters
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    
    @Override
    public String toDataString() {
        return id + "," + name + "," + phone + "," + email + "," + password;
    }    
}